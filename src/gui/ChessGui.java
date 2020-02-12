package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import enums.Color;
import online.ChessClient;
import online.ChessServer;
import logic.ChessGame;
import logic.PiecesDragAndDropListener;

/**
 * all x and y coordinates point to the upper left position of a component all
 * lists are treated as 0 being the bottom and size-1 being the top piece
 */

/**
 * @author Murat, Alex and Nikola
 */
public class ChessGui extends JLayeredPane implements Runnable, ActionListener, MouseListener {

    private static final long serialVersionUID = -8207574964820892354L;

    private static final int BOARD_START_X = 301;
    private static final int BOARD_START_Y = 51;

    private static final int SQUARE_WIDTH = 50;
    private static final int SQUARE_HEIGHT = 50;

    private static final int PIECE_WIDTH = 48;
    private static final int PIECE_HEIGHT = 48;

    private static final int PIECES_START_X = BOARD_START_X + (int) (SQUARE_WIDTH / 2.0 - PIECE_WIDTH / 2.0);
    private static final int PIECES_START_Y = BOARD_START_Y + (int) (SQUARE_HEIGHT / 2.0 - PIECE_HEIGHT / 2.0);

    private static final int DRAG_TARGET_SQUARE_START_X = BOARD_START_X - (int) (PIECE_WIDTH / 2.0);
    private static final int DRAG_TARGET_SQUARE_START_Y = BOARD_START_Y - (int) (PIECE_HEIGHT / 2.0);

    private Image imgBackground;

    private JButton newGame;
    private JButton infoB;
    private JButton send;
    private JButton logIn;
    private JButton acceptCon;

    private JLabel lblGameState;
    private JLabel gameOn = new JLabel("Searching for opponent");
    private JLabel infoP;
    private JLabel startScreen;
    private JLabel lblYourIP;

    private JTextArea messageBoard;
    private JTextField messageField;
    private JTextField userName;
    private JTextField opponent;
    private Font messageFont = new Font("Verdana", Font.BOLD, 15);
    private Font conected = new Font("Verdana", Font.PLAIN, 10);
    private java.awt.Color color;

    private String user;
    private String yourIP;

    private boolean gameOver = false;
    private boolean mousePressed = false;
    private boolean serverMode = false;
    private boolean opponentFound = false, isClient = true, connected = false, th;
    private boolean click = false;

    private ChessClient gc;
    private ChessServer gs;
    private Thread networkThread;

    private ChessGame chessGame;
    private List<GuiPiece> guiPieces = new ArrayList<>();
    private PiecesDragAndDropListener listener;

    private Color myColor;

    public ChessGui() throws IOException {
        this.setLayout(null);

        // background
        this.imgBackground = new ImageIcon("img/bo.png").getImage();

        // create chess game
        this.chessGame = new ChessGame();

        // wrap game pieces into their graphical representation
        for (Piece piece : this.chessGame.getPieces()) {
            createAndAddGuiPiece(piece);
        }

        // add listeners to enable drag and drop
        //
        listener = new PiecesDragAndDropListener(this.guiPieces, this);

        // label to display game state
        this.gameState();
        // All Gui components
        this.startScreen();
        this.newGame();
        this.messageBoard();
        this.sendButton();
        this.messageField();
        this.gameON();
        this.infoPanel();
        this.infoButton();
        this.logInButton();
        this.userName();
        this.opponent();
        this.acceptConnect();
        this.yourIP();
        this.applicationFrame();
    }

    public Color getColor() {
        return this.myColor;
    }

    public void gameState() {
        String labelText = this.getGameStateAsText();
        this.lblGameState = new JLabel(labelText);
        lblGameState.setBounds(100, 240, 200, 100);
        lblGameState.setFont(new Font("Verdana", Font.BOLD, 30));
        lblGameState.setForeground(java.awt.Color.WHITE);
        this.add(lblGameState);
    }

    public void newGame() {
        this.newGame = new JButton("New Game");
        newGame.setBounds(50, 350, 210, 50);
        this.add(newGame);
        newGame.addActionListener(this);
    }

    public void messageBoard() {
        this.messageBoard = new JTextArea();
        messageBoard.setFont(messageFont);
        messageBoard.setMargin(new Insets(7, 7, 7, 7));
        messageBoard.setEditable(false);

        JScrollPane jsp = new JScrollPane();
        jsp.setSize(680, 200);
        jsp.setLocation(50, 470);
        jsp.setViewportView(messageBoard);

        // this.add(messageBoard);
        this.add(jsp);
    }

    public void sendButton() {
        this.send = new JButton("Send");
        send.setSize(100, 50);
        send.setLocation(630, 690);
        this.add(send);
        send.addActionListener(this);
    }

    public void messageField() {
        this.messageField = new JTextField();
        messageField.setBounds(50, 690, 580, 50);
        messageField.setFont(messageFont);
        this.add(messageField);
    }

    public void gameON() {
        if (this.opponentFound == true) {
            gameOn.setText("Opponent found ");
        }
        gameOn.setForeground(java.awt.Color.WHITE);
        gameOn.setFont(conected);
        gameOn.setSize(200, 30);
        gameOn.setLocation(60, 15);
        this.add(gameOn);
    }

    public void infoPanel() {
        this.infoP = new JLabel(new ImageIcon("img/info.png"));
        infoP.setBounds(110, 20, 560, 730);
        infoP.setVisible(false);
        this.add(infoP, 2, 0);
    }

    public void startScreen() {
        this.startScreen = new JLabel(new ImageIcon("img/start.png"));
        startScreen.setBounds(0, -110, 800, 1000);
        startScreen.setVisible(true);
        this.add(startScreen, 3, 0);
    }

    public void infoButton() {
        this.infoB = new JButton();
        this.infoB.setIcon(new ImageIcon("img/infoButton.png"));
        infoB.setBounds(715, 50, 56, 56);
        this.add(infoB);
        infoB.addActionListener(this);
    }

    public void logInButton() {
        this.logIn = new JButton("Join game");
        logIn.setBounds(300, 520, 210, 50);
        logIn.setVisible(true);
        logIn.setBackground(java.awt.Color.BLUE);
        logIn.setForeground(java.awt.Color.WHITE);
        logIn.setFont(new Font("Tahoma", Font.BOLD, 20));
        this.add(logIn, 3, 0);
        logIn.addActionListener(this);
    }

    public void acceptConnect() {
        this.acceptCon = new JButton("Start as host");
        acceptCon.setBounds(350, 415, 240, 50);
        acceptCon.setVisible(true);
        acceptCon.setBackground(java.awt.Color.WHITE);
        acceptCon.setForeground(java.awt.Color.DARK_GRAY);
        acceptCon.setFont(new Font("Tahoma", Font.BOLD, 15));
        this.add(acceptCon, 3, 0);
        acceptCon.addActionListener(this);
        acceptCon.addMouseListener(this);
    }

    public void userName() {
        this.color = new java.awt.Color(204, 204, 204);
        this.userName = new JTextField();
        userName.setBounds(350, 285, 240, 40);
        userName.setVisible(true);
        userName.setFont(messageFont);
        userName.setForeground(color);
        userName.setText("Username");
        userName.addMouseListener(this);
        this.add(userName, 3, 0); //JTextField dissapears when changing constarint to 2.
    }

    public void opponent() {
        this.opponent = new JTextField();
        opponent.setBounds(350, 350, 240, 40);
        opponent.setVisible(true);
        opponent.setFont(messageFont);
        opponent.setForeground(color);
        opponent.setText("IP-Address");
        opponent.addMouseListener(this);
        this.add(opponent, 3, 0);
    }

    public void yourIP() throws IOException {
        this.yourIP = "" + InetAddress.getLocalHost().getHostAddress();
        this.lblYourIP = new JLabel("Player IP: " + yourIP);
        lblYourIP.setBounds(270, 670, 350, 100);
        lblYourIP.setFont(new Font("Verdana", Font.BOLD, 20));
        lblYourIP.setForeground(java.awt.Color.WHITE);
        this.add(lblYourIP, 3, 0);
    }

    public void applicationFrame() {
        JFrame f = new JFrame("OnlineChess");
        // f.setSize(80, 80);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.setSize(800, 820);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
    }

    public void init(String ip) {
        opponentFound = false;
        repaint();
        gameOn.setText("Searching for Opponent ");
        connected = false;

        gc = new ChessClient();
        if (gc.connect(ip)) {
            // we will act as client
            this.opponentFound = true;
            this.myColor = Color.BLACK;
            // chessGame.gameState = ChessGame.GAME_STATE_WHITE;
            repaint();
        } else {
            // we will be a server waiting for a client
            // chessGame.gameState = ChessGame.GAME_STATE_BLACK;
            isClient = false;
            gs = new ChessServer();
            this.myColor = Color.WHITE;
            this.opponentFound = false;
        }
        networkThread = new Thread(this);
        networkThread.start();
    }

    public void sendMove(String move) {

        if (isClient) {
            gc.sendCommand(move);
        } else {
            gs.sendCommand(move);
        }
    }

    public void run() {
        String line;
        if (isClient)
            this.opponentFound = true;
        gameOn.setText("Opponent found ");
        th = true;
        while (th) {
            if (isClient) {
                // act as client
                line = gc.getCommand();
            } else {
                // act as server
                if (!connected) {
                    System.out.println("Waiting for client");
                    gs.waitForClient();
                    connected = true;
                    opponentFound = true;
                    repaint();
                    gameOn.setText("Opponent found ");
                }
                line = gs.getCommand();
            }
            process(line);
            line = null;
        }
    }

    public void process(String command) {
        System.out.println("Process: " + command);
        if (command.equals("####")) {
            messageBoard.setText(null);
            guiPieces.clear();
            // create chess game
            this.chessGame = new ChessGame();

            // wrap game pieces into their graphical representation
            for (Piece piece : this.chessGame.getPieces()) {
                createAndAddGuiPiece(piece);
            }
            repaint();
            return;
        }
        if (command.startsWith("MESSAGE"))
            messageBoard.append(command.substring(7) + "\n");
        if (command.startsWith("MOVE")) {
            System.out.println(command);
            String line, toks[];
            line = command.substring(4);
            toks = line.split("-");
            setNewPieceLocationN(Integer.parseInt(toks[0]), Integer.parseInt(toks[1]), Integer.parseInt(toks[2]),
                    Integer.parseInt(toks[3]), Integer.parseInt(toks[4]), Integer.parseInt(toks[5]));
            repaint();
        }
    }

    /**
     * @return textual description of current game state
     */
    private String getGameStateAsText() {
        if (chessGame.getGameState() != null)
            return chessGame.getGameState().toString();
        else
            return "GAME OVER";
    }

    /**
     * create a game piece
     *
     * @param piece The Piece to add
     */
    private void createAndAddGuiPiece(Piece piece) {
        GuiPiece guiPiece = new GuiPiece(piece);
        this.guiPieces.add(guiPiece);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(this.imgBackground, 0, 0, null);

        if (this.opponentFound == false) {
            g.setColor(java.awt.Color.RED);
        } else {
            g.setColor(java.awt.Color.GREEN);
        }
        g.fillOval(15, 15, 30, 30);

        for (GuiPiece guiPiece : this.guiPieces) {
            if (!guiPiece.isCaptured()) {
                g.drawImage(guiPiece.getImage(), guiPiece.getX(), guiPiece.getY(), null);
            }
        }

        this.lblGameState.setText(this.getGameStateAsText());
    }

    /**
     * @return current game state
     */
    public Color getGameState() {
        return this.chessGame.getGameState();
    }

    /**
     * convert logical column into x coordinate
     *
     * @param column
     * @return x coordinate for column
     */
    public static int convertColumnToX(int column) {
        return PIECES_START_X + SQUARE_WIDTH * column;
    }

    /**
     * convert logical row into y coordinate
     *
     * @param row
     * @return y coordinate for row
     */
    public static int convertRowToY(int row) {
        return PIECES_START_Y + SQUARE_HEIGHT * (Piece.ROW_8 - row);
    }

    /**
     * convert x coordinate into logical column
     *
     * @param x
     * @return logical column for x coordinate
     */
    public static int convertXToColumn(int x) {
        return (x - DRAG_TARGET_SQUARE_START_X) / SQUARE_WIDTH;
    }

    /**
     * convert y coordinate into logical row
     *
     * @param y
     * @return logical row for y coordinate
     */
    public static int convertYToRow(int y) {
        return Piece.ROW_8 - (y - DRAG_TARGET_SQUARE_START_Y) / SQUARE_HEIGHT;
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
        int targetRow = ChessGui.convertYToRow(y);
        int targetColumn = ChessGui.convertXToColumn(x);

        if (targetRow < Piece.ROW_1 || targetRow > Piece.ROW_8 || targetColumn < Piece.COLUMN_A
                || targetColumn > Piece.COLUMN_H) {
            // reset piece position if move is not valid
            dragPiece.resetToUnderlyingPiecePosition();

        } else {
            // change model and update gui piece afterwards
            System.out.println("moving piece to " + targetRow + "/" + targetColumn);
            this.chessGame.movePiece(dragPiece.getPiece().getRow(), dragPiece.getPiece().getColumn(), targetRow,
                    targetColumn);
            for (GuiPiece g : guiPieces)
                g.resetToUnderlyingPiecePosition();
        }
    }

    public void setNewPieceLocationN(int sx, int sy, int setX, int setY, int x, int y) {
        // if(i < guiPieces.size()/2)
        // i += guiPieces.size()/2;
        // else
        // i -= guiPieces.size()/2;
        // System.out.println(guiPieces.size() + " and " + i);

        GuiPiece dragPiece = null;
        for (int i = this.guiPieces.size() - 1; i >= 0; i--) {
            GuiPiece guiPiece = this.guiPieces.get(i);
            if (guiPiece.isCaptured())
                continue;

            if (mouseOverPiece(guiPiece, sx, sy)) {

                if (this.getGameState() == guiPiece.getColor()) {
                    // calculate offset, because we do not want the drag piece
                    // to jump with it's upper left corner to the current mouse
                    // position
                    //
                    dragPiece = guiPiece;
                    break;
                }
            }
        }

        // move drag piece to the top of the list
        if (dragPiece != null) {
            this.guiPieces.remove(dragPiece);
            this.guiPieces.add(dragPiece);
        }
        dragPiece.setX(setX);
        dragPiece.setY(setY);

        int targetRow = ChessGui.convertYToRow(y);
        int targetColumn = ChessGui.convertXToColumn(x);

        if (targetRow < Piece.ROW_1 || targetRow > Piece.ROW_8 || targetColumn < Piece.COLUMN_A
                || targetColumn > Piece.COLUMN_H) {
            // reset piece position if move is not valid
            dragPiece.resetToUnderlyingPiecePosition();

        } else {
            // change model and update gui piece afterwards
            System.out.println("moving piece to " + targetRow + "/" + targetColumn);
            this.chessGame.movePiece(dragPiece.getPiece().getRow(), dragPiece.getPiece().getColumn(), targetRow,
                    targetColumn);
            dragPiece.resetToUnderlyingPiecePosition();
        }
    }

    private boolean mouseOverPiece(GuiPiece guiPiece, int x, int y) {

        return guiPiece.getX() <= x && guiPiece.getX() + guiPiece.getWidth() >= x && guiPiece.getY() <= y
                && guiPiece.getY() + guiPiece.getHeight() >= y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //When pressed shows information abouth chess pieces
        if (e.getSource().equals(infoB) && click == false) {
            this.infoP.setVisible(true);

            click = true;
        } else if (e.getSource().equals(infoB) && click == true) {
            this.infoP.setVisible(false);
            click = false;
        }

        //When button pressed removes start screan and its components and initiate the game
        if (e.getSource().equals(logIn)) {
            if (userName.getText().length() > 3 && !"IP-Address".equals(this.opponent.getText())) {
                int lenght = opponent.getText().length();
                String ip = this.opponent.getText();
                if (gameOver == false) {
                    this.addMouseListener(listener);
                    this.addMouseMotionListener(listener);
                }
                if (serverMode == false && !"Server Mode".equals(this.opponent.getText()) && lenght > 5) {
                    init(ip);
                    System.out.println(ip);
                    this.userName.setVisible(false);
                    this.user = userName.getText();
                    this.startScreen.setVisible(false);
                    this.logIn.setEnabled(false);
                    this.logIn.setVisible(false);
                    this.opponent.setVisible(false);
                    this.acceptCon.setVisible(false);
                    this.acceptCon.setEnabled(false);
                } else if (serverMode == true && "Server Mode".equals(this.opponent.getText())) {
                    this.userName.setVisible(false);
                    this.user = userName.getText();
                    this.startScreen.setVisible(false);
                    this.logIn.setEnabled(false);
                    this.logIn.setVisible(false);
                    this.opponent.setVisible(false);
                    this.acceptCon.setVisible(false);
                    this.acceptCon.setEnabled(false);
                }
            }

        }

        //When button pressed and specific conditionds are true makes you to server and waits for connection
        if (e.getSource().equals(acceptCon) && (this.mousePressed == false)) {
            if (userName.getText().length() > 3
                    && (this.opponent.getText().length() == 0 || "IP-Address".equals(this.opponent.getText()))) {
                this.opponent.setEditable(false);
                this.opponent.setText("Server Mode");
                this.serverMode = true;
                this.mousePressed = true;
                if (this.serverMode == true) {
                    init(yourIP);
                    System.out.println(yourIP);
                }
            }
        }

        //When button pressed sends message
        if (e.getSource().equals(send)) {
            String pMessage = user + ": " + messageField.getText();
            String oMessage = user + ": " + messageField.getText();
            messageField.setText(""); //Removes text in the messageField after sending a message.

            if (isClient) {
                // act as client
                gc.sendCommand("MESSAGE" + pMessage);
                messageBoard.append(pMessage + "\n");

            } else if (connected) {
                // act as server
                gs.sendCommand("MESSAGE" + oMessage);
                messageBoard.append(oMessage + "\n");
            }
        }

        //When button pressed restarts the game
        if (e.getSource().equals(newGame)) {
            messageBoard.setText(null);
            if (isClient)
                gc.sendCommand("####");
            else
                gs.sendCommand("####");

            guiPieces.clear();
            // create chess game
            chessGame = new ChessGame();
            gameOver = false;

            // wrap game pieces into their graphical representation
            for (Piece piece : chessGame.getPieces()) {
                createAndAddGuiPiece(piece);
            }
            repaint();
        }
    }

    // If you click on the textFields the text gets removed and changes text color to black.
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource().equals(userName) && (userName.getText().equals("Username"))) {
            this.userName.setText("");
            this.userName.setForeground(java.awt.Color.BLACK);
        }
        if (e.getSource().equals(opponent) && (opponent.getText().equals("IP-Address"))) {
            this.opponent.setText("");
            this.opponent.setForeground(java.awt.Color.BLACK);
        }
    }

    //mouseEntered and mouseExited are used for hover affect
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource().equals(acceptCon) && (mousePressed == false)) {
            acceptCon.setBackground(java.awt.Color.GREEN);
            acceptCon.setForeground(java.awt.Color.WHITE);

        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource().equals(acceptCon) && (mousePressed == false)) {
            acceptCon.setBackground(java.awt.Color.WHITE);
            acceptCon.setForeground(java.awt.Color.DARK_GRAY);

        }
    }

    //mousePressed and mouseReleased are used as indication on "Server Mode".
    //The color of acceptCon button changes to green
    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource().equals(acceptCon) && (mousePressed == true)) {
            System.out.println("acceptCon button pressed");
            acceptCon.setBackground(java.awt.Color.GREEN);
            acceptCon.setForeground(java.awt.Color.WHITE);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource().equals(acceptCon) && (mousePressed == true)) {
            acceptCon.setBackground(java.awt.Color.GREEN);
            acceptCon.setForeground(java.awt.Color.WHITE);
        }
    }


    //Starts the game
    public static void main(String[] args) throws IOException {
        new ChessGui();
    }
}