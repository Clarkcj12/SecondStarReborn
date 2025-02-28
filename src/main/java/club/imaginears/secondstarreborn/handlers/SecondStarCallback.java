package club.imaginears.secondstarreborn.handlers;

import com.velocitypowered.api.proxy.Player;

import java.util.UUID;

public class SecondStarCallback {
    public interface PlayerCallback{
        void run(Player player);
    }

    public interface UUIDCallback{
        void run(UUID uuid);
    }
}