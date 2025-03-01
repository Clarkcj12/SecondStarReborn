package club.imaginears.secondstarreborn.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for processing links and color codes in strings and converting them into
 * Kyori Adventure's {@link Component}.
 */
public class LinkUtil {

    // Regex for detecting links in text and optional newlines
    private static final Pattern LINK_PATTERN = Pattern.compile("((?:(?:https?)://)?(?:[-\\w_.]{2,}\\.[a-z]{2,4}.*?(?=\\s|$)))", Pattern.CASE_INSENSITIVE);

    /**
     * Converts a raw string into an Adventure {@link Component}, formatting links as clickable and hoverable elements.
     *
     * @param message The input raw string.
     * @return The resulting {@link Component} with links processed and text decorations applied.
     */
    public static Component fromString(String message) {
        if (message == null || message.isBlank()) {
            return Component.empty();
        }

        List<Component> components = new ArrayList<>();
        Matcher matcher = LINK_PATTERN.matcher(message);

        int lastEnd = 0;
        while (matcher.find()) {
            // Extracting plain text before the link
            if (matcher.start() > lastEnd) {
                components.add(Component.text(message.substring(lastEnd, matcher.start())));
            }

            // Extracting the clickable link
            String detectedLink = matcher.group();
            components.add(createClickableLinkComponent(detectedLink));
            lastEnd = matcher.end();
        }

        // Adding any remaining plain text after the last link
        if (lastEnd < message.length()) {
            components.add(Component.text(message.substring(lastEnd)));
        }

        // Combine the list of components into a single Component
        return Component.join(Component.empty(), components);
    }

    /**
     * Creates a clickable, hoverable {@link Component} from a given link.
     *
     * @param link The URL or text of the link.
     * @return A {@link Component} for the clickable link.
     */
    private static Component createClickableLinkComponent(String link) {
        // Ensure we add "http://" if it is not already a valid URL
        String validUrl = link.startsWith("http://") || link.startsWith("https://") ? link : "http://" + link;

        return Component.text(link)
                .decorate(TextDecoration.UNDERLINED)
                .clickEvent(ClickEvent.openUrl(validUrl))
                .hoverEvent(HoverEvent.showText(Component.text("Click to open: " + validUrl)));
    }

    /**
     * Converts an Adventure {@link Component} into plain text. This acts similar to `toString()`,
     * stripping out any Adventure Component formatting.
     *
     * @param component The {@link Component} to convert.
     * @return The plain-text representation of the component.
     */
    public static String fromComponent(Component component) {
        if (component == null) {
            return "";
        }

        // Strip out any hover/click events or formatting
        return component.toString();
    }

    /**
     * Parses a string while preserving basic formatting and applying clickable links and hover effects where necessary.
     *
     * @param message The input string to parse.
     * @return a {@link Component} with the applied formatting and links processed.
     */
    public static Component parseFormattedString(String message) {
        // Parse and process links
        return fromString(Optional.ofNullable(message).orElse(""));
    }
}