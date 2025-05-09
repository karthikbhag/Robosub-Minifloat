package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ConnectionManager {
    private final OutputDisplay outputDisplay;
    private final HttpClient client;

    public ConnectionManager(OutputDisplay outputDisplay) {
        this.outputDisplay = outputDisplay;
        this.client = HttpClient.newHttpClient();
    }

    public void makeGetConnection(String address) {
        outputDisplay.display("Sending GET request to: " + address);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(address))
                .timeout(Duration.ofSeconds(3))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        sendRequest(request);
    }

    public void makePostConnection(String address, String jsonBody) {
        outputDisplay.display("Sending POST request to: " + address + " with body: " + jsonBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(address))
                .timeout(Duration.ofSeconds(3))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        sendRequest(request);
    }

    private void sendRequest(HttpRequest request) {
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(outputDisplay::display)
                .exceptionally(e -> {
                    outputDisplay.display("Request failed: " + e.getMessage());
                    return null;
                });
    }
}