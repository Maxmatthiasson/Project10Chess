package online;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Murat and Ermin
 */
public class ChessServer {

    private static final int PORT = 9001;
    private ServerSocket listener;

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

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
            socket = listener.accept();
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

    //Read message/command from client
    public String getCommand() {
        String line;
        while (true) {
            try {
                line = input.readLine();
                if (line != null)
                    return line;
            } catch (Exception e) {
                continue;
            }
        }
    }

    //Write message/command to socket port which the client is listening to
    public void sendCommand(String command) {
        if (output != null)
            try {
                output.println(command);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}