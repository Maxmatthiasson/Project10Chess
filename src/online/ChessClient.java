package online;

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
	
	//Listens to port 9001
	private static final int PORT = 9001;
		
	//When connected check if the opponent is free
	public boolean connect(String serverAddress) {
		try {
			Socket socket = new Socket(serverAddress, PORT);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
			String line = input.readLine();
			if(line.equals("CONNECTED"))
				output.println("ACK");
			System.out.println("Client: connected");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}