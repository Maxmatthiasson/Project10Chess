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
    private int time;
    //Creates a server on this computer

    public ChessServer(int time) {
        super(Color.WHITE);
        this.time = time;
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
    public String waitForClient(String userName, String color) {
        try {
            System.out.println("Waiting for client to connect");
            Socket socket = listener.accept();
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                String opponentColor = color.equals("WHITE") ? "BLACK" : "WHITE";
                output.println("CONNECTED-" + userName + "-" + opponentColor + "-" + time);  // Skicka tiden här
                String line = input.readLine();
                if (line.startsWith("ACK")) {
                    System.out.println("Server: connected");
                    return line.split("-")[1];
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to connect to client");
            return "Error";
        }
    }


}