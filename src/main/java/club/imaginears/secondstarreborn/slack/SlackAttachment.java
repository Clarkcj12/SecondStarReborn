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

        /**
         * Constructs a new Field object with the specified title, value, and display configuration.
         *
         * @param title the title of the field, if null, it defaults to an empty string
         * @param value the value of the field, if null, it defaults to an empty string
         * @param isShort a boolean indicating if the field is displayed in a condensed format
         */
        public Field(String title, String value, boolean isShort) {
            this.title = Objects.requireNonNullElse(title, "");
            this.value = Objects.requireNonNullElse(value, "");
            this.isShort = isShort;
        }

        /**
         * Checks if the field is marked as "short".
         *
         * @return true if the field is marked as "short", otherwise false.
         */
        public boolean isShort() {
            return isShort;
        }
    }

    /**
     * Represents an author with optional attributes such as name, link, and icon.
     * This record provides a convenient way to encapsulate author-related information
     * for use in contexts like message attachments or other related models.
     */
    public record Author(String name, String link, String icon) {
        public Author(String name) {
            this(name, null, null);
        }

        public Author(String name, String link) {
            this(name, link, null);
        }
    }

    /**
     * Sets the author details for this Slack attachment using the provided {@code Author} record.
     * The author's name, link, and icon will be populated from the provided {@code Author} instance.
     *
     * @param author the {@code Author} record containing name, link, and icon attributes; must not be null
     * @return the current instance of {@code SlackAttachment}, allowing method chaining
     */
    public SlackAttachment author(Author author) {
        this.authorName = author.name();
        this.authorLink = author.link();
        this.authorIcon = author.icon();
        return this;
    }

    /**
     * Sets the fallback text for the Slack attachment. The fallback text is a plain-text summary
     * of the attachment content, which is displayed in environments where rich attachment rendering
     * is not supported. If the provided fallback text is null, an empty string will be used.
     *
     * @param fallbackText the fallback text to set for this Slack attachment; if null, an empty string is used
     * @return the current instance of {@code SlackAttachment}, allowing method chaining
     */
    public SlackAttachment fallback(String fallbackText) {
        this.fallback = Objects.requireNonNullElse(fallbackText, "");
        return this;
    }

    /**
     * Sets the color of the Slack attachment. The color is represented as a hexadecimal string.
     * If the provided color value is null, an empty string will be used instead.
     *
     * @param colorInHex the hexadecimal color string to set for the attachment; if null, an empty string is used
     * @return the current instance of {@code SlackAttachment}, allowing method chaining
     */
    public SlackAttachment color(String colorInHex) {
        this.color = Objects.requireNonNullElse(colorInHex, "");
        return this;
    }

    /**
     * Sets the pretext for the Slack attachment. If the provided pretext is null, an empty string will be used.
     *
     * @param pretext the pretext to set for this Slack attachment; if null, an empty string is used
     * @return the current instance of {@code SlackAttachment}, allowing method chaining
     */
    public SlackAttachment preText(String pretext) {
        this.pretext = Objects.requireNonNullElse(pretext, "");
        return this;
    }

    /**
     * Sets the title of the Slack attachment. If the provided title is null, an empty string will be used.
     *
     * @param title the title to set for this Slack attachment; if null, an empty string is used
     * @return the current instance of {@code SlackAttachment}, allowing method chaining
     */
    public SlackAttachment title(String title) {
        this.title = Objects.requireNonNullElse(title, "");
        return this;
    }

    /**
     * Sets the title and its associated hyperlink for the Slack attachment.
     * If the provided title or link is null, an empty string will be used instead.
     *
     * @param title the title to display for the attachment; if null, an empty string is used
     * @param link the hyperlink associated with the title; if null, an empty string is used
     * @return the current instance of {@code SlackAttachment}, allowing method chaining
     */
    public SlackAttachment title(String title, String link) {
        this.title = Objects.requireNonNullElse(title, "");
        this.titleLink = Objects.requireNonNullElse(link, "");
        return this;
    }

    /**
     * Sets the URL of the image to be displayed within the Slack attachment.
     * If the provided image URL is null, an empty string will be used instead.
     *
     * @param imageUrl the URL of the image to attach; if null, an empty string is used
     * @return the current instance of {@code SlackAttachment}, allowing method chaining
     */
    public SlackAttachment imageUrl(String imageUrl) {
        this.imageUrl = Objects.requireNonNullElse(imageUrl, "");
        return this;
    }

    /**
     * Sets the text content for the Slack attachment. The text represents the main body
     * of the attachment and is required.
     *
     * @param text the text content for the attachment; must not be null
     * @return the current instance of {@code SlackAttachment}, allowing method chaining
     * @throws NullPointerException if the provided text is null
     */
    public SlackAttachment text(String text) {
        this.text = Objects.requireNonNull(text, "Text cannot be null");
        return this;
    }

    /**
     * Adds a field to the SlackAttachment. Each field represents a compact, organized piece of information
     * associated with the attachment. This method ensures that the total number of fields does not exceed
     * the maximum allowable fields.
     *
     * @param field the {@code Field} object to be added to the attachment; must not be null
     * @return the current instance of {@code SlackAttachment}, allowing method chaining
     * @throws IllegalStateException if the maximum number of fields is exceeded
     */
    public SlackAttachment addField(Field field) {
        if (fields.size() >= MAX_FIELDS) {
            throw new IllegalStateException("Cannot add more than " + MAX_FIELDS + " fields.");
        }
        fields.add(field);
        return this;
    }

    /**
     * Adds a markdown element to the list of markdown formatting options for this attachment.
     *
     * @param markdownIn the markdown element to be added; must not be null
     * @return the current instance of {@code SlackAttachment}, allowing method chaining
     * @throws IllegalStateException if the maximum number of markdown elements is exceeded
     * @throws NullPointerException if the provided markdown element is null
     */
    public SlackAttachment addMarkdownIn(String markdownIn) {
        if (markdown.size() >= MAX_MARKDOWN_ELEMENTS) {
            throw new IllegalStateException("Cannot add more than " + MAX_MARKDOWN_ELEMENTS + " markdown elements.");
        }
        this.markdown.add(Objects.requireNonNull(markdownIn, "Markdown element cannot be null"));
        return this;
    }

    /**
     * Returns an unmodifiable view of the list of fields associated with this Slack attachment.
     * This ensures thread-safe, read-only access to the fields.
     *
     * @return an unmodifiable list of {@code Field} objects associated with this attachment
     */
    // Getter methods to provide read-only access to lists (Thread-safety)
    public List<Field> getFields() {
        return Collections.unmodifiableList(fields);
    }

    /**
     * Retrieves an unmodifiable list of markdown elements associated with this Slack attachment.
     * The elements specify which fields or text support markdown formatting.
     *
     * @return an unmodifiable list of markdown elements as strings
     */
    public List<String> getMarkdown() {
        return Collections.unmodifiableList(markdown);
    }
}