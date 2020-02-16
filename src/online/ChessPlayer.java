package online;

import enums.Color;

import java.io.BufferedReader;
import java.io.PrintWriter;

public abstract class ChessPlayer {

    //Listens to port 9001
    static final int PORT = 9001;
    BufferedReader input;
    PrintWriter output;
    Color color;

    ChessPlayer(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
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
