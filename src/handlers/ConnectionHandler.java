package handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private final Socket client;
    public ConnectionHandler(Socket client) {
        this.client = client;
    }
    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String clientMessage;
            out.println("Enter a name: ");
            String name = in.readLine();
            System.out.println(name + " is connected");
            while ((clientMessage = in.readLine()) != null) {
                System.out.println(name + " : " + clientMessage);
                if(clientMessage.equals("exit")) break;
            }
            out.println(name + " exited the chat");
            in.close();
            client.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
