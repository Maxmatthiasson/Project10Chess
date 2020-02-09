package logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import enums.Color;
import enums.Type;
import gui.Piece;

import javax.swing.*;


/**
 * @author Murat, Alex, Nikola and Ermin
 */
public class ChessGame {

    private Color gameState = Color.WHITE;
    /*public static final int GAME_STATE_WHITE = 0;
    public static final int GAME_STATE_BLACK = 1;
    public static final int GAME_STATE_END = 2;*/

    private boolean whiteInCheck = false;
    private boolean blackInCheck = false;
    private boolean whiteInMate = false;
    private boolean blackInMate = false;
    private boolean castlingInProgress = false;
    private int moveCounter = 0;


    // 0 = bottom, size = top
    private List<Piece> pieces = new ArrayList<>();

    private MoveValidator moveValidator = new MoveValidator(this);

    /**
     * initialize game
     */
    public ChessGame() {
        startPositions();
    }

    // A constructor for testing purposes, get any starting condition you need
    public ChessGame(LinkedList<Piece> pieces) {
        for (Piece p : pieces)
            createAndAddPiece(p.getColor(), p.getType(), p.getRow(), p.getColumn());
    }

    private void startPositions() {
        // create and place pieces
        // rook, knight, bishop, queen, king, bishop, knight, and rook
        createAndAddPiece(Color.WHITE, Type.ROOK, Piece.ROW_1, Piece.COLUMN_A);
        createAndAddPiece(Color.WHITE, Type.KNIGHT, Piece.ROW_1,
                Piece.COLUMN_B);
        createAndAddPiece(Color.WHITE, Type.BISHOP, Piece.ROW_1,
                Piece.COLUMN_C);
        createAndAddPiece(Color.WHITE, Type.QUEEN, Piece.ROW_1,
                Piece.COLUMN_D);
        createAndAddPiece(Color.WHITE, Type.KING, Piece.ROW_1, Piece.COLUMN_E);
        createAndAddPiece(Color.WHITE, Type.BISHOP, Piece.ROW_1,
                Piece.COLUMN_F);
        createAndAddPiece(Color.WHITE, Type.KNIGHT, Piece.ROW_1,
                Piece.COLUMN_G);
        createAndAddPiece(Color.WHITE, Type.ROOK, Piece.ROW_1, Piece.COLUMN_H);

        // pawns
        int currentColumn = Piece.COLUMN_A;
        for (int i = 0; i < 8; i++) {
            createAndAddPiece(Color.WHITE, Type.PAWN, Piece.ROW_2,
                    currentColumn);
            currentColumn++;
        }

        createAndAddPiece(Color.BLACK, Type.ROOK, Piece.ROW_8, Piece.COLUMN_A);
        createAndAddPiece(Color.BLACK, Type.KNIGHT, Piece.ROW_8,
                Piece.COLUMN_B);
        createAndAddPiece(Color.BLACK, Type.BISHOP, Piece.ROW_8,
                Piece.COLUMN_C);
        createAndAddPiece(Color.BLACK, Type.QUEEN, Piece.ROW_8,
                Piece.COLUMN_D);
        createAndAddPiece(Color.BLACK, Type.KING, Piece.ROW_8, Piece.COLUMN_E);
        createAndAddPiece(Color.BLACK, Type.BISHOP, Piece.ROW_8,
                Piece.COLUMN_F);
        createAndAddPiece(Color.BLACK, Type.KNIGHT, Piece.ROW_8,
                Piece.COLUMN_G);
        createAndAddPiece(Color.BLACK, Type.ROOK, Piece.ROW_8, Piece.COLUMN_H);

        // pawns
        currentColumn = Piece.COLUMN_A;
        for (int i = 0; i < 8; i++) {
            createAndAddPiece(Color.BLACK, Type.PAWN, Piece.ROW_7,
                    currentColumn);
            currentColumn++;
        }
    }

    /**
     * create piece instance and add it to the internal list of pieces
     *
     * @param color  on of Color..
     * @param type   on of Type..
     * @param row    on of Pieces.ROW_..
     * @param column on of Pieces.COLUMN_..
     */
    public void createAndAddPiece(Color color, Type type, int row, int column) {
        Piece piece = new Piece(color, type, row, column);
        this.pieces.add(piece);
    }

    /**
     * Move piece to the specified location. If the target location is occupied
     * by an opponent piece, that piece is marked as 'captured'. If the move
     * could not be executed successfully, 'false' is returned and the game
     * state does not change.
     *
     * @param sourceRow    the source row (Piece.ROW_..) of the piece to move
     * @param sourceColumn the source column (Piece.COLUMN_..) of the piece to
     *                     move
     * @param targetRow    the target row (Piece.ROW_..)
     * @param targetColumn the target column (Piece.COLUMN_..)
     * @return true, if piece was moved successfully
     */
    public boolean movePiece(int sourceRow, int sourceColumn, int targetRow,
                             int targetColumn) {
        boolean resetCounter = false;
        Piece piece = getNonCapturedPieceAtLocation(sourceRow, sourceColumn);

        // Does source piece exist
        if (piece == null) {
            System.out.println("no source piece");
            return false;
        }

        if (!this.moveValidator.isMoveValid(sourceRow, sourceColumn, targetRow,
                targetColumn)) {
            System.out.println("move invalid");
            return false;
        }

        Color opponentColor = (piece.getColor().reverse());
        if (!castlingInProgress) {

            Piece opponentPiece = getNonCapturedPieceAtLocation(targetRow, targetColumn);

            // check if the move is capturing an opponent piece
            if (opponentPiece != null && opponentPiece.getColor() != piece.getColor()) {
                opponentPiece.isCaptured(true);
                resetCounter = true;
            }

            piece.setRow(targetRow);
            piece.setColumn(targetColumn);

            if (moveValidator.checkValidator(piece.getColor())) {
                System.out.println("illegal move, puts king in check");
                piece.setRow(sourceRow);
                piece.setColumn(sourceColumn);
                if (opponentPiece != null)
                    opponentPiece.isCaptured(false);
                return false;
            }

            if (piece.getType() == Type.PAWN) {
                resetCounter = true;
                if ((piece.getColor() == Color.WHITE && piece.getRow() == Piece.ROW_8) ||
                        (piece.getColor() == Color.BLACK && piece.getRow() == Piece.ROW_1)) {
                    String[] buttons = {"Queen", "Rook", "Bishop", "Knight", "Pawn"};
                    int returnValue = JOptionPane.showOptionDialog(null, "Promote your pawn?", "Promotion",
                            JOptionPane.DEFAULT_OPTION, 0, null, buttons, 1);
                    piece.setType(Type.valueOf(buttons[returnValue].toUpperCase()));
                }
            }

            piece.touch();
        } else {
            castlingInProgress = false;
            resetCounter = true;
        }
        if (resetCounter)
            moveCounter = 0;
        else
            moveCounter++;

        changeGameState();

        if (moveValidator.checkValidator(opponentColor))
            if (opponentColor == Color.WHITE) {
                System.out.println("White in check");
                whiteInCheck = true;
                if (mateValidator(Color.WHITE)) {
                    System.out.println("White in checkmate");
                    whiteInMate = true;
                }
            } else {
                System.out.println("Black in check");
                blackInCheck = true;
                if (mateValidator(Color.BLACK)) {
                    blackInMate = true;
                    System.out.println("Black in checkmate");
                }
            }

        for (Piece p : pieces)
            if (p.getType() == Type.PAWN && p.getColor() == gameState)
                p.resetEnPassant();

        checkEndConditions();

        return true;
    }

    /**
     * returns the first piece at the specified location that is not marked as
     * 'captured'.
     *
     * @param row    one of Piece.ROW_..
     * @param column one of Piece.COLUMN_..
     * @return the first not captured piece at the specified location (or a pawn ahead if it is in en passant)
     */
    public Piece getNonCapturedPieceAtLocation(int row, int column) {
        for (Piece piece : this.pieces) {
            if ((!piece.isCaptured() && piece.getColumn() == column) && (
                    (piece.getRow() == row) ||
                            (piece.getType() == Type.PAWN && piece.isEnPassant() && (
                                    (piece.getColor() == Color.WHITE && piece.getRow() == row + 1 && piece.getRow() == Piece.ROW_4) ||
                                            (piece.getColor() == Color.BLACK && piece.getRow() == row - 1 && piece.getRow() == Piece.ROW_5)))))
                return piece;
        }
        return null;
    }

    /**
     * Checks whether there is a piece at the specified location that is not
     * marked as 'captured' and has the specified color.
     *
     * @param color  one of Piece.COLOR_..
     * @param row    one of Piece.ROW_..
     * @param column on of Piece.COLUMN_..
     * @return true, if the location contains a not-captured piece of the
     * specified color
     */
    private boolean isNonCapturedPieceAtLocation(Color color, int row, int column) {
        for (Piece piece : this.pieces) {
            if (piece.getRow() == row && piece.getColumn() == column
                    && piece.isCaptured() == false && piece.getColor() == color) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether there is a non-captured piece at the specified location
     *
     * @param row    one of Piece.ROW_..
     * @param column on of Piece.COLUMN_..
     * @return true, if the location contains a piece
     */
    public boolean isNonCapturedPieceAtLocation(int row, int column) {
        for (Piece piece : this.pieces) {
            if (piece.getRow() == row && piece.getColumn() == column
                    && piece.isCaptured() == false) {
                return true;
            }
        }
        return false;
    }

    public void isCastling() {
        castlingInProgress = true;
    }

    /**
     * @return current game state (one of ChessGame.GAME_STATE_..)
     */
    public Color getGameState() {
        return this.gameState;
    }

    /**
     * @return the internal list of pieces
     */
    public List<Piece> getPieces() {
        return this.pieces;
    }

    /**
     * switches the game state depending on the current board situation.
     */
    public void changeGameState() {
        if (gameState != null)
            gameState = gameState.reverse();
    }

    private void checkEndConditions() {
        // check if game end condition has been reached
        //
        if (whiteInMate || blackInMate || moveCounter == 50) {

            if (whiteInMate) {
                System.out.println("Game over! Black won!");
            } else if (blackInMate) {
                System.out.println("Game over! White won!");
            } else
                System.out.println("50 move rule, stalemate!");

            this.gameState = null;
        }
    }

    public boolean mateValidator(Color color) {
        LinkedList<Move> validMoves = new LinkedList<>();

        for (Piece p : pieces) {
            if (p.getColor() == color && !p.isCaptured()) {

                if (p.getType() == Type.PAWN) {
                    int[] row = (color == Color.BLACK ? new int[]{-1, -1, -1, -2} : new int[]{1, 1, 1, 2});
                    int[] col = {-1, 0, 1, 0};
                    for (int i = 0; i < row.length; i++)
                        if ((i < 3 || p.onStartingPlace()) && moveValidator.isMoveValid(p.getRow(), p.getColumn(), p.getRow() + row[i], p.getColumn() + col[i]))
                            validMoves.add(new Move(p.getRow(), p.getColumn(), p.getRow() + row[i], p.getColumn() + col[i], p));

                } else if (p.getType() == Type.KING) {
                    for (int row = -1; row < 2; row++)
                        for (int col = -1; col < 2; col++) {
                            if (!(col == 0 && row == 0) && moveValidator.isMoveValid(p.getRow(), p.getColumn(), p.getRow() + row, p.getColumn() + col)) {
                                validMoves.add(new Move(p.getRow(), p.getColumn(), p.getRow() + row, p.getColumn() + col, p));
                            }
                        }
                } else if (p.getType() == Type.BISHOP || p.getType() == Type.ROOK || p.getType() == Type.QUEEN) {
                    int[] row = {1, 0, -1, 0, 1, 1, -1, -1}; // index 0-3: rook move (straight), 4-7: bishop move (diagonally)
                    int[] col = {0, 1, 0, -1, 1, -1, -1, 1};
                    int begin = 0, end = 7;

                    if (p.getType() == Type.ROOK)
                        end = 3;
                    else if (p.getType() == Type.BISHOP)
                        begin = 4;

                    for (int i = begin; i <= end; i++) {
                        int jumpRow = 0;
                        int jumpCol = 0;
                        boolean moveIsValid;
                        do {
                            jumpRow += row[i];
                            jumpCol += col[i];
                            moveIsValid = moveValidator.isMoveValid(p.getRow(), p.getColumn(), p.getRow() + jumpRow, p.getColumn() + jumpCol);
                            if (moveIsValid)
                                validMoves.add(new Move(p.getRow(), p.getColumn(), p.getRow() + jumpRow, p.getColumn() + jumpCol, p));
                        } while (moveIsValid);
                    }
                } else if (p.getType() == Type.KNIGHT) {
                    int[] row = {2, 2, -2, -2, 1, 1, -1, -1};
                    int[] col = {1, -1, 1, -1, 2, -2, 2, -2};

                    for (int i = 0; i < row.length; i++) {
                        if (moveValidator.isMoveValid(p.getRow(), p.getColumn(), p.getRow() + row[i], p.getColumn() + col[i]))
                            validMoves.add(new Move(p.getRow(), p.getColumn(), p.getRow() + row[i], p.getColumn() + col[i], p));
                    }
                }
            }
        }
        System.out.println("Valid moves: " + validMoves.size());
        for (Move m : validMoves) {
            m.piece.setRow(m.targetRow);
            m.piece.setColumn(m.targetColumn);
            boolean mate = moveValidator.checkValidator(color);
            m.piece.setRow(m.sourceRow);
            m.piece.setColumn(m.sourceColumn);
            if (!mate)
                return false;
        }
        return true;
    }

    public String toString() {
        LinkedList<Piece> piecesToPrint = new LinkedList<>();
        for (Piece p : pieces)
            if (!p.isCaptured())
                piecesToPrint.add(p);
        String letters = "   A  B  C  D  E  F  G  H\n";
        String sep = "   -- -- -- -- -- -- -- --\n";
        StringBuilder res = new StringBuilder(letters);
        res.append(sep);
        for (int row = 7; row >= 0; row--) {
            res.append(row + 1).append(" |");
            for (int col = 0; col < 8; col++) {
                Piece toPrint = null;
                Iterator<Piece> pieceIterator = piecesToPrint.iterator();
                while (pieceIterator.hasNext() && toPrint == null) {
                    Piece p = pieceIterator.next();
                    if (p.getRow() == row && p.getColumn() == col) {
                        toPrint = p;
                        pieceIterator.remove();
                    }
                }
                if (toPrint != null)
                    res.append(toPrint.getColorAndType()).append("|");
                else
                    res.append("  |");
            }
            res.append("\n").append(sep);
        }
        res.append(letters);

        return res.toString();
    }
}