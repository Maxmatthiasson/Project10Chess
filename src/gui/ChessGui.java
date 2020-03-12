package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import enums.Color;
import enums.Type;
import logic.Piece;
import online.ChessClient;
import online.ChessPlayer;
import online.ChessServer;
import logic.ChessGame;

/**
 * all x and y coordinates point to the upper left position of a component all
 * lists are treated as 0 being the bottom and size-1 being the top piece
 */

/**
 * @author Murat, Alex and Nikola
 */
public class ChessGui extends JLayeredPane implements Runnable, MouseListener, FocusListener, KeyListener {

    private static final long serialVersionUID = -8207574964820892354L;

    private Image imgBackground = new ImageIcon("img/bo.png").getImage();

    private JButton btnJoin = new JButton("Join game");
    private JButton btnHost = new JButton("Host game");
    private JButton btnCancelJoin = new JButton("X");
    private JButton btnCancelHost = new JButton("X");
    private JButton soundButton = new JButton();

    private JLabel lblGameState;
    private JLabel lblPlayerColor;
    private JLabel gameOn = new JLabel("Searching for opponent");
    private JLabel infoP;
    private JLabel startScreen;
    private JLabel lblYourIP;
    private JLabel lblTest = new JLabel("Hello");
    private LinkedList<int[]> possibleMoves = new LinkedList<>();

    Image dot = new ImageIcon("img/dot.png").getImage();


    private ImageIcon soundicon = new ImageIcon("img/sound.png");

    private JTextArea messageBoard = new JTextArea();
    private JTextField messageField;
    private JTextField userName;
    private Font messageFont = new Font("Verdana", Font.BOLD, 15);
    private Font connected = new Font("Verdana", Font.PLAIN, 10);

    private boolean mousePressed = false;
    private boolean click = false;
    private boolean sound = true;

    //	private GuiPiece guiPiece;
    private ChessPlayer player;
    private ChessGame chessGame;
    private List<GuiPiece> guiPieces = new ArrayList<>();
    private List<GuiPiece> capPieces = new ArrayList<>();

    //	private capturedPiece capturedPiece;

    private Thread thrJoin;

    public ChessGui() throws IOException {
        setLayout(null);

        // create chess game
        chessGame = new ChessGame();

        // wrap game pieces into their graphical representation
        createAndAddGuiPieces();

        // add listeners to enable drag and drop
        PiecesDragAndDropListener listener = new PiecesDragAndDropListener(guiPieces, this);
        addMouseListener(listener);
        addMouseMotionListener(listener);

        // All Gui components
        setupGameStateLabel();
        setupPlayerColorLabel();
        setupStartScreen();
        setupNewGameButton();
        setupMessageBoard();
        sendButton();
        setupMessageField();
        setupOpponentFoundLabel();
        setupInfoPanel();
        setupInfoButton();
        setupJoinButton();
        setupUserNameTextField();
        setupHostButton();
        setupCancelButtons();
        setupIpLabel();
        applicationFrame();
        soundButton();

        // Testing hover
        lblTest.setBounds(50, 50, 100, 50);
        add(lblTest, 3, 0);

    }

    public Color getColor() {
        return player.getColor();
    }

    public Color getGameState() {
        return chessGame.getGameState();
    }

    private void setupGameStateLabel() {
        String labelText = chessGame.getGameStateAsText();
        lblGameState = new JLabel(labelText);
        lblGameState.setBounds(100, 240, 200, 100);
        lblGameState.setFont(new Font("Verdana", Font.BOLD, 15));
        lblGameState.setForeground(java.awt.Color.WHITE);
        add(lblGameState);
    }

    private void setupPlayerColorLabel() {
        String labelText = "You are: ";
        lblPlayerColor = new JLabel(labelText);
        lblPlayerColor.setBounds(100, 280, 200, 100);
        lblPlayerColor.setFont(new Font("Verdana", Font.BOLD, 15));
        lblPlayerColor.setForeground(java.awt.Color.WHITE);
        add(lblPlayerColor);
    }

    private void setupNewGameButton() {
        JButton newGame = new JButton("New Game");
        newGame.setBounds(50, 350, 210, 50);
        newGame.addActionListener(e -> newGame());
        add(newGame);

    }

    private void setupMessageBoard() {
        messageBoard.setFont(messageFont);
        messageBoard.setMargin(new Insets(7, 7, 7, 7));
        messageBoard.setEditable(false);

        JScrollPane jsp = new JScrollPane();
        jsp.setSize(680, 100);
        jsp.setLocation(50, 590);
        jsp.setViewportView(messageBoard);
        add(jsp);
    }

    private void sendButton() {
        JButton send = new JButton("Send");
        send.setSize(100, 50);
        send.setLocation(630, 693);
        send.addActionListener(e -> sendMessage());
        add(send);
    }

    private void setupMessageField() {
        messageField = new JTextField();
        messageField.setBounds(50, 693, 580, 50);
        messageField.setFont(messageFont);
        messageField.addKeyListener(this);
        add(messageField);
    }

    private void setupOpponentFoundLabel() {
        gameOn.setText("Opponent found");
        gameOn.setForeground(java.awt.Color.WHITE);
        gameOn.setFont(connected);
        gameOn.setSize(200, 30);
        gameOn.setLocation(60, 15);
        add(gameOn);
    }

    private void setupInfoPanel() {
        infoP = new JLabel(new ImageIcon("img/info.png"));
        infoP.setBounds(110, 20, 560, 730);
        infoP.setVisible(false);
        add(infoP, 2, 0);
    }

    private void setupStartScreen() {
        startScreen = new JLabel(new ImageIcon("img/start2.png"));
        startScreen.setBounds(0, -110, 800, 1000);
        startScreen.setVisible(true);
        add(startScreen, 3, 0);
    }

    private void setupInfoButton() {
        JButton btnInfo = new JButton();
        btnInfo.setIcon(new ImageIcon("img/infoButton.png"));
        btnInfo.setBounds(715, 50, 56, 56);
        add(btnInfo);
        btnInfo.addActionListener(e -> showInfo());
    }


    private void soundButton() {
        soundButton.setIcon(soundicon);
        soundButton.setBounds(715, 150, 56, 56);
        add(soundButton);
        soundButton.addActionListener(e -> play());

    }


    public static void play(String filename) throws IOException {
        try {
            File soundFile = new File(filename);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    //	public void mouseEntered(MouseEvent e) {
    //		WavSound.play("sounds/blop.wav");
    //		if (e.getSource() == lblIm3) {
    //			lblImage.setIcon(image3);
    //		} else if (e.getSource() == lblIm4) {
    //			lblImage.setIcon(image4);
    //		}
    //	}


    private void setupJoinButton() {
        btnJoin.setBounds(350, 350, 240, 50);
        btnJoin.setVisible(true);
        btnJoin.setBackground(java.awt.Color.BLUE);
        btnJoin.setForeground(java.awt.Color.WHITE);
        btnJoin.setFont(new Font("Tahoma", Font.BOLD, 20));
        add(btnJoin, 3, 0);
        btnJoin.addActionListener(e -> joinGame());
    }

    private void setupHostButton() {
        btnHost.setBounds(350, 415, 240, 50);
        btnHost.setVisible(true);
        btnHost.setBackground(java.awt.Color.WHITE);
        btnHost.setForeground(java.awt.Color.DARK_GRAY);
        btnHost.setFont(new Font("Tahoma", Font.BOLD, 15));
        add(btnHost, 3, 0);
        btnHost.addActionListener(e -> hostGame());
        btnHost.addMouseListener(this);
    }

    private void setupCancelButtons() {
        btnCancelJoin.setBounds(600, 350, 50, 50);
        btnCancelJoin.setVisible(false);
        btnCancelJoin.setEnabled(false);
        btnCancelJoin.setBackground(java.awt.Color.RED);
        btnCancelJoin.addActionListener(e -> cancelJoin());
        add(btnCancelJoin, 3, 0);
        btnCancelHost.setBounds(600, 415, 50, 50);
        btnCancelHost.setVisible(false);
        btnCancelHost.setEnabled(false);
        btnCancelHost.setBackground(java.awt.Color.RED);
        btnCancelHost.addActionListener(e -> cancelHost());
        add(btnCancelHost, 3, 0);
    }

    private void setupUserNameTextField() {
        userName = new JTextField();
        userName.setBounds(350, 285, 240, 40);
        userName.setVisible(true);
        userName.setFont(messageFont);
        userName.setForeground(new java.awt.Color(204, 204, 204));
        userName.setText("Username");
        userName.addFocusListener(this);
        add(userName, 3, 0); //JTextField dissapears when changing constraint to 2.
    }

    private void setupIpLabel() throws IOException {
        String yourIP = InetAddress.getLocalHost().getHostAddress();
        lblYourIP = new JLabel("Player IP: " + yourIP);
        lblYourIP.setBounds(270, 670, 350, 100);
        lblYourIP.setFont(new Font("Verdana", Font.BOLD, 20));
        lblYourIP.setForeground(java.awt.Color.WHITE);
        add(lblYourIP, 3, 0);
    }

    private void applicationFrame() {
        JFrame f = new JFrame("OnlineChess");
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.setSize(800, 820);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
    }

    void sendMove(String move) {
        player.sendCommand(move);
        if (sound == true) {
            try {
                ChessGui.play("sounds/blop.wav");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void run() {
        String line;
        while (true) {
            line = player.getCommand();
            process(line);
        }
    }

    private void process(String command) {
        System.out.println("Process: " + command);
        if (command.equals("####")) { // New game
            resetBoard();
        } else if (command.startsWith("MESSAGE"))
            messageBoard.append(command.substring(7) + "\n");
        else if (command.startsWith("MOVE")) {
            System.out.println(command);
            String line, toks[];
            line = command.substring(4);
            toks = line.split("-");
            if (toks.length == 5) // See if promotion information is sent
                chessGame.setPromotion(Type.valueOf(toks[4]));
            setNewPieceLocationN(Integer.parseInt(toks[0]), Integer.parseInt(toks[1]), Integer.parseInt(toks[2]),
                    Integer.parseInt(toks[3]));
            repaint();
        } else if (command.startsWith("COLOR")) {
            String color = command.substring(5);
            player.setColor(Color.valueOf(color));
            lblPlayerColor.setText("You are: " + capitalizeString(player.getColor().toString()));
        }
    }

    /**
     * Capitalizes the first letter in the string and returns it.
     *
     * @param str
     * @return
     */
    private String capitalizeString(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Creates the graphical representations of the pieces
     */
    private void createAndAddGuiPieces() {
        for (Piece piece : chessGame.getPieces()) {
            GuiPiece guiPiece = new GuiPiece(piece);
            guiPieces.add(guiPiece);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        g.drawImage(imgBackground, 0, 0, null);

        //if (this.opponentFound == false) {
        //  g.setColor(java.awt.Color.RED);
        //} else {
        g.setColor(java.awt.Color.GREEN);
        //}
        g.fillOval(15, 15, 30, 30);
        for (GuiPiece guiPiece : guiPieces) {
            if (!guiPiece.isCaptured()) {
                g.drawImage(guiPiece.getImage(), guiPiece.getX(), guiPiece.getY(), null);


            } else if (guiPiece.isCaptured() && guiPiece.getAdded() == false) {
                guiPiece.setAdded(true);
                capPieces.add(guiPiece);
                if (sound == true) {
                    try {
                        ChessGui.play("sounds/crash.wav");
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }
        for (int[] move : possibleMoves)
            g.drawImage(dot, GuiHelper.convertColumnToX(move[1]) + 14, GuiHelper.convertRowToY(move[0]) + 14, null);
        paintCaptured(g);

        lblGameState.setText(chessGame.getGameStateAsText());
    }

    private void paintCaptured(Graphics g) {
        int[] count1 = {0, 40, 80, 120, 160, 200, 240, 280, 320, 360, 400, 440, 480, 520, 560, 600};
        int[] count2 = {0, 40, 80, 120, 160, 200, 240, 280, 320, 360, 400, 440, 480, 520, 560, 600};
        int place1 = 0;
        int place2 = 0;
        int counter = 0;
        for (GuiPiece guiPiece : capPieces) {
            if (counter > 15) {
                g.drawImage(guiPiece.getImage(), count2[place2], 510, null);
                place2++;
            } else {
                g.drawImage(guiPiece.getImage(), count1[place1], 470, null);
                place1++;
                counter++;
            }

        }
    }

    /**
     * change location of given piece, if the location is valid. If the location
     * is not valid, move the piece back to its original position.
     *
     * @param dragPiece
     * @param x
     * @param y
     */
    public void setNewPieceLocation(GuiPiece dragPiece, int x, int y) {
        int targetRow = GuiHelper.convertYToRow(y);
        int targetColumn = GuiHelper.convertXToColumn(x);

        if (targetRow < Piece.ROW_1 || targetRow > Piece.ROW_8 || targetColumn < Piece.COLUMN_A || targetColumn > Piece.COLUMN_H ||
                (dragPiece.getPiece().getRow() == targetRow && dragPiece.getPiece().getColumn() == targetColumn)) {
            // reset piece position if move is not valid
            dragPiece.resetToUnderlyingPiecePosition();
        } else {
            // change model and update gui piece afterwards
            System.out.println("moving piece to " + targetRow + "/" + targetColumn);
            chessGame.movePiece(dragPiece.getPiece().getRow(), dragPiece.getPiece().getColumn(), targetRow,
                    targetColumn);
            for (GuiPiece g : guiPieces)
                g.resetToUnderlyingPiecePosition();
        }
    }

    private void setNewPieceLocationN(int sourceX, int sourceY, int targetX, int targetY) {

        GuiPiece dragPiece = null;
        for (int i = guiPieces.size() - 1; i >= 0 && dragPiece == null; i--) {
            GuiPiece guiPiece = guiPieces.get(i);
            if (mouseOverPiece(guiPiece, sourceX, sourceY))
                dragPiece = guiPiece;
        }
        // move drag piece to the top of the list
        if (dragPiece != null) {
            guiPieces.remove(dragPiece);
            guiPieces.add(dragPiece);
        }

        setNewPieceLocation(dragPiece, targetX, targetY);
    }

    static boolean mouseOverPiece(GuiPiece guiPiece, int x, int y) {
        return guiPiece.getX() <= x && guiPiece.getX() + guiPiece.getWidth() >= x && guiPiece.getY() <= y
                && guiPiece.getY() + guiPiece.getHeight() >= y;
    }

    /**
     * Method for trying to join a game a server has started
     */
    private void joinGame() {
        if (userName.getText().length() > 2) {
            String ip = JOptionPane.showInputDialog(null, "Enter the IP of the server");
            if (testIp(ip) || ip.isEmpty()) {
                player = new ChessClient();
                Runnable runJoin = () -> joinRunnable(ip.isEmpty() ? "127.0.0.1" : ip); // Invokes the joinRunnable() method below
                thrJoin = new Thread(runJoin);
                thrJoin.start();
            } else
                JOptionPane.showMessageDialog(null, "The IP is not valid");
        } else
            JOptionPane.showMessageDialog(null, "Username too short, must be at least 3 characters.");
    }

    /**
     * A method invoked by a Runnable (Thread) so the search for server doesn't freeze the system
     *
     * @param ip The IP of the server
     */
    private void joinRunnable(String ip) {
        btnJoin.setEnabled(false);
        btnHost.setEnabled(false);
        btnCancelJoin.setVisible(true);
        btnCancelJoin.setEnabled(true);
        btnJoin.setText("Connecting...");
        String reply = ((ChessClient) player).connect(ip, userName.getText());
        System.out.println(reply);
        if (!reply.equals("Error")) {
            String[] replies = reply.split("-");
            repaint();
            System.out.println(ip);
            btnCancelJoin.setEnabled(false);
            btnCancelJoin.setVisible(false);
            gameOn.setText("Playing online against " + replies[0]);
            enterGame();
            player.setColor(Color.valueOf(replies[1]));
            lblPlayerColor.setText("You are: " + capitalizeString(replies[1]));
        } else {
            JOptionPane.showMessageDialog(null, "Failed to connect to server.");
            cancelJoin();
        }
    }

    /**
     * Method for cancelling the search for a server
     */
    private void cancelJoin() {
        thrJoin.interrupt();
        btnCancelJoin.setVisible(false);
        btnCancelJoin.setEnabled(false);
        btnJoin.setEnabled(true);
        btnHost.setEnabled(true);
        btnJoin.setText("Join game");
    }

    /**
     * Method for starting a server that a client can join
     */
    private void hostGame() {
        if (userName.getText().length() > 2) {
            player = new ChessServer();
            newGameColor();

            Thread thrHost = new Thread(this::hostRunnable); // Invokes the method hostRunnable() below
            thrHost.start();
        } else
            JOptionPane.showMessageDialog(null, "Username too short, must be at least 3 characters.");
    }

    /**
     * A method invoked by a Runnable (Thread) so the search for client doesn't freeze the system
     */
    private void hostRunnable() {

        btnJoin.setEnabled(false);
        btnHost.setEnabled(false);
        btnCancelHost.setVisible(true);
        btnCancelHost.setEnabled(true);
        repaint();
        btnHost.setText("Waiting for client...");
        String reply = ((ChessServer) player).waitForClient(userName.getText(), player.getColor().toString());
        if (!reply.equals("Error")) {
            gameOn.setText("Playing online against " + reply);
            enterGame();
        } else {
            btnJoin.setEnabled(true);
            btnHost.setEnabled(true);
            btnHost.setText("Host game");
            if (btnCancelHost.isEnabled())
                JOptionPane.showMessageDialog(null, "Failed to connect to client.");
        }
        btnCancelHost.setEnabled(false);
        btnCancelHost.setVisible(false);
    }

    /**
     * Method for cancelling the search for a client
     */
    private void cancelHost() {
        btnCancelHost.setEnabled(false);
        ((ChessServer) player).stopSearchForClient();
    }

    /**
     * Tests whether an IP is valid
     *
     * @param ip The IP to test
     * @return A boolean on the validity of the IP
     */
    private boolean testIp(String ip) {
        boolean res = true;
        try {
            String[] strArr = ip.split("\\.");
            int intTest;
            for (int i = 0; i < 4 && res; i++) {
                intTest = Integer.parseInt(strArr[i]);
                if (intTest < 0 || intTest > 255)
                    res = false;
            }
        } catch (Exception e) {
            res = false;
        }
        return res;
    }

    /**
     * Go from the login screen to the board
     */
    private void enterGame() {
        userName.setVisible(false);
        startScreen.setVisible(false);
        btnJoin.setEnabled(false);
        btnJoin.setVisible(false);
        btnHost.setVisible(false);
        btnHost.setEnabled(false);
        lblYourIP.setVisible(false);

        addMouseMotionListener(new ChessMoveListener());
        (new Thread(this)).start();
    }

    /**
     * Hide or show the chess info screen
     */
    private void showInfo() {
        infoP.setVisible(!click);
        click = !click;
    }

    //
    private void play() {
        if (sound) {
            soundButton.setIcon(new ImageIcon("img/mute.png"));
            sound = false;
        } else {
            soundButton.setIcon(new ImageIcon("img/sound.png"));
            sound = true;
        }

    }

    /**
     * Send message to the other player
     */
    private void sendMessage() {
        String pMessage = userName.getText() + ": " + messageField.getText();
        messageField.setText(""); //Removes text in the message field after sending a message.
        player.sendCommand("MESSAGE" + pMessage);
        messageBoard.append(pMessage + "\n");
    }

    /**
     * Asks for a new side to play on and sends the appropriate color to the opponent.
     */
    private String newGameColor() {
        String[] options = {"White", "Black"};
        int choice = JOptionPane.showOptionDialog(null, "What side do you want to play on?",
                "Color choice",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        String newColor = choice == 0 ? "WHITE" : "BLACK";
        String opponentColor = newColor == "WHITE" ? "BLACK" : "WHITE";
        player.setColor(Color.valueOf(newColor));
        lblPlayerColor.setText("You are: " + capitalizeString(newColor));
        return opponentColor;
    }

    /**
     * When clicking the button "New game", send a command to the other player and reset the board
     */
    private void newGame() {
        capPieces.clear();
        player.sendCommand("####");
        player.sendCommand("COLOR" + newGameColor());
        resetBoard();
    }

    /**
     * Clear the chat and put all pieces back to their original places
     */
    private void resetBoard() {
        messageBoard.setText(null);

        // create chess game
        chessGame = new ChessGame();

        guiPieces.clear();
        capPieces.clear();
        // wrap game pieces into their graphical representation
        createAndAddGuiPieces();
        repaint();
    }

    // If you click on the textFields the text gets removed and changes text color to black.
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    //mouseEntered and mouseExited are used for hover affect
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource().equals(btnHost) && !mousePressed) {
            btnHost.setBackground(java.awt.Color.GREEN);
            btnHost.setForeground(java.awt.Color.WHITE);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource().equals(btnHost) && !mousePressed) {
            btnHost.setBackground(java.awt.Color.WHITE);
            btnHost.setForeground(java.awt.Color.DARK_GRAY);
        }
    }

    //mousePressed and mouseReleased are used as indication on "Server Mode".
    //The color of btnHost button changes to green
    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource().equals(btnHost) && mousePressed) {
            System.out.println("btnHost button pressed");
            btnHost.setBackground(java.awt.Color.GREEN);
            btnHost.setForeground(java.awt.Color.WHITE);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource().equals(btnHost) && mousePressed) {
            btnHost.setBackground(java.awt.Color.GREEN);
            btnHost.setForeground(java.awt.Color.WHITE);
        }
    }


    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource().equals(userName) && (userName.getText().equals("Username"))) {
            userName.setText("");
            userName.setForeground(java.awt.Color.BLACK);
        }
		/*if (e.getSource().equals(opponent) && (opponent.getText().equals("IP-Address"))) {
            this.opponent.setText("");
            this.opponent.setForeground(java.awt.Color.BLACK);
        }*/
    }

    @Override
    public void focusLost(FocusEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("Enter Typed");
            sendMessage();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    //Starts the game
    public static void main(String[] args) throws IOException {
        new ChessGui();

    }

    private class ChessMoveListener implements MouseMotionListener {
        int savedRow = 0, savedCol = 0;

        @Override
        public void mouseMoved(MouseEvent e) {

            int row = GuiHelper.yToRow(e.getY());
            int col = GuiHelper.xToCol(e.getX());
            System.out.println("Row: " + row + ", Col: " + col);
            if (row > -1 && row < 8 && col > -1 && col < 8) {
                if (savedRow != row || savedCol != col) {
                    savedRow = row;
                    savedCol = col;
                    Piece p = chessGame.getNonCapturedPieceAtLocation(row, col);

                    if (p != null && p.getColor() == chessGame.getGameState() && p.getColor() == player.getColor()) {
                        LinkedList<int[]> temp = chessGame.getValidMoves(p);
                        if (temp != null)
                            possibleMoves = temp;
                            ChessGui.this.repaint();
                    } else {
                        possibleMoves.clear();
                        repaint();
                    }
                }
            } else {
                savedRow = -1;
                savedCol = -1;
                possibleMoves.clear();
                ChessGui.this.repaint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }
    }
}
