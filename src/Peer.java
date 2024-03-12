import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import handlers.ConnectionHandler;
import handlers.InputHandler;

public class Peer implements Runnable {
    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean isClient;
    private ExecutorService pool;

    public Peer(boolean isClient) {
        connections = new ArrayList<>();
        this.isClient = isClient;
    }

    @Override
    public void run() {
        if (isClient) {
            try {
                Socket serverSocket = new Socket("127.0.0.1", 9999);
                InputHandler inputHandler = new InputHandler(serverSocket);
                new Thread(inputHandler).start();
                BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    System.out.println(serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Server logic
            try {
                System.out.println("Running as server");
                server = new ServerSocket(9999);
                pool = Executors.newCachedThreadPool();
                while (true) {
                    Socket clientSocket = server.accept();
                    ConnectionHandler handler = new ConnectionHandler(clientSocket);
                    connections.add(handler);
                    pool.execute(handler);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        boolean isClient = args.length > 0 && args[0].equals("client");
        Peer clientServer = new Peer(isClient);
        clientServer.run();
    }
}
