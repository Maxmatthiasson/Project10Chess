package online;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Murat and Ermin
 */
public class ChessServer extends ChessPlayer {

    private static final int PORT = 9001;
    private ServerSocket listener;

    //Creates a server on this computer
    public ChessServer() {
        try {
            listener = new ServerSocket(PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Waiting for client to connect
    public void waitForClient() {
        try {
            System.out.println("Waiting for cilent to connect");
            Socket socket = listener.accept();
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                output.println("CONNECTED");
                if (input.readLine().equalsIgnoreCase("ACK"))
                    break;
            }
            System.out.println("Server: connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}