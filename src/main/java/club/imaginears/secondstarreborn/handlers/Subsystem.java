package club.imaginears.secondstarreborn.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
@AllArgsConstructor
public enum Subsystem {
    PART("Party", NamedTextColor.BLUE);
    String name;
    NamedTextColor color;

    public String getPrefix(){
        return color + "[" + name + "] ";
    }

    public String getComponentPrefix(){
        return color + "[" + name + "] ";
    }
}