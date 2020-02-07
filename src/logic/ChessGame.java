package logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import gui.Piece;

/**
 * @author Murat, Alex, Nikola and Ermin
 */
public class ChessGame {

    int gameState = GAME_STATE_WHITE;
    public static final int GAME_STATE_WHITE = 0;
    public static final int GAME_STATE_BLACK = 1;
    public static final int GAME_STATE_END = 2;

    private boolean whiteInCheck = false;
    private boolean blackInCheck = false;
    private boolean whiteInMate = false;
    private boolean blackInMate = false;
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

    public ChessGame(LinkedList<Piece> pieces) {
        for (Piece p: pieces)
            createAndAddPiece(p.getColor(), p.getType(), p.getRow(), p.getColumn());
    }

    private void startPositions() {
        // create and place pieces
        // rook, knight, bishop, queen, king, bishop, knight, and rook
        createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_ROOK, Piece.ROW_1, Piece.COLUMN_A);
        createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_KNIGHT, Piece.ROW_1,
                Piece.COLUMN_B);
        createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_BISHOP, Piece.ROW_1,
                Piece.COLUMN_C);
        createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_QUEEN, Piece.ROW_1,
                Piece.COLUMN_D);
        createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_KING, Piece.ROW_1, Piece.COLUMN_E);
        createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_BISHOP, Piece.ROW_1,
                Piece.COLUMN_F);
        createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_KNIGHT, Piece.ROW_1,
                Piece.COLUMN_G);
        createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_ROOK, Piece.ROW_1, Piece.COLUMN_H);

        // pawns
        int currentColumn = Piece.COLUMN_A;
        for (int i = 0; i < 8; i++) {
            createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_PAWN, Piece.ROW_2,
                    currentColumn);
            currentColumn++;
        }

        createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_ROOK, Piece.ROW_8, Piece.COLUMN_A);
        createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_KNIGHT, Piece.ROW_8,
                Piece.COLUMN_B);
        createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_BISHOP, Piece.ROW_8,
                Piece.COLUMN_C);
        createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_QUEEN, Piece.ROW_8,
                Piece.COLUMN_D);
        createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_KING, Piece.ROW_8, Piece.COLUMN_E);
        createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_BISHOP, Piece.ROW_8,
                Piece.COLUMN_F);
        createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_KNIGHT, Piece.ROW_8,
                Piece.COLUMN_G);
        createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_ROOK, Piece.ROW_8, Piece.COLUMN_H);

        // pawns
        currentColumn = Piece.COLUMN_A;
        for (int i = 0; i < 8; i++) {
            createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_PAWN, Piece.ROW_7,
                    currentColumn);
            currentColumn++;
        }
    }

    /**
     * create piece instance and add it to the internal list of pieces
     *
     * @param color  on of Pieces.COLOR_..
     * @param type   on of Pieces.TYPE_..
     * @param row    on of Pieces.ROW_..
     * @param column on of Pieces.COLUMN_..
     */
    public void createAndAddPiece(int color, int type, int row, int column) {
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

        if (!this.moveValidator.isMoveValid(sourceRow, sourceColumn, targetRow,
                targetColumn)) {
            System.out.println("move invalid");
            return false;
        }

        Piece piece = getNonCapturedPieceAtLocation(sourceRow, sourceColumn);

        // check if the move is capturing an opponent piece
        int opponentColor = (piece.getColor() == Piece.COLOR_BLACK ? Piece.COLOR_WHITE
                : Piece.COLOR_BLACK);
        if (isNonCapturedPieceAtLocation(opponentColor, targetRow, targetColumn)) {
            Piece opponentPiece = getNonCapturedPieceAtLocation(targetRow, targetColumn);
            opponentPiece.isCaptured(true);
            resetCounter = true;
        }


        piece.setRow(targetRow);
        piece.setColumn(targetColumn);

        if (moveValidator.checkValidator(piece.getColor())) {
            System.out.println("illegal move, puts king in check");
            piece.setRow(sourceRow);
            piece.setColumn(sourceColumn);
            return false;
        }

        if (piece.getType() == Piece.TYPE_PAWN)
            resetCounter = true;

        piece.touch();

        if (resetCounter)
            moveCounter = 0;
        else
            moveCounter++;

        changeGameState();

        if (moveValidator.checkValidator(opponentColor))
            if (opponentColor == Piece.COLOR_WHITE) {
                System.out.println("White in check");
                whiteInCheck = true;
                if (mateValidator(Piece.COLOR_WHITE)) {
                    System.out.println("White in checkmate");
                    whiteInMate = true;
                }
            } else {
                System.out.println("Black in check");
                blackInCheck = true;
                if (mateValidator(Piece.COLOR_BLACK)) {
                    blackInMate = true;
                    System.out.println("Black in checkmate");
                }
            }

        checkEndConditions();

        return true;
    }

    /**
     * returns the first piece at the specified location that is not marked as
     * 'captured'.
     *
     * @param row    one of Piece.ROW_..
     * @param column one of Piece.COLUMN_..
     * @return the first not captured piece at the specified location
     */
    public Piece getNonCapturedPieceAtLocation(int row, int column) {
        for (Piece piece : this.pieces) {
            if (piece.getRow() == row && piece.getColumn() == column
                    && piece.isCaptured() == false) {
                return piece;
            }
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
    private boolean isNonCapturedPieceAtLocation(int color, int row, int column) {
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

    /**
     * @return current game state (one of ChessGame.GAME_STATE_..)
     */
    public int getGameState() {
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
        switch (this.gameState) {
            case GAME_STATE_BLACK:
                this.gameState = GAME_STATE_WHITE;
                break;
            case GAME_STATE_WHITE:
                this.gameState = GAME_STATE_BLACK;
                break;
            case GAME_STATE_END:
                // don't change anymore
                break;
            default:
                throw new IllegalStateException("unknown game state:" + this.gameState);
        }
    }

    private void checkEndConditions() {
        // check if game end condition has been reached
        //
        if (whiteInMate ||  blackInMate || moveCounter == 50) {

            if (whiteInMate) {
                System.out.println("Game over! Black won!");
            } else if (blackInMate){
                System.out.println("Game over! White won!");
            } else
                System.out.println("50 move rule, stalemate!");

            this.gameState = ChessGame.GAME_STATE_END;
        }
    }

    public boolean mateValidator(int color) {
        LinkedList<Move> validMoves = new LinkedList<>();

        for (Piece p : pieces) {
            if (p.getColor() == color && !p.isCaptured()) {

                if (p.getType() == Piece.TYPE_PAWN) {
                    int[] row = (color == Piece.COLOR_WHITE ? new int[]{-1, -1, -1 - 2} : new int[]{1, 1, 1, 2});
                    int[] col = {-1, 0, 1, 0};
                    for (int i = 0; i < row.length; i++) {
                        if ((i < 3 || !p.isTouched()) && moveValidator.isMoveValid(p.getRow(), p.getColumn(), p.getRow() + row[i], p.getColumn() + col[i])) {
                            validMoves.add(new Move(p.getRow(), p.getColumn(), p.getRow() + row[i], p.getColumn() + col[i], p));
                        }
                    }
                } else if (p.getType() == Piece.TYPE_KING) {
                    for (int row = -1; row < 2; row++)
                        for (int col = -1; col < 2; col++) {
                            if (!(col == 0 && row == 0) && moveValidator.isMoveValid(p.getRow(), p.getColumn(), p.getRow() + row, p.getColumn() + col)) {
                                validMoves.add(new Move(p.getRow(), p.getColumn(), p.getRow() + row, p.getColumn() + col, p));
                            }
                        }
                } else if (p.getType() == Piece.TYPE_BISHOP || p.getType() == Piece.TYPE_ROOK || p.getType() == Piece.TYPE_QUEEN) {
                    int[] row = {1, 0, -1, 0, 1, 1, -1, -1}; // index 0-3: rook move (straight), 4-7: bishop move (diagonally)
                    int[] col = {0, 1, 0, -1, 1, -1, -1, 1};
                    int begin = 0, end = 7;

                    if (p.getType() == Piece.TYPE_ROOK)
                        end = 3;
                    else if (p.getType() == Piece.TYPE_BISHOP)
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
                } else if (p.getType() == Piece.TYPE_KNIGHT) {
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