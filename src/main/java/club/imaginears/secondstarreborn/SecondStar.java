package club.imaginears.secondstarreborn;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.UUID;

@Plugin(id = "secondstarreborn", name = "SecondStarReborn", version = BuildConstants.VERSION, url = "https://imaginears.club", authors = {"Imaginears Development Team"})
public class SecondStar {
    /**
     * A unique identifier for the proxy instance. This is statically initialized
     * with a randomly generated UUID when the class is loaded. It is used to
     * differentiate this specific proxy instance from others, ensuring uniqueness
     * across multiple instances or environments.
     */
    @Getter private static final UUID proxyID = UUID.randomUUID();
    /**
     * The static instance of the SecondStar class.
     * This variable is used to provide global access to the main class instance.
     * Typically initialized during the plugin's lifecycle to ensure a single
     * instance of the class exists.
     */
    @Getter private static SecondStar instance;
    /**
     * A static instance of the {@link ConfigUtil} class utilized for managing and accessing
     * configuration-related utilities within the SecondStar plugin.
     *
     * This instance acts as a centralized point for interacting with configuration settings,
     * ensuring consistency and ease of access across the application.
     *
     * It is assumed to be initialized and managed internally by the plugin's lifecycle.
     */
    @Getter private static ConfigUtil configUtil;
    /**
     * Singleton instance of the {@link ServerUtil} class, utilized for performing
     * server-related operations and utilities.
     * The {@code serverUtil} variable is statically accessible and provides functionalities
     * related to server management within the scope of the plugin.
     */
    @Getter private static ServerUtil serverUtil;

    /**
     * A static instance of {@link AFKUtil}, utilized for managing and
     * handling functionalities related to player's AFK (Away From Keyboard) status.
     * This utility provides methods that simplify AFK detection and status updates,
     * ensuring seamless player activity monitoring within the application.
     */
    @Getter private static AFKUtil afkUtil;
    /**
     * A static utility class that provides functionalities for managing and handling
     * broadcast operations within the plugin. This utility is designed to facilitate
     * sending messages or notifications to multiple players or systems in a streamlined
     * and centralized manner.
     */
    @Getter private static BroadcastUtil broadcastUtil;
    /**
     * A static and final instance of the JaroWinkler algorithm.
     * This is utilized for comparing strings and determining their similarity,
     * often used in text matching scenarios. The Jaro-Winkler algorithm is
     * particularly effective for measuring similarity of short strings,
     * typically used in applications like chat message processing.
     */
    @Getter private static final JaroWinkler chatAlgorithm = new JaroWinkler();
    /**
     * Provides utilities and helper methods related to chat functionality.
     * This static instance is used throughout the application to centralize chat operations.
     */
    @Getter private static ChatUtil chatUtil;
    /**
     * A utility class for managing forum-related features and functionality in the application.
     * Developers can use ForumUtil for operations associated with the forum system.
     *
     * This is a singleton instance accessed using the getter method.
     */
    @Getter private static ForumUtil forumUtil;
    /**
     * Singleton instance of the GuideUtil class.
     * Provides utility functions for managing or interacting with guides.
     * This static instance is shared across the application.
     */
    @Getter private static GuideUtil guideUtil;
    /**
     * A static instance of the ModerationUtil class, which provides utility methods
     * and functionalities related to moderation features within the application.
     */
    @Getter private static ModerationUtil moderationUtil;
    /**
     * The PartyUtil instance for handling party-related operations
     * in the SecondStar plugin. This utility provides functionalities
     * to manage player parties, including creation, management, and
     * interaction within groups.
     */
    @Getter private static PartyUtil partyUtil;
    /**
     * A utility object for handling password-related operations within the application.
     * This singleton instance provides methods and functionalities required for
     * managing, securing, and validating passwords.
     */
    @Getter private static PasswordUtil passwordUtil;
    /**
     * A static utility instance of the SlackUtil class, utilized for interacting with
     * Slack-related functionalities within the application.
     */
    @Getter private static SlackUtil slackUtil;

    /**
     * A static instance of {@link MongoHandler} used to interact with a MongoDB database.
     * This variable is primarily intended to provide database handling functionality
     * throughout the application. It is accessible globally within the application
     * whenever MongoDB-related operations are required.
     */
    @Getter private static MongoHandler mongoHandler;
    /**
     * A static instance of the {@code MessageHandler} class used to handle
     * the messaging subsystem within the SecondStar plugin. This handler
     * facilitates tasks such as sending, receiving, and processing messages
     * between different components and/or systems integrated within the plugin.
     */
    @Getter private static MessageHandler messageHandler;

    /**
     * Represents the start time of the application in milliseconds since the epoch.
     * This variable is initialized when the application starts and can be used
     * to determine the runtime or uptime of the application.
     */
    @Getter private static final long startTime = System.currentTimeMillis();
    /**
     * A static map that stores player data, mapped by their unique UUID.
     * This map is used for managing and accessing player-related information.
     * The key is a UUID representing the player's unique identifier, and the value is a Player object that encapsulates the player's details.
     */
    private final static HashMap<UUID, Player> players = new HashMap<>();

    /**
     * A static cache that stores the association between player UUIDs and their corresponding usernames.
     * This cache is used to efficiently retrieve a player's username using their UUID, reducing the
     * need for repetitive queries or lookups to external systems or databases.
     *
     * Key: The UUID of a player.
     * Value: The corresponding username of the player.
     *
     * This cache is expected to be updated and maintained by the system, with entries added or removed
     * as players interact with the system or their data changes.
     */
    @Getter private final static HashMap<UUID, String> usernameCache = new HashMap<>();
    /**
     * A static boolean flag indicating whether the application is running in a
     * test network environment. This variable is intended to differentiate between
     * a production environment and a testing environment, which could influence
     * application behavior and functionality, such as connecting to alternate
     * services or databases for testing purposes.
     */
    @Getter private static boolean testNetwork;

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
