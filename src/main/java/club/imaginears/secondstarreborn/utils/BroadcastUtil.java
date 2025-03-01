package club.imaginears.secondstarreborn.utils;

import club.imaginears.secondstarreborn.SecondStar;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BroadcastUtil {
    public BroadCastUtil(){
        // TODO need to find way to distribute this system in the future
        SecondStar.getProxyServer().getScheduler().schedule(SecondStar.getInstance(), new Runnable() {
            int i = 0;

            @Override
            public void run() {
                List<String> announcements = SecondStar.getConfigUtil().getAnnouncements();
                if (announcements.isEmpty()) return;
                if (i >= announcements.size()) i = 0;

                String message = NamedTextColor.WHITE + "[" + NamedTextColor.BLUE + "✦" + NamedTextColor.WHITE + "] " + announcements.get(i++);

                for (Player tp : SecondStar.getOnlinePlayers()) {
                    tp.sendMessage(message);
                }
            }
        }, 1, 5, TimeUnit.MINUTES);
    }
}