package online;

import enums.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Murat and Ermin
 */
public class ChessServer extends ChessPlayer {

    private ServerSocket listener;

    //Creates a server on this computer
    public ChessServer() {
        super(Color.WHITE);
        try {
            listener = new ServerSocket(PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopSearchForClient() {
        try {
            listener.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Waiting for client to connect
    public boolean waitForClient() {
        try {
            System.out.println("Waiting for cilent to connect");
            Socket socket = listener.accept();
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                output.println("CONNECTED");
                if (input.readLine().equalsIgnoreCase("ACK")) {
                    System.out.println("Server: connected");
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to connect to client");
            return false;
        }
    }
}