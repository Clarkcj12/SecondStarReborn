package club.imaginears.secondstarreborn.slack;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.Proxy;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class SlackService {

    private final HttpClient httpClient;
    private static final Gson GSON = new Gson();

    // Constructor with proxy support
    public SlackService(Proxy proxy) {
        this.httpClient = HttpClient.newBuilder()
                .proxy(HttpClient.Builder.proxy(proxy))
                .build();
    }

    // Default constructor without proxy
    public SlackService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    // Push method with attachments
    public void push(String webHookUrl, SlackMessage text, List<SlackAttachment> attachments) throws IOException, InterruptedException {
        Map<String, Object> payload = Map.of(
                "text", text.toString(),
                "attachments", attachments.isEmpty() ? null : attachments
        );
        execute(webHookUrl, payload);
    }

    // Push method without attachments
    public void push(String webHookUrl, SlackMessage text) throws IOException, InterruptedException {
        push(webHookUrl, text, List.of());
    }

    // Executes the HTTP POST request
    public void execute(String webHookUrl, Map<String, Object> payload) throws IOException, InterruptedException {
        String jsonEncodedMessage = GSON.toJson(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webHookUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonEncodedMessage))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Failed to send Slack message. HTTP Status Code: " + response.statusCode());
        }
    }
}