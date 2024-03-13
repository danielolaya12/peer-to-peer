import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import handlers.ConnectionHandler;
import handlers.InputHandler;

public class Peer implements Runnable {
    private String host;
    private int port;

    public Peer(String host, int port){
        this.host = host;
        this.port = port;
    }
    @Override
    public void run() {
        try {
            setupPeer();
        } catch (IOException e) {
            //If instance of ConnectException, it means there is no server running in port 9999.
            //Therefore we need to setup the Peer as a server
            if(e instanceof java.net.ConnectException){
                setupServer();
            } else {
                System.out.println(e.getMessage());
            }
        }
    }
    public void setupPeer() throws IOException {
        Socket serverSocket = new Socket(host, port);
        InputHandler inputHandler = new InputHandler(serverSocket);
        new Thread(inputHandler).start();
        BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        String serverMessage;
        while ((serverMessage = in.readLine()) != null) {
            System.out.println(serverMessage);
        }
    }
    public void setupServer(){
        try {
            System.out.println("Running as server");
            try(ServerSocket server = new ServerSocket(port);
                ExecutorService pool = Executors.newCachedThreadPool()){
                while (true) {
                    Socket clientSocket = server.accept();
                    ConnectionHandler handler = new ConnectionHandler(clientSocket);
                    pool.execute(handler);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public static void main(String[] args) {
        if(args.length < 2){
            System.out.println("IP host and port are needed. " +
                    "\nRun as java Peer <host> <port>");
            System.exit(1);
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Peer clientServer = new Peer(host, port);
        clientServer.run();
    }
}
