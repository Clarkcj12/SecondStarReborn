package club.imaginears.secondstarreborn.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

@AllArgsConstructor
public enum Rank {
    OWNER("Owner", NamedTextColor.RED + "Owner ", NamedTextColor.RED, NamedTextColor.YELLOW, true, 13),
    EXEC("Executive", NamedTextColor.RED + "Director ", NamedTextColor.RED, NamedTextColor.YELLOW, true, 13),
    MANAGER("Manager", NamedTextColor.GOLD + "Manager ", NamedTextColor.GOLD, NamedTextColor.YELLOW, true, 13),
    LEAD("Lead", NamedTextColor.GREEN + "Lead ", NamedTextColor.DARK_GREEN, NamedTextColor.GREEN, true, 13),
    RETIRED("Retired Owner", NamedTextColor.DARK_PURPLE + "Retired Owner ", NamedTextColor.DARK_PURPLE, NamedTextColor.DARK_PURPLE, true, 13),
    DEVELOPER("Developer", NamedTextColor.BLUE + "Developer ", NamedTextColor.BLUE, NamedTextColor.AQUA, true, 13),
    COORDINATOR("Coordinator", NamedTextColor.BLUE + "Coordinator ", NamedTextColor.BLUE, NamedTextColor.AQUA, true, 12),
    BUILDER("Imagineer", NamedTextColor.AQUA + "Imagineer ", NamedTextColor.AQUA, NamedTextColor.AQUA, true, 11),
    IMAGINEER("Imagineer", NamedTextColor.AQUA + "Imagineer ", NamedTextColor.AQUA, NamedTextColor.AQUA, true, 11),
    MEDIA("Cast Member", NamedTextColor.AQUA + "CM ", NamedTextColor.AQUA, NamedTextColor.AQUA, true, 11),
    CM("Cast Member", NamedTextColor.AQUA + "CM ", NamedTextColor.AQUA, NamedTextColor.AQUA, true, 11),
    TRAINEETECH("Trainee", NamedTextColor.AQUA + "Trainee ", NamedTextColor.AQUA, NamedTextColor.AQUA, false, 10),
    TRAINEEBUILD("Trainee", NamedTextColor.AQUA + "Trainee ", NamedTextColor.AQUA, NamedTextColor.AQUA, false, 10),
    TRAINEE("Trainee", NamedTextColor.AQUA + "Trainee ", NamedTextColor.AQUA, NamedTextColor.AQUA, false, 9),
    CHARACTER("Character", NamedTextColor.DARK_PURPLE + "Character ", NamedTextColor.DARK_PURPLE, NamedTextColor.DARK_PURPLE, false, 8),
    INFLUENCER("Influencer", NamedTextColor.DARK_PURPLE + "Influencer ", NamedTextColor.DARK_PURPLE, NamedTextColor.WHITE, false, 7),
    VIP("VIP", NamedTextColor.DARK_PURPLE + "VIP ", NamedTextColor.DARK_PURPLE, NamedTextColor.WHITE, false, 7),
    SHAREHOLDER("Shareholder", NamedTextColor.LIGHT_PURPLE + "Shareholder ", NamedTextColor.LIGHT_PURPLE, NamedTextColor.WHITE, false, 6),
    CLUB("Club 33", NamedTextColor.DARK_RED + "C33 ", NamedTextColor.DARK_RED, NamedTextColor.WHITE, false, 5),
    DVC("DVC", NamedTextColor.GOLD + "DVC ", NamedTextColor.GOLD, NamedTextColor.WHITE, false, 4),
    PASSPORT("Premier Passport", NamedTextColor.YELLOW + "Premier ", NamedTextColor.YELLOW, NamedTextColor.WHITE, false, 3),
    PASSHOLDER("Passholder", NamedTextColor.DARK_AQUA + "Passholder ", NamedTextColor.DARK_AQUA, NamedTextColor.WHITE, false, 2),
    GUEST("Guest", NamedTextColor.GRAY + "", NamedTextColor.GRAY, NamedTextColor.GRAY, false, 1);

    @Getter
    private final String name;
    @Getter
    private final String scoreboardName;
    @Getter
    private final NamedTextColor tagColor;
    @Getter
    private final NamedTextColor chatColor;
    @Getter
    private final boolean isOp;
    @Getter
    private final int rankId;

    /**
     * Get rank object from a string
     *
     * @param name rank name in string
     * @return rank object
     */
    public static Rank fromString(String name) {
        if (name == null) return GUEST;
        if (name.equalsIgnoreCase("admin")) return LEAD;
        String rankName = name.replaceAll(" ", "");

        for (Rank rank : Rank.values()) {
            if (rank.getDBName().equalsIgnoreCase(rankName)) return rank;
        }
        return GUEST;
    }

    public String getDBName() {
        String s;
        switch (this) {
            case TRAINEEBUILD:
            case TRAINEETECH:
                s = name().toLowerCase();
                break;
            case MEDIA:
                s = "media";
                break;
            case BUILDER:
                s = "builder";
                break;
            default:
                s = name.toLowerCase().replaceAll(" ", "");
        }
        return s;
    }

    /**
     * Get the formatted name of a rank
     *
     * @return the rank name with any additional formatting that should exist
     */
    public String getFormattedName() {
        return getTagColor() + getName();
    }
}