package logic;

import enums.Color;
import enums.Type;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Murat, Alex, Nikola and Ermin
 */
public class MoveValidator {

    private ChessGame chessGame;
    private Piece sourcePiece, targetPiece;
    private boolean output = true;
    private int targetRow, targetCol;
    private LinkedList<Move> validMoves = new LinkedList<>();

    public MoveValidator(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    /**
     * Checks if the specified validMoves is valid
     *
     * @param sourceRow
     * @param sourceColumn
     * @param targetRow
     * @param targetColumn
     * @return true if validMoves is valid, false if validMoves is invalid
     */
    public LinkedList<Move> isMoveValid(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        validMoves.clear();
        // check if target location within boundaries
        if (targetRow < Piece.ROW_1 || targetRow > Piece.ROW_8
                || targetColumn < Piece.COLUMN_A || targetColumn > Piece.COLUMN_H) {
            if (output)
                System.out.println("target row or column out of scope");
            return validMoves;
        }
        sourcePiece = chessGame.getNonCapturedPieceAtLocation(sourceRow, sourceColumn);
        targetPiece = chessGame.getNonCapturedPieceAtLocation(targetRow, targetColumn);
        this.targetRow = targetRow;
        this.targetCol = targetColumn;

        // source piece has right color?
        if (sourcePiece.getColor() != chessGame.getGameState()) {
            if (output)
                System.out.println("It's not your turn");
            return validMoves;
        }

        if (targetPiece != null && targetPiece.getColor() == sourcePiece.getColor()) {
            if (output)
                System.out.println("Targetpiece same color");
            return validMoves;
        }

        // validate piece movement rules
        switch (sourcePiece.getType()) {
            case BISHOP:
                isValidBishopMove();
                break;
            case KING:
                isValidKingMove();
                break;
            case KNIGHT:
                isValidKnightMove();
                break;
            case PAWN:
                isValidPawnMove();
                break;
            case QUEEN:
                isValidQueenMove();
                break;
            case ROOK:
                isValidRookMove();
                break;
        }
        if (!validMoves.isEmpty() && !notInCheck(validMoves.getFirst())) {
            if (output)
                System.out.println("Move puts king in check");
            validMoves.clear();
        }
        return validMoves;
    }

    private void isValidBishopMove() {
        //The bishop can validMoves any number of squares diagonally, but may not leap
        //over other pieces.
        // first lets check if the path to the target is diagonally at all
        int diffRow = Math.abs(targetRow - sourcePiece.getRow());
        int diffColumn = Math.abs(targetCol - sourcePiece.getColumn());

        if (diffRow == diffColumn && diffColumn > 0 && !arePiecesBetweenSourceAndTarget()) {
            validMoves.add(new Move(sourcePiece.getRow(), sourcePiece.getColumn(), targetRow, targetCol, sourcePiece, targetPiece));
        } else {
            // not moving diagonally
            if (sourcePiece.getType() != Type.QUEEN) {
                if (output)
                    System.out.println("Not moving diagonally");
            }
        }
    }

    private void isValidQueenMove() {
        // The queen combines the power of the rook and bishop and can validMoves any number
        // of squares along rank, file, or diagonal, but it may not leap over other pieces.
        //
        isValidBishopMove();
        if (validMoves.isEmpty())
            isValidRookMove();
    }

    private void isValidPawnMove() {
        // The pawn may validMoves forward to the unoccupied square immediately in front
        // of it on the same file, or on its first validMoves it may advance two squares
        // along the same file provided both squares are unoccupied
        int steps = sourcePiece.getRow() - targetRow;
        if (isValidPawnMoveStraight(steps) || isValidPawnMoveDiagonal(steps)) {
            validMoves.add(new Move(sourcePiece.getRow(), sourcePiece.getColumn(), targetRow, targetCol, sourcePiece, targetPiece));
        }
    }

    private boolean isValidPawnMoveStraight(int steps) {
        return sourcePiece.getColumn() == targetCol && targetPiece == null &&
                ((sourcePiece.getColor() == Color.WHITE &&
                        (steps == -1 ||
                                (steps == -2 && sourcePiece.onStartingPlace() &&
                                        !arePiecesBetweenSourceAndTarget())) ||
                        (sourcePiece.getColor() == Color.BLACK &&
                                (steps == 1 ||
                                        (steps == 2 && sourcePiece.onStartingPlace() &&
                                                !arePiecesBetweenSourceAndTarget())))));
    }

    private boolean isValidPawnMoveDiagonal(int steps) {
        return targetPiece != null &&
                Math.abs(sourcePiece.getColumn() - targetCol) == 1 &&
                ((sourcePiece.getColor() == Color.WHITE && steps == -1) ||
                        (sourcePiece.getColor() == Color.BLACK && steps == 1));
    }

    private void isValidKnightMove() {
        // The knight moves to any of the closest squares which are not on the same rank,
        // file or diagonal, thus the validMoves forms an "L"-shape two squares long and one
        // square wide. The knight is the only piece which can leap over other pieces.

        if ((Math.abs(sourcePiece.getRow() - targetRow) == 2 && Math.abs(sourcePiece.getColumn() - targetCol) == 1) ||
                (Math.abs(sourcePiece.getRow() - targetRow) == 1 && Math.abs(sourcePiece.getColumn() - targetCol) == 2))
            validMoves.add(new Move(sourcePiece.getRow(), sourcePiece.getColumn(), targetRow, targetCol, sourcePiece, targetPiece));
        else {
            if (output)
                System.out.println("Invalid knight move");
        }
    }

    private void isValidKingMove() {

        // The king moves one square in any direction, the king has also a special validMoves which is
        // called castling and also involves a rook.

        if (sourcePiece.getRow() - targetRow == 0 &&
                Math.abs(sourcePiece.getColumn() - targetCol) == 2 &&
                sourcePiece.onStartingPlace() &&
                !checkValidator(sourcePiece.getColor())) {

            targetPiece = getCastlingRook(targetRow, targetCol);
            if (!arePiecesBetweenSourceAndTarget()) {

                int reverse = -1;
                boolean isValid = true;
                int direction = Integer.compare(targetPiece.getColumn(), sourcePiece.getColumn());
                Move[] castlingMoves = {new Move(sourcePiece.getRow(), sourcePiece.getColumn(), sourcePiece.getRow(), sourcePiece.getColumn() + direction, sourcePiece, null),
                        new Move(sourcePiece.getRow(), sourcePiece.getColumn() + direction, sourcePiece.getRow(), sourcePiece.getColumn() + (2 * direction), sourcePiece, null),
                        new Move(targetPiece.getRow(), targetPiece.getColumn(), targetRow, sourcePiece.getColumn() + direction, targetPiece, null)};

                for (int i = 0; i < castlingMoves.length; i++) {
                    castlingMoves[i].piece.setRow(castlingMoves[i].targetRow);
                    castlingMoves[i].piece.setColumn(castlingMoves[i].targetColumn);
                    reverse++;
                    if (checkValidator(sourcePiece.getColor())) {
                        isValid = false;
                        break;
                    }
                }
                for (int i = reverse; i >= 0; i--) {
                    castlingMoves[i].piece.setRow(castlingMoves[i].sourceRow);
                    castlingMoves[i].piece.setColumn(castlingMoves[i].sourceColumn);
                }

                if (!isValid) {
                    if (output)
                        System.out.println("Castling puts king in check");
                } else {
                    validMoves.add(new Move(sourcePiece.getRow(), sourcePiece.getColumn(), sourcePiece.getRow(), sourcePiece.getColumn() + (2 * direction), sourcePiece, null));
                    validMoves.add(new Move(targetPiece.getRow(), targetPiece.getColumn(), targetRow, sourcePiece.getColumn() + direction, this.targetPiece, null));
                }
            }
        } else {
            if (Math.abs(sourcePiece.getRow() - targetRow) < 2 && Math.abs(sourcePiece.getColumn() - targetCol) < 2) {
                validMoves.add(new Move(sourcePiece.getRow(), sourcePiece.getColumn(), targetRow, targetCol, sourcePiece, targetPiece));

            } else {
                if (output)
                    System.out.println("moving too far");
            }
        }
    }

    private Piece getCastlingRook(int targetRow, int targetCol) {
        int rookTargetCol = -1;
        if (targetCol == 2)
            rookTargetCol = 0;
        else if (targetCol == 6)
            rookTargetCol = 7;

        Piece p = chessGame.getNonCapturedPieceAtLocation(targetRow, rookTargetCol);
        if (p != null && p.getType() == Type.ROOK &&
                p.getColor() == sourcePiece.getColor() &&
                p.onStartingPlace())
            return p;
        else
            return null;
    }

    private void isValidRookMove() {
        // The rook can validMoves any number of squares along any rank or file, but
        // may not leap over other pieces. Along with the king, the rook is also
        // involved during the king's castling validMoves.

        // first lets check if the path to the target is straight at all
        if ((targetRow - sourcePiece.getRow() == 0 ^ targetCol - sourcePiece.getColumn() == 0) &&
                !arePiecesBetweenSourceAndTarget()) {
            validMoves.add(new Move(sourcePiece.getRow(), sourcePiece.getColumn(), targetRow, targetCol, sourcePiece, targetPiece));
        } else {
            // not moving diagonally
            if (sourcePiece.getType() != Type.QUEEN) {
                if (output)
                    System.out.println("not moving straight");
            }
        }
    }

    public LinkedList<Move> getValidMoves(Piece p) {
        LinkedList<Move> moves = new LinkedList<>();
        switch (p.getType()) {
            case PAWN:
                getValidPawnMoves(p, moves);
                break;
            case KNIGHT:
                getValidKnightMoves(p, moves);
                break;
            case ROOK:
            case BISHOP:
            case QUEEN:
                getValidBishopQueenRookMoves(p, moves);
                break;
            case KING:
                getValidKingMoves(p, moves);
        }
        return moves;
    }

    private boolean outOfMate(Piece p) {
        switch (p.getType()) {
            case PAWN:
                return getValidPawnMoves(p, null);
            case KNIGHT:
                return getValidKnightMoves(p, null);
            case ROOK:
            case BISHOP:
            case QUEEN:
                return getValidBishopQueenRookMoves(p, null);
            case KING:
                return getValidKingMoves(p, null);
        }
        return false;
    }

    /**
     * The following methods are used in two ways: if the list is null, it will return a boolean
     * whether there are any possible moves or not (as soon as a valid Move is found it breaks off)
     * and if the list is initiated it will add all possible moves to the list. The first way is used
     * by the mateValidator, it doesn't care how many moves there is to get out of check, as long as
     * there is at least one. The other way is used to show the player what possible moves they can
     * currently do.
     *
     * @param p     The piece to moves for
     * @param moves The list to add moves to (a LinkedList of int arrays with [row][col])
     */
    private boolean getValidPawnMoves(Piece p, LinkedList<Move> moves) {
        int[] row = (p.getColor() == Color.BLACK ? new int[]{-1, -1, -1, -2} : new int[]{1, 1, 1, 2});
        int[] col = {-1, 0, 1, 0};
        for (int i = 0; i < row.length; i++) {
            isMoveValid(p.getRow(), p.getColumn(), p.getRow() + row[i], p.getColumn() + col[i]);
            if ((i < 3 || p.onStartingPlace()) && !validMoves.isEmpty())
                if (moves != null)
                    moves.addAll(validMoves);
                else
                    return true;
        }
        return false;
    }

    private boolean getValidKnightMoves(Piece p, LinkedList<Move> moves) {
        int[] row = {2, 2, -2, -2, 1, 1, -1, -1};
        int[] col = {1, -1, 1, -1, 2, -2, 2, -2};

        for (int i = 0; i < row.length; i++) {
            isMoveValid(p.getRow(), p.getColumn(), p.getRow() + row[i], p.getColumn() + col[i]);
            if (!validMoves.isEmpty())
                if (moves != null)
                    moves.addAll(validMoves);
                else
                    return true;
        }
        return false;
    }

    private boolean getValidBishopQueenRookMoves(Piece p, LinkedList<Move> moves) {
        int[] row = {1, 0, -1, 0, 1, 1, -1, -1}; // index 0-3: rook validMoves (straight), 4-7: bishop validMoves (diagonally)
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

                isMoveValid(p.getRow(), p.getColumn(), p.getRow() + jumpRow, p.getColumn() + jumpCol);
                moveIsValid = !validMoves.isEmpty();
                if (moveIsValid)
                    if (moves != null)
                        moves.addAll(validMoves);
                    else
                        return true;
            } while (moveIsValid);
        }
        return false;
    }

    private boolean getValidKingMoves(Piece p, LinkedList<Move> moves) {
        for (int row = Math.max(p.getRow() - 1, Piece.ROW_1); row <= Math.min(p.getRow() + 1, Piece.ROW_8); row++)
            for (int col = Math.max(p.getColumn() - 1, Piece.COLUMN_A); col <= Math.min(p.getColumn() + 1, Piece.COLUMN_H); col++) {
                isMoveValid(p.getRow(), p.getColumn(), row, col);
                if (!(row == p.getRow() && col == p.getColumn()) &&
                        !validMoves.isEmpty())
                    if (moves != null)
                        moves.addAll(validMoves);
                    else
                        return true;
            }
        if (p.onStartingPlace()) {
            isMoveValid(p.getRow(), p.getColumn(), p.getRow(), p.getColumn() + 2);
            if (!validMoves.isEmpty())
                if (moves != null)
                    moves.addAll(validMoves);
                else
                    return true;
            isMoveValid(p.getRow(), p.getColumn(), p.getRow(), p.getColumn() - 2);
            if (!validMoves.isEmpty())
                if (moves != null)
                    moves.addAll(validMoves);
                else
                    return true;
        }
        return false;
    }

    private boolean arePiecesBetweenSourceAndTarget() {
        int rowIncrementPerStep = Integer.compare(targetRow, sourcePiece.getRow()),
                columnIncrementPerStep = Integer.compare(targetCol, sourcePiece.getColumn()),
                currentRow = sourcePiece.getRow() + rowIncrementPerStep,
                currentColumn = sourcePiece.getColumn() + columnIncrementPerStep;

        while (true) {
            if (currentRow == targetRow && currentColumn == targetCol) {
                break;
            }
            if (currentRow < Piece.ROW_1 || currentRow > Piece.ROW_8
                    || currentColumn < Piece.COLUMN_A || currentColumn > Piece.COLUMN_H) {
                break;
            }

            if (this.chessGame.isNonCapturedPieceAtLocation(currentRow, currentColumn)) {
                if (output)
                    System.out.println("pieces in between source and target");
                return true;
            }

            currentRow += rowIncrementPerStep;
            currentColumn += columnIncrementPerStep;
        }
        return false;
    }

    private boolean notInCheck(Move move) {
        move.piece.setRow(move.targetRow);
        move.piece.setColumn(move.targetColumn);
        move.flipCapture();
        boolean check = checkValidator(move.piece.getColor());
        move.piece.setRow(move.sourceRow);
        move.piece.setColumn(move.sourceColumn);
        move.flipCapture();
        return !check;
    }

    public boolean checkValidator(Color color) {
        Piece king = null;
        for (Piece p : chessGame.getPieces())
            if (p.getType() == Type.KING && p.getColor() == color) {
                king = p;
                break;
            }

        // Check bishops, queens, pawns, and kings
        int[] row = {1, 1, -1, -1}, col = {1, -1, -1, 1};
        Piece checkingPiece;
        int curRow, curCol;

        for (int i = 0; i < row.length; i++) {
            curRow = king.getRow();
            curCol = king.getColumn();
            do {
                curRow += row[i];
                curCol += col[i];
                checkingPiece = chessGame.getNonCapturedPieceAtLocation(curRow, curCol);

                // Check for en passant
                if (checkingPiece != null && checkingPiece.getRow() != curRow)
                    checkingPiece = null;
            } while (checkingPiece == null && curRow > 0 && curRow < 7 && curCol > 0 && curCol < 7);

            if ((checkingPiece != null && checkingPiece.getColor() != king.getColor()) &&
                    (checkingPiece.getType() == Type.QUEEN ||
                            checkingPiece.getType() == Type.BISHOP ||
                            (checkingPiece.getType() == Type.KING &&
                                    Math.abs(king.getRow() - checkingPiece.getRow()) < 2 &&
                                    Math.abs(king.getColumn() - checkingPiece.getColumn()) < 2) ||
                            (checkingPiece.getType() == Type.PAWN &&
                                    (Math.abs(checkingPiece.getColumn() - king.getColumn()) == 1 &&
                                            ((checkingPiece.getColor() == Color.WHITE &&
                                                    checkingPiece.getRow() - king.getRow() == -1) ||
                                                    (checkingPiece.getColor() == Color.BLACK &&
                                                            Math.abs(checkingPiece.getColumn() - king.getColumn()) == 1))))))
                return true;
        }

        // check rooks and kings and queens
        row = new int[]{1, 0, -1, 0};
        col = new int[]{0, 1, 0, -1};

        for (int i = 0; i < row.length; i++) {
            curRow = king.getRow();
            curCol = king.getColumn();
            do {
                curRow += row[i];
                curCol += col[i];
                checkingPiece = chessGame.getNonCapturedPieceAtLocation(curRow, curCol);

                // Check for en passant
                if (checkingPiece != null && checkingPiece.getRow() != curRow)
                    checkingPiece = null;
            } while (checkingPiece == null && curRow > 0 && curRow < 7 && curCol > 0 && curCol < 7);

            if ((checkingPiece != null &&
                    checkingPiece.getColor() != king.getColor() &&
                    (checkingPiece.getType() == Type.QUEEN ||
                            checkingPiece.getType() == Type.ROOK ||
                            (checkingPiece.getType() == Type.KING &&
                                    Math.abs(king.getRow() - checkingPiece.getRow()) < 2 &&
                                    Math.abs(king.getColumn() - checkingPiece.getColumn()) < 2))))
                return true;
        }

        //Check knights
        row = new int[]{2, 2, -2, -2, 1, 1, -1, -1};
        col = new int[]{1, -1, 1, -1, 2, -2, 2, -2};

        for (int i = 0; i < row.length; i++) {
            checkingPiece = chessGame.getNonCapturedPieceAtLocation(king.getRow() + row[i], king.getColumn() + col[i]);
            if (checkingPiece != null && checkingPiece.getColor() != king.getColor() && checkingPiece.getType() == Type.KNIGHT)
                return true;
        }

        return false;
    }

    boolean mateValidator(List<Piece> pieces, Color color) {
        for (Piece p : pieces)
            if (p.getColor() == color && !p.isCaptured())
                if (outOfMate(p))
                    return false;
        return true;
    }

    /**
     * Method for switching off console output, so the player won't see all the failed tries the computer does
     * when trying to find out if a player is in checkmate
     */
    public void setOutput(boolean output) {
        this.output = output;
    }
}