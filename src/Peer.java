import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import handlers.ConnectionHandler;
import handlers.InputHandler;

public class Peer implements Runnable {
    private final ArrayList<ConnectionHandler> connections;
    public Peer() {
        connections = new ArrayList<>();
    }

    @Override
    public void run() {
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
            if(e instanceof java.net.ConnectException){
                try {
                    System.out.println("Running as server");
                    ServerSocket server = new ServerSocket(9999);
                    ExecutorService pool = Executors.newCachedThreadPool();
                    while (true) {
                        Socket clientSocket = server.accept();
                        ConnectionHandler handler = new ConnectionHandler(clientSocket);
                        connections.add(handler);
                        pool.execute(handler);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //boolean isClient = args.length > 0 && args[0].equals("client");
        Peer clientServer = new Peer();
        clientServer.run();
    }
}
