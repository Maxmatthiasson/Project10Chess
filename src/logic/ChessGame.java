package logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import enums.Color;
import enums.Type;

import javax.swing.*;


/**
 * @author Murat, Alex, Nikola and Ermin
 */
public class ChessGame {

    private Color gameState = Color.WHITE;
    private Color check;
    private Color mate;
    private boolean castlingInProgress = false;
    private Type promotion;
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
        this.pieces = pieces;
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
        for (int i = Piece.COLUMN_A; i <= Piece.COLUMN_H; i++) {
            createAndAddPiece(Color.WHITE, Type.PAWN, Piece.ROW_2, i);
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
        for (int i = Piece.COLUMN_A; i <= Piece.COLUMN_H; i++) {
            createAndAddPiece(Color.BLACK, Type.PAWN, Piece.ROW_7, i);
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

        if (!moveValidator.isMoveValid(sourceRow, sourceColumn, targetRow,
                targetColumn)) {
            System.out.println("move invalid");
            return false;
        }

        Color opponentColor = (piece.getColor().reverse());

            Piece opponentPiece = getNonCapturedPieceAtLocation(targetRow, targetColumn);

            // check if the move is capturing an opponent piece
            if (opponentPiece != null && opponentPiece.getColor() != piece.getColor()) {
                opponentPiece.isCaptured(true);
                resetCounter = true;
            }

            if (moveValidator.isValidCastlingMove()) {
                if (opponentPiece.getColumn() == 0) {
                    piece.setColumn(piece.getColumn() - 2);
                    opponentPiece.setColumn(opponentPiece.getColumn() + 3);
                    opponentPiece.touch();
                } else {
                    piece.setColumn(piece.getColumn() + 2);
                    opponentPiece.setColumn(opponentPiece.getColumn() - 2);
                    opponentPiece.touch();
                }
            } else {
                piece.setRow(targetRow);
                piece.setColumn(targetColumn);
            }

            if (moveValidator.checkValidator(piece.getColor())) {
                System.out.println("illegal move, puts king in check");
                piece.setRow(sourceRow);
                piece.setColumn(sourceColumn);
                if (opponentPiece != null)
                    opponentPiece.isCaptured(false);
                return false;
            } else
                check = null; // If a move was successful, current player isn't in check

            if (piece.getType() == Type.PAWN) {
                resetCounter = true;
                // Check for promotion
                if (promotion == null) { // If the promotion is local
                    if ((piece.getColor() == Color.WHITE && piece.getRow() == Piece.ROW_8) ||
                            (piece.getColor() == Color.BLACK && piece.getRow() == Piece.ROW_1)) {
                        String[] buttons = {"Queen", "Rook", "Bishop", "Knight", "Pawn"};
                        int returnValue = JOptionPane.showOptionDialog(null, "Promote your pawn?", "Promotion",
                                JOptionPane.DEFAULT_OPTION, 0, null, buttons, 1);
                        piece.setType(Type.valueOf(buttons[returnValue].toUpperCase()));
                    }
                } else { // If the promotion is remote
                    piece.setType(promotion);
                    promotion = null;
                }
            }

            piece.touch();

        if (resetCounter)
            moveCounter = 0;
        else
            moveCounter++;

        changeGameState();

        if (moveValidator.checkValidator(opponentColor)) {
            // stopping the console from being flooded by all the faulty
            // moves the computer tries to make while trying to test checkmate
            moveValidator.switchOutput();
            check = opponentColor;
            System.out.println(check.toString() + " in check");
            if (moveValidator.mateValidator(pieces, check)) {
                mate = opponentColor;
                System.out.println(mate + " in checkmate");
            }
            // Turn the console output on again
            moveValidator.switchOutput();
        }

        for (Piece p : pieces.stream().filter(p -> p.getType() == Type.PAWN && p.getColor() == gameState).collect(Collectors.toList()))
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
     * Checks whether there is a non-captured piece at the specified location
     *
     * @param row    one of Piece.ROW_..
     * @param column on of Piece.COLUMN_..
     * @return true, if the location contains a piece
     */
    public boolean isNonCapturedPieceAtLocation(int row, int column) {
        for (Piece piece : pieces) {
            if (piece.getRow() == row && piece.getColumn() == column
                    && !piece.isCaptured()) {
                return true;
            }
        }
        return false;
    }

    void isCastling() {
        castlingInProgress = true;
    }

    public void setPromotion(Type type) {
        promotion = type;
    }

    public LinkedList<int[]> getValidMoves(Piece p) {
        return moveValidator.getValidMoves(p);
    }

    /**
     * @return current game state (one of ChessGame.GAME_STATE_..)
     */
    public String getGameStateAsText() {
        String ret = "<html>";
        if (mate != null)
            ret += mate + " in mate<br>" + mate.reverse() + " wins!";
        else
            ret += gameState.toString().substring(0,1).toUpperCase() + gameState.toString().substring(1).toLowerCase() + "'s move" + (check != null ? "<br>" + check + " in check" : "");
        return ret + "</html>";
    }

    public Color getGameState() {
        return gameState;
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
    private void changeGameState() {
        if (gameState != null)
            gameState = gameState.reverse();
    }

    private void checkEndConditions() {
        // check if game end condition has been reached
        //
        if (mate != null || moveCounter == 50) {

            if (mate != null)
                System.out.println("Game over! " + mate + " won!");
            else
                System.out.println("50 move rule, stalemate!");

            this.gameState = null;
        }
    }

    public boolean inCheck(Color color) {
        return check == color;
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