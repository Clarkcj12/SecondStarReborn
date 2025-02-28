package club.imaginears.secondstarreborn.utils;

import club.imaginears.secondstarreborn.SecondStar;
import club.imaginears.secondstarreborn.handlers.Player;
import club.imaginears.secondstarreborn.handlers.Server;
import com.velocitypowered.api.proxy.server.ServerInfo;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;

public class ServerUtil {
    private final Map<String, Server> servers = HashMap.newHashMap(); // Java 22 `HashMap` factory method
    private ServerInfo currentHub;
    @Getter
    private int onlineCount = 0;
    @Getter
    private final List<String> onlinePlayerNames = Collections.synchronizedList(new ArrayList<>());

    // Scheduled executor to replace `Timer` for periodic tasks
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public ServerUtil() {
        loadServers();

        currentHub = getServerInfo("Hub1", true);

        SecondStar.getProxyServer().setReconnectHandler(new ReconnectHandler() {
            @Override
            public ServerInfo getServer(com.velocitypowered.api.proxy.Player proxiedPlayer) {
                return currentHub;
            }

            @Override
            public void setServer(com.velocitypowered.api.proxy.Player proxiedPlayer) {
            }

            @Override
            public void save() {
            }

            @Override
            public void close() {
            }
        });

        // Schedule periodic tasks to replace `Timer`
        scheduler.scheduleAtFixedRate(this::updateOnlineCount, 2, 5, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::updateOnlinePlayerNames, 2, 5, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::determineCurrentHub, 2, 5, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::removeDisconnectedPlayers, 5, 5, TimeUnit.SECONDS);
    }

    private void loadServers() {
        servers.clear();
        try {
            List<Server> serverList = SecondStar.getMongoHandler().getServers(SecondStar.isTestNetwork());
            for (Server server : serverList) {
                servers.put(server.getName(), server);
                try {
                    String[] addressList = server.getAddress().split(":");
                    ServerInfo serverInfo = SecondStar.getProxyServer().createServer(
                            new InetSocketAddress(addressList[0], Integer.parseInt(addressList[1])),
                            server.getName(),
                            Optional.empty()
                    );
                    SecondStar.getProxyServer().registerServer(serverInfo);
                } catch (Exception e) {
                    SecondStar.getProxyServer().getLogger().log(Level.SEVERE, "Error registering server: " + server.getName(), e);
                }
            }
            SecondStar.getProxyServer().getLogger().info("Successfully loaded " + serverList.size() + " servers!");
        } catch (Exception e) {
            SecondStar.getProxyServer().getLogger().log(Level.SEVERE, "Error loading servers, shutting down proxy server.", e);
            System.exit(1);
        }
    }

    private void updateOnlineCount() {
        try {
            onlineCount = SecondStar.getMongoHandler().getOnlineCount();
        } catch (Exception e) {
            SecondStar.getProxyServer().getLogger().log(Level.SEVERE, "Error retrieving global player count", e);
        }
    }

    private void updateOnlinePlayerNames() {
        try {
            List<String> names = SecondStar.getMongoHandler().getOnlinePlayerNames();
            synchronized (onlinePlayerNames) {
                onlinePlayerNames.clear();
                onlinePlayerNames.addAll(names);
            }
        } catch (Exception e) {
            SecondStar.getProxyServer().getLogger().log(Level.SEVERE, "Error updating online player name list", e);
        }
    }

    private void determineCurrentHub() {
        try {
            int currentCount = servers.get(currentHub.getName()).getCount();
            for (Server server : servers.values()) {
                if (!server.getName().startsWith("Hub")) continue;
                if (server.getCount() < currentCount) {
                    currentCount = server.getCount();
                    currentHub = getServerInfo(server.getName(), true);
                }
            }
        } catch (Exception e) {
            SecondStar.getProxyServer().getLogger().log(Level.SEVERE, "Error determining currentHub", e);
        }
    }

    private void removeDisconnectedPlayers() {
        try {
            for (Player player : SecondStar.getOnlinePlayers()) {
                if (player.getProxiedPlayer().isEmpty() && (System.currentTimeMillis() - player.getLoginTime()) > 5000) {
                    SecondStar.logout(player.getUniqueId(), player);
                }
            }
        } catch (Exception e) {
            SecondStar.getProxyServer().getLogger().log(Level.SEVERE, "Error maintaining online player list", e);
        }
    }

    public ServerInfo getServerInfo(String name, boolean exact) {
        return SecondStar.getProxyServer().getAllServers().stream()
                .map(server -> (ServerInfo) server)
                .filter(info -> exact ? info.getName().equals(name) : info.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Server getServer(String name, boolean exact) {
        return servers.values().stream()
                .filter(server -> exact ? server.getName().equals(name) : server.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public int getServerCount(String name) {
        return SecondStar.getMongoHandler().getServerCount(name);
    }

    public void createServer(Server server) {
        servers.put(server.getName(), server);
    }

    public void deleteServer(String name) {
        servers.remove(name);
    }

    public List<Server> getServers() {
        return List.copyOf(servers.values());
    }

    public void sendPlayer(com.velocitypowered.api.proxy.Player player, String serverName) {
        Server server = getServer(serverName, true);
        if (server != null) server.join(player);
    }

    public void sendPlayer(com.velocitypowered.api.proxy.Player player, Server server) {
        if (server != null) server.join((Player) player);
    }

    public String getChannel(Player player) {
        Server server = getServer(player.getServerName(), true);
        return server != null && server.isPark() ? "ParkChat" : Optional.ofNullable(server).map(Server::getName).orElse("");
    }

    public boolean isOnPark(Player player) {
        Server server = getServer(player.getServerName(), true);
        return server != null && server.isPark();
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }
}