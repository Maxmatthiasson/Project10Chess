package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

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

    private JLabel lblGameState;
    private JLabel gameOn = new JLabel("Searching for opponent");
    private JLabel infoP;
    private JLabel startScreen;
    private JLabel lblYourIP;

    private JTextArea messageBoard = new JTextArea();
    private JTextField messageField;
    private JTextField userName;
    private Font messageFont = new Font("Verdana", Font.BOLD, 15);
    private Font connected = new Font("Verdana", Font.PLAIN, 10);
    private String user;

    private boolean mousePressed = false;
    private boolean click = false;

    private ChessPlayer player;
    private ChessGame chessGame;
    private List<GuiPiece> guiPieces = new ArrayList<>();

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
        jsp.setSize(680, 200);
        jsp.setLocation(50, 470);
        jsp.setViewportView(messageBoard);
        add(jsp);
    }

    private void sendButton() {
        JButton send = new JButton("Send");
        send.setSize(100, 50);
        send.setLocation(630, 690);
        send.addActionListener(e -> sendMessage());
        add(send);
    }

    private void setupMessageField() {
        messageField = new JTextField();
        messageField.setBounds(50, 690, 580, 50);
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
    }

    public void run() {
        String line;
        gameOn.setText("Opponent found ");
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
        }
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
            }
        }

        lblGameState.setText(chessGame.getGameStateAsText());
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
        if (userName.getText().length() > 3) {
            String ip = JOptionPane.showInputDialog(null, "Enter the IP of the server");
            if (testIp(ip)) {
                player = new ChessClient();
                Runnable runJoin = () -> joinRunnable(ip); // Invokes the joinRunnable() method below
                thrJoin = new Thread(runJoin);
                thrJoin.start();
            } else
                JOptionPane.showMessageDialog(null, "The IP is not valid");
        } else
            JOptionPane.showMessageDialog(null, "Username too short, must be at least 3 characters.");
    }

    /**
     * A method invoked by a Runnable (Thread) so the search for server doesn't freeze the system
     * @param ip The IP of the server
     */
    private void joinRunnable(String ip) {
        btnJoin.setEnabled(false);
        btnHost.setEnabled(false);
        btnCancelJoin.setVisible(true);
        btnCancelJoin.setEnabled(true);
        btnJoin.setText("Connecting...");
        if (((ChessClient) player).connect(ip)) {
            repaint();
            System.out.println(ip);
            btnCancelJoin.setEnabled(false);
            btnCancelJoin.setVisible(false);
            enterGame();
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
        if (userName.getText().length() > 3) {
            player = new ChessServer();
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
        if (((ChessServer) player).waitForClient()) {
            btnCancelHost.setEnabled(false);
            btnCancelHost.setVisible(false);
            enterGame();
        } else {
            btnJoin.setEnabled(true);
            btnHost.setEnabled(true);
            btnHost.setText("Host game");
            if (btnCancelHost.isEnabled())
                JOptionPane.showMessageDialog(null, "Failed to connect to client.");
            btnCancelHost.setVisible(false);
            btnCancelHost.setEnabled(false);
        }
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
        user = userName.getText();
        startScreen.setVisible(false);
        btnJoin.setEnabled(false);
        btnJoin.setVisible(false);
        btnHost.setVisible(false);
        btnHost.setEnabled(false);
        lblYourIP.setVisible(false);

        (new Thread(this)).start();
    }

    /**
     * Hide or show the chess info screen
     */
    private void showInfo() {
        infoP.setVisible(!click);
        click = !click;
    }

    /**
     * Send message to the other player
     */
    private void sendMessage() {
        String pMessage = user + ": " + messageField.getText();
        messageField.setText(""); //Removes text in the message field after sending a message.
        player.sendCommand("MESSAGE" + pMessage);
        messageBoard.append(pMessage + "\n");
    }

    /**
     * When clicking the button "New game", send a command to the other player and reset the board
     */
    private void newGame() {
        player.sendCommand("####");
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
        // wrap game pieces into their graphical representation
        createAndAddGuiPieces();
        repaint();
    }

    // If you click on the textFields the text gets removed and changes text color to black.
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
//        if (e.getSource().equals(setupUserNameTextField) && (setupUserNameTextField.getText().equals("Username"))) {
//            this.setupUserNameTextField.setText("");
//            this.setupUserNameTextField.setForeground(java.awt.Color.BLACK);
//        }
//        if (e.getSource().equals(opponent) && (opponent.getText().equals("IP-Address"))) {
//            this.opponent.setText("");
//            this.opponent.setForeground(java.awt.Color.BLACK);
//        }
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

    //Starts the game
    public static void main(String[] args) throws IOException {
        new ChessGui();
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
}