package handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionHandler implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private String nickname;
    private ArrayList<ConnectionHandler> connections;
    public ConnectionHandler(Socket client, ArrayList<ConnectionHandler> connections){
        this.client = client;
        this.connections = connections;
    }
    @Override
    public void run() {
        try {
            //Auto stream is set to flush so we don't have to manually flush the stream to send messages.
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out.println("Please enter a nickname: ");
            nickname = in.readLine();
            System.out.println(nickname + "connected");
            broadcast(nickname + " joined the chat!");
            String message;
            while ((message = in.readLine()) != null){
                if(message.startsWith(("/nick "))){
                    String[] messageSplit = message.split(" ", 2);
                    if(messageSplit.length ==2){
                        broadcast(nickname + "renamed themselves to " + messageSplit[1]);
                        System.out.println(nickname + "renamed themselves to " + messageSplit[1]);
                        nickname = messageSplit[1];
                        out.println("Succesfully changed nickname to " + nickname);
                    } else {
                        out.println("No nickname provided");
                    }
                } else if (message.startsWith("/quit")){
                    broadcast(nickname + " left the chat");
                    shutdown();
                } else {
                    broadcast(nickname + ": " + message);
                }
            }
        } catch (IOException e){
            shutdown();
        }
    }
    public void sendMessage(String message){
        out.println(message);
    }

    public void shutdown(){
        try{
            in.close();
            out.close();
            if(!client.isClosed()){
                client.close();
            }
        } catch(IOException e){
            //ignore exception
        }
    }
    public void broadcast(String message) {
        for (ConnectionHandler ch: connections){
            if(ch != null){
                ch.sendMessage(message);
            }
        }
    }
}