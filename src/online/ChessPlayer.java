package online;

import java.io.BufferedReader;
import java.io.PrintWriter;

public abstract class ChessPlayer {

    BufferedReader input;
    PrintWriter output;

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
