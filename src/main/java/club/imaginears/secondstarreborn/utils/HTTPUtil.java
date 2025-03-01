package club.imaginears.secondstarreborn.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class HTTPUtil {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    /**
     * Reads the content from the given URL and returns it as a String.
     *
     * @param urlString the URL to fetch data from
     * @return the response content as a String
     * @throws Exception if an error occurs during the request
     */
    public static Optional<String> readUrl(String urlString) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .GET()
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200
                ? Optional.of(response.body())
                : Optional.empty();
    }
}