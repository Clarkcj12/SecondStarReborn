package club.imaginears.secondstarreborn.utils;

import club.imaginears.secondstarreborn.SecondStar;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("MismatchedReadAndWriteOfArray")
@Getter
public class ConfigUtil {
    private Favicon favicon;
    private String motd, motdTemp;
    private ServerPing.Players[] motdInfo;
    private String maintenanceMotd, maintenanceMotdTemp;
    private boolean maintenanceMode;
    private int chatDelay;
    private boolean dmEnabled;
    private boolean strictChat;
    private double strictThreshold;
    private List<String> mutedChats;
    private List<String> announcements;

    private final Path configPath = Path.of("config.yml");

    /**
     * Reloads the configuration from the YAML file.
     */
    public void reload() throws IOException {
        var loader = YamlConfigurationLoader.builder()
                .path(configPath)
                .build();
        ConfigurationNode node = loader.load();

        // Load variosu configuration properties
        favicon = Favicon.load(node.node("favicon").getString());
        motd = node.node("motd").getString();
        motdTemp = motd;
        maintenanceMode = node.node("maintenance").getBoolean();
        maintenanceMotd = node.node("maintenanceMotd").getString();
        maintenanceMotdTemp = maintenanceMotd;
        chatDelay = node.node("chatDelay").getInt();
        dmEnabled = node.node("dmEnabled").getBoolean();
        strictChat = node.node("strictChat").getBoolean();
        strictThreshold = node.node("strictThreshold").getDouble();
        mutedChats = node.node("mutedChats").getList(String.class, List.of());
        announcements = node.node("announcements").getList(String.class, List.of());
    }


        Configuration fileConfig = getConfig();
        boolean versionOverride;
        if (fileConfig.contains("versions")) {
            SecondStar.getInstance().getLogger().warning("Bypassing MongoDB config for MC version settings due to file override");
            Configuration versions = fileConfig.getSection("versions");
            ProtocolConstants.setHighVersion(versions.getInt("maxVersion"), versions.getString("maxVersionText"));
            ProtocolConstants.setLowVersion(versions.getInt("minVersion"), versions.getString("minVersionText"));
            versionOverride = true;
        } else {
            versionOverride = false;
        }

        VelocityConfig velocityConfig = getVelocityConfig();
        this.favicon = velocityConfig.favicon;
        this.motdTemp = velocityConfig.motd;
        this.motd = this.motdTemp.replaceAll("%n%", System.getProperty("line.separator"));
        this.motdInfo = new ServerPing.Players[velocityConfig.motdInfo.length];
        for (int i = 0; i < velocityConfig.motdInfo.length; i++) {
            this.motdInfo[i] = new ServerPing.Players(NamedTextColor.translateAlternateColorCodes('&', velocityConfig.motdInfo[i]), "");
        }
        this.maintenanceMotdTemp = velocityConfig.maintenanceMotd;
        this.maintenanceMotd = this.maintenanceMotdTemp.replaceAll("%n%", System.getProperty("line.separator"));
        this.maintenance = velocityConfig.maintenance;
        this.chatDelay = velocityConfig.chatDelay;
        this.dmEnabled = velocityConfig.dmEnabled;
        this.strictChat = velocityConfig.strictChat;
        this.strictThreshold = velocityConfig.strictThreshold;
        this.mutedChats = velocityConfig.mutedChats;
        this.announcements = velocityConfig.announcements;

        if (!versionOverride) {
            ProtocolConstants.setHighVersion(velocityConfig.maxVersion, velocityConfig.maxVersionString);
            ProtocolConstants.setLowVersion(velocityConfig.minVersion, velocityConfig.minVersionString);
        }

        if (this.maintenance) {
            for (Player tp : new ArrayList<>(SecondStar.getOnlinePlayers())) {
                try {
                    if (tp.getRank().getRankId() < Rank.DEVELOPER.getRankId()) {
                        tp.kickPlayer(NamedTextColor.AQUA + "Palace Network has entered a period of maintenance!\nFollow " +
                                NamedTextColor.BLUE + "@PalaceDev " + NamedTextColor.AQUA + "on Twitter for updates.", false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public DatabaseConnection getRabbitMQInfo() {
        try {
            Configuration config = getConfig().getSection("rabbitmq");
            return new DatabaseConnection(config.getString("host"), config.getString("username"), config.getString("password"), config.getString("virtualhost"), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseConnection("", "", "", "", 0);
        }
    }

    public DatabaseConnection getMongoDBInfo() {
        try {
            Configuration config = getConfig().getSection("mongodb");
            return new DatabaseConnection(config.getString("hostname"), config.getString("username"), config.getString("password"), config.getString("database"), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseConnection("", "", "", "", 0);
        }
    }

    public DatabaseConnection getSQLInfo() {
        try {
            Configuration config = getConfig().getSection("sql");
            return new DatabaseConnection(config.getString("host"), config.getString("username"), config.getString("password"), config.getString("database"), config.getInt("port"));
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseConnection("", "", "", "", 0);
        }
    }

    public BungeeConfig getBungeeConfig() {
        try {
            return SecondStar.getMongoHandler().getBungeeConfig();
        } catch (Exception e) {
            e.printStackTrace();
            return new BungeeConfig(null, "", new String[0], "", false, 2, true, false, 0.8, 0, 0, "", "", new ArrayList<>(), new ArrayList<>());
        }
    }

    public Configuration getConfig() throws IOException {
        return ConfigurationProvider.getProvider(YamlConfigurationLoader.class).load(getConfigFile());
    }

    public File getConfigFile() throws IOException {
        File folder = new File("plugins/PalaceBungee");
        if (!folder.exists()) folder.mkdir();

        File file = new File(folder, "config.yml");
        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }

    public void setChatDelay(int seconds) throws Exception {
        this.chatDelay = seconds;
        saveConfigChanges();
    }

    public void setMaintenanceMode(boolean maintenance) throws Exception {
        this.maintenance = maintenance;
        saveConfigChanges();
    }

    public void setDmEnabled(boolean dmEnabled) throws Exception {
        this.dmEnabled = dmEnabled;
        saveConfigChanges();
    }

    public void setMutedChats(List<String> mutedChats, boolean dbUpdate) throws Exception {
        this.mutedChats = mutedChats;
        if (dbUpdate) saveConfigChanges();
    }

    public void setAnnouncements(List<String> announcements, boolean dbUpdate) throws Exception {
        this.announcements = announcements;
        if (dbUpdate) saveConfigChanges();
    }

    public void setStrictChat(boolean strictChat) throws Exception {
        this.strictChat = strictChat;
        saveConfigChanges();
    }

    public void setStrictThreshold(double strictThreshold) throws Exception {
        this.strictThreshold = strictThreshold;
        saveConfigChanges();
    }

    private void saveConfigChanges() throws Exception {
        BungeeConfig config = new BungeeConfig(null, null, null, null,
                maintenance, chatDelay, dmEnabled, strictChat, strictThreshold,
                0, 0, null, null, mutedChats, announcements);
        SecondStar.getMongoHandler().setVelocityConfig(config);
        SecondStar.getMessageHandler().sendMessage(new ProxyReloadPacket(), SecondStar.getMessageHandler().ALL_PROXIES);
    }

    public boolean isTestNetwork() throws IOException {
        Configuration config = getConfig();
        if (config.contains("testNetwork")) return config.getBoolean("testNetwork", true);
        return true;
    }

    @Getter
    @AllArgsConstructor
    public static class DatabaseConnection {
        private final String host, username, password, database;
        private final int port;
    }

    @Getter
    @AllArgsConstructor
    public static class BungeeConfig {
        private final Favicon favicon;
        private final String motd;
        private final String[] motdInfo;
        private final String maintenanceMotd;
        private final boolean maintenance;
        private final int chatDelay;
        private final boolean dmEnabled;
        private final boolean strictChat;
        private final double strictThreshold;
        private final int maxVersion;
        private final int minVersion;
        private final String maxVersionString;
        private final String minVersionString;
        private final List<String> mutedChats;
        private final List<String> announcements;
    }
}