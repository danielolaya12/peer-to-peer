// handlers/ConnectionHandler.java
package handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private Socket client;

    public ConnectionHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Received from client: " + clientMessage);
                // Handle incoming messages from the client
            }
            // Close resources
            in.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
