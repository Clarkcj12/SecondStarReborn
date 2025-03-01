package club.imaginears.secondstarreborn.slack;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
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
    @Getter
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

    // Builder pattern for encapsulating author information
    public record Author(String name, String link, String icon) {
        public Author(String name) {
            this(name, null, null);
        }

        public Author(String name, String link) {
            this(name, link, null);
        }
    }

    // Author methods refactored to use Author record
    public SlackAttachment author(Author author) {
        this.authorName = author.name();
        this.authorLink = author.link();
        this.authorIcon = author.icon();
        return this;
    }

    public SlackAttachment fallback(String fallbackText) {
        this.fallback = Objects.requireNonNullElse(fallbackText, "");
        return this;
    }

    public SlackAttachment color(String colorInHex) {
        this.color = Objects.requireNonNullElse(colorInHex, "");
        return this;
    }

    public SlackAttachment preText(String pretext) {
        this.pretext = Objects.requireNonNullElse(pretext, "");
        return this;
    }

    public SlackAttachment title(String title) {
        this.title = Objects.requireNonNullElse(title, "");
        return this;
    }

    public SlackAttachment title(String title, String link) {
        this.title = Objects.requireNonNullElse(title, "");
        this.titleLink = Objects.requireNonNullElse(link, "");
        return this;
    }

    public SlackAttachment imageUrl(String imageUrl) {
        this.imageUrl = Objects.requireNonNullElse(imageUrl, "");
        return this;
    }

    public SlackAttachment text(String text) {
        this.text = Objects.requireNonNull(text, "Text cannot be null");
        return this;
    }

    public SlackAttachment addField(Field field) {
        if (fields.size() >= MAX_FIELDS) {
            throw new IllegalStateException("Cannot add more than " + MAX_FIELDS + " fields.");
        }
        fields.add(field);
        return this;
    }

    public SlackAttachment addMarkdownIn(String markdownIn) {
        if (markdown.size() >= MAX_MARKDOWN_ELEMENTS) {
            throw new IllegalStateException("Cannot add more than " + MAX_MARKDOWN_ELEMENTS + " markdown elements.");
        }
        this.markdown.add(Objects.requireNonNull(markdownIn, "Markdown element cannot be null"));
        return this;
    }

    // Getter methods to provide read-only access to lists (Thread-safety)
    public List<Field> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public List<String> getMarkdown() {
        return Collections.unmodifiableList(markdown);
    }
}