package club.imaginears.secondstarreborn.handlers;

import net.kyori.adventure.text.format.NamedTextColor;

public enum Subsystem {
    PARTY("Party", NamedTextColor.BLUE);

    private final String name;
    private final NamedTextColor color;

    Subsystem(String name, NamedTextColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public NamedTextColor getColor() {
        return color;
    }

    public String getPrefix() {
        return STR."\{color.toString()}[\{name}] ";
    }

    public String getComponentPrefix() {
        return getPrefix();
    }
}