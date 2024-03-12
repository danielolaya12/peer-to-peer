package peers;

import handlers.ConnectionHandler;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client implements Runnable {
    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean isServer;
    private Socket clientSocket;

    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
        connections = new ArrayList<>();
        isServer = false;
    }

    @Override
    public void run() {
        if (isServer) {
            // Server logic
            try {
                server = new ServerSocket(9999);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Handle client-server communication
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    // Handle incoming messages from the client
                    System.out.println("Received from client: " + inputLine);
                    // Implement your response logic here
                }

                // Close resources
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Client logic
            try {
                Socket serverSocket = new Socket("127.0.0.1", 9999);
                // Implement client logic here
                // For example, sending messages to the server
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startAsServer() {
        isServer = true;
        new Thread(this).start();
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Client client = new Client(clientSocket);
                client.startAsServer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
