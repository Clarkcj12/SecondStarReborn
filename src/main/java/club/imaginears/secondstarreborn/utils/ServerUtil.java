package club.imaginears.secondstarreborn.utils;

import club.imaginears.secondstarreborn.SecondStar;
import com.mongodb.internal.connection.Server;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.ServerInfo;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.logging.Level;

public class ServerUtil {
    private final HashMap<String, Server> servers = new HashMap<>();
    private ServerInfo currentHub;
    @Getter private int onlineCount = 0;
    @Getter private final List<String> onlinePlayerNames = new ArrayList<>();

    public ServerUtil() {
        loadServers();

        currentHub = getServerInfo("Hub1", true);

        SecondStar.getProxyServer().setReconnectHandler(new ReconnectHandler() {
            @Override
            public ServerInfo getServer(Player proxiedPlayer) {
                return currentHub;
            }

            @Override
            public void setServer(Player proxiedPlayer) {
            }

            @Override
            public void save() {
            }

            @Override
            public void close() {
            }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    onlineCount = SecondStar.getMongoHandler().getOnlineCount();
                } catch (Exception e) {
                    SecondStar.getProxyServer().getLogger().log(Level.SEVERE, "Error retrieving global player count", e);
                }
                try {
                    List<String> names = SecondStar.getMongoHandler().getOnlinePlayerNames();
                    onlinePlayerNames.clear();
                    onlinePlayerNames.addAll(names);
                } catch (Exception e) {
                    SecondStar.getProxyServer().getLogger().log(Level.SEVERE, "Error updating online player name list", e);
                }
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
                try {
                    for (Player tp : SecondStar.getOnlinePlayers()) {
                        if (tp.getProxiedPlayer().isEmpty() && (System.currentTimeMillis() - tp.getLoginTime()) > 5000) {
                            SecondStar.logout(tp.getUniqueId(), tp);
                        }
                    }
                } catch (Exception e) {
                    SecondStar.getProxyServer().getLogger().log(Level.SEVERE, "Error maintaining online player list", e);
                }
            }
        }, 2000L, 5000L);
    }

    private void loadServers() {
        servers.clear();
//        ProxyServer.getInstance().getServers().clear();
        try {
            List<Server> servers = SecondStar.getMongoHandler().getServers(SecondStar.isTestNetwork());
            for (Server s : servers) {
                this.servers.put(s.getName(), s);
                try {
                    String[] addressList = s.getAddress().split(":");
                    ServerInfo info = ProxyServer.getInstance().constructServerInfo(s.getName(),
                            new InetSocketAddress(addressList[0], Integer.parseInt(addressList[1])), "", false);
                    ProxyServer.getInstance().getServers().put(info.getName(), info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            SecondStar.getProxyServer().getLogger().info("Successfully loaded " + servers.size() + " servers!");
        } catch (Exception e) {
            SecondStar.getProxyServer().getLogger().log(Level.SEVERE, "Error loading servers, stopping bungee server!", e);
            System.exit(1);
        }
    }

    public ServerInfo getServerInfo(String name, boolean exact) {
        for (ServerInfo info : ProxyServer.getInstance().getServers().values()) {
            if ((exact && info.getName().equals(name)) || (!exact && info.getName().equalsIgnoreCase(name)))
                return info;
        }
        return null;
    }

    public Server getServer(String name, boolean exact) {
        if (exact) return servers.get(name);
        for (Server s : servers.values()) {
            if (s.getName().equalsIgnoreCase(name))
                return s;
        }
        return null;
    }

    public int getServerCount(net.md_5.bungee.api.connection.Server server) {
        return getServerCount(server.getInfo().getName());
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
        return new ArrayList<>(servers.values());
    }

    public void sendPlayer(Player player, String serverName) {
        Server server = getServer(serverName, true);
        if (server == null) return;
        server.join(player);
    }

    public void sendPlayer(Player player, Server server) {
        if (server == null) return;
        server.join(player);
    }

    public void handleServerSwitch(UUID uuid, ServerInfo fromInfo, ServerInfo toInfo) {
        Player tp = SecondStar.getPlayer(uuid);
        Server from = fromInfo == null ? null : getServer(fromInfo.getName(), true);
        Server to = toInfo == null ? null : getServer(toInfo.getName(), true);
        if (from == null) {
            // new connection
            if (tp.isDisabled()) {
                try {
                    tp.sendPacket(new DisablePlayerPacket(tp.getUniqueId(), true), true);
                } catch (Exception e) {
                    e.printStackTrace();
                    tp.kickPlayer("Internal Exception: java.io.IOException: An existing connection was forcibly closed by the remote host", false);
                }
            }
        }
        if (to == null) {
            // unknown error
        } else {
            SecondStar.getMongoHandler().setPlayerServer(uuid, to.getName());
        }
    }

    public Server getServerByType(String serverType) {
        return getServerByType(serverType, null);
    }

    public Server getServerByType(String serverType, UUID exclude) {
        Server server = null;
        for (Server s : servers.values()) {
            if ((s.getUniqueId().equals(exclude)) || !s.isOnline()) continue;
            if (s.getServerType().equals(serverType)) {
                if (server == null) {
                    server = s;
                    continue;
                }
                if (s.getCount() < server.getCount()) server = s;
            }
        }
        return server;
    }

    public void sendPlayerByType(Player player, String serverType) {
        Server server = getServerByType(serverType);
        if (server != null) server.join(player);
    }


    public Server getEmptyParkServer(UUID exclude) {
        Server s = null;
        List<Server> servers = new ArrayList<>(this.servers.values());
        for (Server server : servers) {
            if ((server.getUniqueId().equals(exclude)) || !server.isOnline() || !server.isPark()) continue;
            if (s == null) {
                s = server;
                continue;
            }
            if (server.getCount() < s.getCount()) {
                s = server;
            }
        }
        return s;
    }

    public String getChannel(Player player) {
        String serverName = player.getServerName();
        Server server = getServer(serverName, true);
        if (server == null) {
            return "";
        }
        return server.isPark() ? "ParkChat" : server.getName();
    }

    public boolean isOnPark(Player tp) {
        Server server = getServer(tp.getServerName(), true);
        return server != null && server.isPark();
    }
}