package club.imaginears.secondstarreborn.slack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlackMessage {
    private final StringBuilder messageBuilder = new StringBuilder();

    public SlackMessage() { }

    public SlackMessage(String text) {
        this.text(text);
    }

    public SlackMessage text(String text) {
        messageBuilder.append(text);
        return this;
    }

    public SlackMessage link(String url) {
        return link(url, "");
    }

    public SlackMessage link(String url, String text) {
        appendLink(url, text);
        return this;
    }

    public SlackMessage bold(String text) {
        messageBuilder.append("*").append(text).append("*");
        return this;
    }

    public SlackMessage italic(String text) {
        messageBuilder.append("_").append(text).append("_");
        return this;
    }

    public SlackMessage code(String text) {
        messageBuilder.append('`').append(text).append('`');
        return this;
    }

    public SlackMessage preformatted(String text) {
        messageBuilder.append("```").append(text).append("```");
        return this;
    }

    public SlackMessage quote(String text) {
        messageBuilder.append("\n> ").append(text).append("\n");
        return this;
    }

    @Override
    public String toString() {
        return messageBuilder.toString();
    }

    public String rawText() {
        String rawText = messageBuilder.toString();

        // Using Pattern Matching for Regular Expressions (Java 22 Improvement)
        var patterns = new String[]{
                "\\*(.*?)\\*",       // Bold formatting
                "_(.*?)_",           // Italic formatting
                "```(.*?)```",       // Preformatted text
                "`(.*?)`"            // Inline code
        };

        for (String regex : patterns) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(rawText);
            rawText = matcher.replaceAll(result -> result.group(1));
        }

        Pattern quotePattern = Pattern.compile("\n>\\s+(.*)\n");
        Matcher quoteMatcher = quotePattern.matcher(rawText);
        return quoteMatcher.replaceAll(result -> result.group(1));
    }

    // Private helper to handle common link formatting logic
    private void appendLink(String url, String text) {
        messageBuilder.append(text.isEmpty()
                ? String.format("<%s>", url)
                : String.format("<%s|%s>", url, text));
    }
}