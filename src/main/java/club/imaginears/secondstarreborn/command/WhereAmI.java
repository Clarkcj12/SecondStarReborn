package club.imaginears.secondstarreborn.command;

import club.imaginears.secondstarreborn.handlers.moderation.SecondStarCommand;
import club.imaginears.secondstarreborn.handlers.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class WhereAmI extends SecondStarCommand {
    public WhereAmI() {
        super("whereami");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (player == null || player.getServerName() == null) {
            return; // Handle null case
        }

        String serverName = player.getServerName();

        Object proxiedPlayer = player.getProxiedPlayer();
        if (proxiedPlayer instanceof com.velocitypowered.api.proxy.Player velocityPlayer) {
            velocityPlayer.sendMessage(
                    Component.text("You are on the server ")
                            .color(NamedTextColor.BLUE)
                            .append(Component.text(serverName, NamedTextColor.GOLD))
            );
        } else {
            player.sendMessage(
                    Component.text("You are on the server ")
                            .color(NamedTextColor.BLUE)
                            .append(Component.text(serverName, NamedTextColor.GOLD))
            );
        }
    }
}