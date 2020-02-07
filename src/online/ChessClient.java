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
public class ChessClient {

	BufferedReader input;
	PrintWriter output;
	private Socket socket;
	
	//Listens to port 9001
	private static final int PORT = 9001;
		
	//When connected check if the opponent is free
	public boolean connect(String serverAddress) {
		try {
			socket = new Socket(serverAddress, PORT);
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
	
	//Reads message/command from opponent (server)
	public String getCommand() {
		String line;
		while(true) {
			try {
				line = input.readLine();
				if(line != null)
					return line;
			} catch (Exception e) {
				continue;
			}
		}
	}
	
	//Sends message/command to opponent (server)
	public void sendCommand(String command) {
		try {
			output.println(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}