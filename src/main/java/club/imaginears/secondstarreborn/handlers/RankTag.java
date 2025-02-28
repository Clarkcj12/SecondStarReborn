package club.imaginears.secondstarreborn.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

@AllArgsConstructor
public enum RankTag {
    /* Dev Team (best team) */
    JRDEV("Jr. Developer Program", "J", NamedTextColor.DARK_RED, 9),
    /* Media Team */
    DESIGNER("Resource Pack Designer", "D", NamedTextColor.BLUE, 8),
    /* Guide Program */
    GUIDE("Guide Team", "G", NamedTextColor.DARK_GREEN, 7),
    /* Sponsor Tiers */
    SPONSOR_OBSIDIAN("Obsidian Tier Sponsor", "S", NamedTextColor.DARK_PURPLE, 6),
    SPONSOR_EMERALD("Emerald Tier Sponsor", "S", NamedTextColor.GREEN, 5),
    SPONSOR_DIAMOND("Diamond Tier Sponsor", "S", NamedTextColor.AQUA, 4),
    SPONSOR_LAPIS("Lapis Tier Sponsor", "S", NamedTextColor.BLUE, 3),
    SPONSOR_GOLD("Gold Tier Sponsor", "S", NamedTextColor.YELLOW, 2),
    SPONSOR_IRON("Iron Tier Sponsor", "S", NamedTextColor.GRAY, 1),
    CREATOR("Creator", "C", NamedTextColor.BLUE, 0);

    @Getter
    private String name;
    private String tag;
    @Getter private NamedTextColor color;
    @Getter private int id;

    public String getTag() {
        return NamedTextColor.WHITE + "[" + color + TextDecoration.BOLD + tag + NamedTextColor.WHITE + "] ";
    }

    /**
     * Get tag object from a string
     *
     * @param name tag name in string
     * @return tag object
     */
    public static RankTag fromString(String name) {
        if (name == null || name.isEmpty()) return null;

        for (RankTag tier : RankTag.values()) {
            if (tier.getDBName().equalsIgnoreCase(name)) return tier;
        }
        return null;
    }

    public String getDBName() {
        return name().toLowerCase();
    }

    public String getScoreboardTag() {
        return " " + NamedTextColor.WHITE + "[" + color + "S" + NamedTextColor.WHITE + "]";
    }

    public static String format(List<RankTag> tags) {
        tags.sort((rankTag, t1) -> t1.id - rankTag.id);
        StringBuilder s = new StringBuilder();
        for (RankTag tag : tags) {
            s.append(tag.getTag());
        }
        return s.toString();
    }
}