package club.imaginears.secondstarreborn;

import club.imaginears.secondstarreborn.command.Commands;
import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.Scheduler;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

@Plugin(id = "secondstar",
        name = "SecondStar-Velocity",
        version = "1.0-SANPSHOT",
        description = "SecondStar Velocity plugin for Imaginears Club",
        authors = {"ImaginearsClubDevs"})
public class SecondStar {

    @Getter private static final UUID proxyID = UUID.randomUUID();
    @Getter private static ConfigUtil configUtil;
    @Getter private static ServerUtil serverUtil;


    @Getter private static MongoHandler mongoHandler;
    @Getter private static MessageHandler messageHandler;

    @Getter private static final long startTime = System.currentTimeMillis();
    private final static HashMap<UUID, Player> players = new HashMap<>();
    @Getter private static final HashMap<UUID, String> usernameCache = new HashMap<>();

    @Getter private static boolean testNetwork;
    public final ProxyServer server;
    public static SecondStar instance;
    public final Logger logger;
    public final Path dataDirectory;

    @Inject
    public SecondStar(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        instance = this;
    }

    public Scheduler.TaskBuilder task(Runnable r) {
        return this.server.getScheduler().buildTask(this, r);
    }
}