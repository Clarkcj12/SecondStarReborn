package club.imaginears.secondstarreborn.slack;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SlackAttachment {
    @SerializedName("fallback")
    private String fallback;
    @SerializedName("color")
    private String color;
    @SerializedName("pretext")
    private String pretext;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("author_link")
    private String authorLink;
    @SerializedName("author_icon")
    private String authorIcon;
    @SerializedName("title")
    private String title;
    @SerializedName("title_link")
    private String titleLink;
    @SerializedName("text")
    private String text;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("fields")
    private final List<Field> fields = new ArrayList<>();
    @SerializedName("mrkdwn_in")
    private final List<String> markdown = new ArrayList<>();

    private static final int MAX_FIELDS = 10; // Slack limit on fields per attachment
    private static final int MAX_MARKDOWN_ELEMENTS = 10; // Example: Limit markdown elements

    public SlackAttachment(String text) {
        this.text = Objects.requireNonNull(text, "Text cannot be null");
    }

    // Static Field class as a standalone component (SRP compliance)
    public static final class Field {
        @Getter
        @SerializedName("title")
        private final String title;
        @Getter
        @SerializedName("value")
        private final String value;
        @SerializedName("short")
        private final boolean isShort;

        public Field(String title, String value, boolean isShort) {
            this.title = Objects.requireNonNullElse(title, "");
            this.value = Objects.requireNonNullElse(value, "");
            this.isShort = isShort;
        }

        public boolean isShort() {
            return isShort;
        }
    }

    public SlackAttachment fallback(String fallbackText) {
        this.fallback = fallbackText;
        return this;
    }

    public SlackAttachment color(String colorInHex) {
        this.color = colorInHex;
        return this;
    }

    public SlackAttachment preText(String pretext) {
        this.pretext = pretext;
        return this;
    }

    // Builder pattern for encapsulating author information
    public record Author(String name, String link, String icon) {
        public Author(String name) {
            this(name, null, null);
        }

        public Author(String name, String link) {
            this(name, link, null);
        }
    }


    public SlackAttachment author(String name, String link, String iconOrImageLink) {
        this.authorIcon = iconOrImageLink;
        return author(name, link);
    }

    public SlackAttachment title(String title) {
        this.title = title;
        return this;
    }

    public SlackAttachment title(String title, String link) {
        this.titleLink = link;
        return title(title);
    }

    public SlackAttachment imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public SlackAttachment text(String text) {
        this.text = text;
        return this;
    }

    public SlackAttachment text(SlackMessage message) {
        return text(message.toString());
    }

    public SlackAttachment addField(Field field) {
        this.fields.add(field);
        return this;
    }

    public SlackAttachment addMarkdownIn(String markdownin) {
        this.markdown.add(markdownin);
        return this;
    }

    public String getText() {
        return text;
    }
}