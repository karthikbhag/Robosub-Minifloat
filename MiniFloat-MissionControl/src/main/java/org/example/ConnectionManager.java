package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/*
*   Manages connection to device.
*   will need to be able to do a POST request to tell the ESP to start its process.
*   Needs a GET request to get the data from the ESP after we reestablish a connection
* */
public class ConnectionManager {

    // Output display is passed in so that we can control where the output is shown. (Right now shown in GUI)
    private final OutputDisplay outputDisplay;
    public ConnectionManager(OutputDisplay outputDisplay)
    {
        this.outputDisplay = outputDisplay;
    }

    //Eventually make it where POST/GET requests can be specified. Maybe two diff methods?
    public void makeConnection(String address)
    {
        outputDisplay.display("Attempting to connect to: " + address);

        // Creates an HTTP Client. This is what is used to attempt a connection to the device
        try (HttpClient client = HttpClient.newHttpClient()){

            // Here is where you build the HTTPRequest. I believe this is called the 'Builder Pattern' if you want to look into that
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(address))
                    .timeout(Duration.ofSeconds(3))
                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofFile(Paths.get("file.json")))      // This is how Post requests are make (this one sends a file named 'file.json'
                    .GET()
                    .build(); // Every builder ends with .build() to actually build the object :)

            // Below the client attempts to send the request that we just constructed
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()) // We use sendAsync so that the GUI doesn't lock up while waiting for a request
                    .thenApply(HttpResponse::body)  //   This puts the response into the "body" of the HttpResponse class. Maybe figure out how to get status code too?
                    .thenAccept(outputDisplay::display); // This tells it to then send the output to be displayed

        }catch (Exception e)
        {
            outputDisplay.display(e.getMessage());
        }
    }
}
