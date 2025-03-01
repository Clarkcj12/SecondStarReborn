package club.imaginears.secondstarreborn.handlers.moderation;

import club.imaginears.secondstarreborn.SecondStar;
import club.imaginears.secondstarreborn.handlers.Player;
import club.imaginears.secondstarreborn.handlers.Rank;
import club.imaginears.secondstarreborn.handlers.RankTag;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.command.CommandSource;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;
import java.util.List;

public abstract class SecondStarCommand implements SimpleCommand {
    @Getter
    private final Rank rank;
    @Getter
    private final RankTag tag;
    protected boolean tabComplete = false;
    protected boolean tabCompletePlayers = false;

    public SecondStarCommand(String name) {
        this(name, Rank.GUEST);
    }

    public SecondStarCommand(String name, Rank rank) {
        this(name, rank, null);
    }

    public SecondStarCommand(String name, Rank rank, String... aliases) {
        this(name, rank, null, aliases);
    }

    public SecondStarCommand(String name, Rank rank, RankTag tag, String... aliases) {
        this.rank = rank;
        this.tag = tag;
    }

    /**
     * Checks if the given CommandSource has permission to execute the command.
     *
     * @param source the CommandSource to check permissions for.
     * @return true if the source has permission, false otherwise.
     */
    public boolean hasPermission(CommandSource source) {
        if (!(source instanceof com.velocitypowered.api.proxy.Player player)) {
            return false;
        }
        Player pluginPlayer = SecondStar.getPlayer(player.getUniqueId());
        if (pluginPlayer == null) {
            return false;
        }
        return pluginPlayer.getRank().getRankId() >= rank.getRankId() ||
                (tag != null && pluginPlayer.getTags().contains(tag));
    }

    /**
     * Executes the command for a CommandSource.
     *
     * @param source the source of the command.
     * @param args   the command arguments.
     */
    @Override
    public void execute(CommandSource source, String[] args) {
        if (!(source instanceof com.velocitypowered.api.proxy.Player player)) {
            return;
        }
        Player pluginPlayer = SecondStar.getPlayer(player.getUniqueId());
        if (pluginPlayer == null) {
            return;
        }
        if (pluginPlayer.isDisabled() && !"staff".equalsIgnoreCase(getName())) {
            return;
        }
        if (pluginPlayer.getRank().getRankId() >= rank.getRankId() ||
                (tag != null && pluginPlayer.getTags().contains(tag))) {
            execute(pluginPlayer, args);
        } else {
            pluginPlayer.sendMessage(NamedTextColor.RED + "You do not have permission to perform this command!");
        }
    }

    /**
     * Handles tab completion for the command.
     *
     * @param source the source requesting tab completion.
     * @param args   the current command arguments.
     * @return a list of possible completions.
     */
    public List<String> suggest(CommandSource source, String[] args) {
        if (!tabComplete || !(source instanceof com.velocitypowered.api.proxy.Player player)) {
            return List.of();
        }
        Player pluginPlayer = SecondStar.getPlayer(player.getUniqueId());
        if (pluginPlayer == null) {
            return List.of();
        }
        if (pluginPlayer.getRank().getRankId() >= rank.getRankId() ||
                (tag != null && pluginPlayer.getTags().contains(tag))) {
            if (tabCompletePlayers) {
                List<String> playerNames = SecondStar.getServerUtil().getOnlinePlayerNames();
                if (args.length > 0) {
                    String lastArg = args[args.length - 1].toLowerCase();
                    return playerNames.stream()
                            .filter(name -> name.toLowerCase().startsWith(lastArg))
                            .sorted()
                            .toList(); // Java 22 API toList() for immutable result.
                } else {
                    return playerNames.stream()
                            .sorted()
                            .toList();
                }
            } else {
                return onTabComplete(pluginPlayer, Arrays.asList(args));
            }
        } else {
            return List.of();
        }
    }

    /**
     * Abstract method to execute the command for the plugin's Player object.
     *
     * @param player the target player.
     * @param args   the command arguments.
     */
    public abstract void execute(Player player, String[] args);

    /**
     * Handles tab completion for the plugin's Player object.
     *
     * @param player the target player.
     * @param args   the current command arguments.
     * @return a list of possible completions.
     */
    public List<String> onTabComplete(Player player, List<String> args) {
        return List.of();
    }
}