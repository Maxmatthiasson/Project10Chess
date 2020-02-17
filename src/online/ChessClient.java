package online;

import enums.Color;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 
 * @author Murat and Ermin
 *
 */
public class ChessClient extends ChessPlayer {

    public ChessClient() {
        super(Color.BLACK);
    }

	//When connected check if the opponent is free
	public String connect(String serverAddress, String userName) {
		try {
			Socket socket = new Socket(serverAddress, PORT);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
			String line = input.readLine();
			if(line.startsWith("CONNECTED"))
				output.println("ACK-" + userName);
			System.out.println("Client: connected");
			return line.substring(line.indexOf("-") + 1);
		} catch (Exception e) {
			return "Error";
		}
	}
}