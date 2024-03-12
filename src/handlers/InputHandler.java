// handlers/InputHandler.java
package handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class InputHandler implements Runnable {
    private Socket serverSocket;

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
                out.println(userInput);
            }
            // Close resources
            out.close();
            consoleIn.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
