package club.imaginears.secondstarreborn.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;

@NoArgsConstructor
@AllArgsConstructor
public class ChatModifier {
    @Getter @Setter private ClickEvent clickEvent;
    @Getter @Setter private HoverEvent<?> hoverEvent;
    @Getter @Setter private boolean bold = false;
    @Getter @Setter private boolean italic = false;
    @Getter @Setter private boolean strikethrough = false;
    @Getter @Setter private boolean underline = false;
    @Getter @Setter private boolean random = false;
    @Getter @Setter private TextColor color = null;

    /**
     * Applies the chat modifier's style to a given TextComponent and returns the modified component.
     *
     * @param component The original TextComponent to which styles will be applied.
     * @return A new TextComponent with all styles applied from this ChatModifier.
     */
    public TextComponent apply(TextComponent component) {
        // Build the Style using the Adventure API and decorations for formatting
        Style.Builder styleBuilder = Style.style()
                .clickEvent(clickEvent)
                .hoverEvent(hoverEvent)
                .color(color);

        // Add decorations based on the ChatModifier properties
        if (bold) {
            styleBuilder.decoration(TextDecoration.BOLD, true);
        }
        if (italic) {
            styleBuilder.decoration(TextDecoration.ITALIC, true);
        }
        if (strikethrough) {
            styleBuilder.decoration(TextDecoration.STRIKETHROUGH, true);
        }
        if (underline) {
            styleBuilder.decoration(TextDecoration.UNDERLINED, true);
        }
        if (random) {
            styleBuilder.decoration(TextDecoration.OBFUSCATED, true);
        }

        // Build and apply the style to the component
        return component.style(styleBuilder.build());
    }

    /**
     * Creates a copy of the current ChatModifier instance.
     *
     * @return A deep copy of this ChatModifier.
     */
    @Override
    public ChatModifier clone() {
        return new ChatModifier(
                clickEvent,
                hoverEvent,
                bold,
                italic,
                strikethrough,
                underline,
                random,
                color
        );
    }
}