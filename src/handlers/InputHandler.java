package handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class InputHandler implements Runnable {
    private final Socket serverSocket;
    public InputHandler(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
            BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while ((userInput = consoleIn.readLine()) != null) {
                System.out.print("Message: ");
                out.println(userInput);
                if (userInput.equals("exit")) break;
            }
            System.out.println("Exiting chat");
            out.close();
            consoleIn.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
