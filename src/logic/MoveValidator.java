package logic;

import gui.Piece;

import java.util.LinkedList;

/**
 * @author Murat, Alex, Nikola and Ermin
 */
public class MoveValidator {

    private ChessGame chessGame;
    private Piece sourcePiece;
    private Piece targetPiece;

    public MoveValidator(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    /**
     * Checks if the specified move is valid
     *
     * @param sourceRow
     * @param sourceColumn
     * @param targetRow
     * @param targetColumn
     * @return true if move is valid, false if move is invalid
     */
    public boolean isMoveValid(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {

        sourcePiece = chessGame.getNonCapturedPieceAtLocation(sourceRow, sourceColumn);
        targetPiece = this.chessGame.getNonCapturedPieceAtLocation(targetRow, targetColumn);

        // source piece does not exist
        if (sourcePiece == null) {
            System.out.println("no source piece");
            return false;
        }

        // source piece has right color?
        if (sourcePiece.getColor() == Piece.COLOR_WHITE
                && this.chessGame.getGameState() == ChessGame.GAME_STATE_WHITE) {
            // ok
        } else if (sourcePiece.getColor() == Piece.COLOR_BLACK
                && this.chessGame.getGameState() == ChessGame.GAME_STATE_BLACK) {
            // ok
        } else {
            System.out.println("it's not your turn");
            return false;
        }

        // check if target location within boundaries
        if (targetRow < Piece.ROW_1 || targetRow > Piece.ROW_8
                || targetColumn < Piece.COLUMN_A || targetColumn > Piece.COLUMN_H) {
            System.out.println("target row or column out of scope");
            return false;
        }

        // target location possible?
        if (!(isTargetLocationFree() || isTargetLocationCaptureable()) && sourcePiece.getType() != Piece.TYPE_PAWN) {
            System.out.println("target location not free and not captureable");
            return false;
        }

        // validate piece movement rules
        boolean validPieceMove = false;
        switch (sourcePiece.getType()) {
            case Piece.TYPE_BISHOP:
                validPieceMove = isValidBishopMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case Piece.TYPE_KING:
                validPieceMove = isValidKingMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case Piece.TYPE_KNIGHT:
                validPieceMove = isValidKnightMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case Piece.TYPE_PAWN:
                validPieceMove = isValidPawnMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case Piece.TYPE_QUEEN:
                validPieceMove = isValidQueenMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case Piece.TYPE_ROOK:
                validPieceMove = isValidRookMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            default:
                break;
        }
        if (!validPieceMove) {
            return false;
        } else {
            // ok
        }


        // handle stalemate and checkmate
        // ..

        return true;
    }

    private boolean isTargetLocationCaptureable() {
        if (targetPiece == null) {
            return false;
        } else if (targetPiece.getColor() != sourcePiece.getColor()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isTargetLocationFree() {
        return targetPiece == null;
    }

    private boolean isValidBishopMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        //The bishop can move any number of squares diagonally, but may not leap
        //over other pieces.

        boolean isValid;
        // first lets check if the path to the target is diagonally at all
        int diffRow = Math.abs(targetRow - sourceRow);
        int diffColumn = Math.abs(targetColumn - sourceColumn);

        if (diffRow == diffColumn && diffColumn > 0) {
            isValid = !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn);

        } else {
            // not moving diagonally
            System.out.println(diffRow);
            System.out.println(diffColumn);
            System.out.println("not moving diagonally");
            isValid = false;
        }
        return isValid;
    }

    private boolean isValidQueenMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        // The queen combines the power of the rook and bishop and can move any number
        // of squares along rank, file, or diagonal, but it may not leap over other pieces.
        //
        boolean result = isValidBishopMove(sourceRow, sourceColumn, targetRow, targetColumn);
        result |= isValidRookMove(sourceRow, sourceColumn, targetRow, targetColumn);
        return result;
    }

    private boolean isValidPawnMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {

        boolean isValid = false;
        // The pawn may move forward to the unoccupied square immediately in front
        // of it on the same file, or on its first move it may advance two squares
        // along the same file provided both squares are unoccupied

        if (sourceColumn == targetColumn && isTargetLocationFree()) {

            int steps = sourceRow - targetRow;
            if (sourcePiece.getColor() == Piece.COLOR_WHITE) {
                if (steps == -1)
                    return true;
                else if (steps == -2 && !sourcePiece.isTouched())
                    return arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn);
            } else {
                if (steps == 1)
                    return true;
                else if (steps == 2 && !sourcePiece.isTouched())
                    return arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn);
            }
        } else if (isTargetLocationCaptureable()) {
            if (Math.abs(sourceColumn - targetColumn) == 1)
                if ((sourcePiece.getColor() == Piece.COLOR_WHITE && sourceRow - targetRow == 1) ||
                        (sourcePiece.getColor() == Piece.COLOR_BLACK && sourceRow - targetRow == -1))
                    return true;
        } else
            return false;

        // on its first move it may advance two squares
        // ..

        // The pawn has two special
        // moves, the en passant capture, and pawn promotion.

        // en passant
        // ..
        return isValid;
    }

    private boolean isValidKnightMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        // The knight moves to any of the closest squares which are not on the same rank,
        // file or diagonal, thus the move forms an "L"-shape two squares long and one
        // square wide. The knight is the only piece which can leap over other pieces.

        return ((Math.abs(sourceRow - targetRow) == 2 && Math.abs(sourceColumn - targetColumn) == 1) ||
                (Math.abs(sourceRow - targetRow) == 1 && Math.abs(sourceColumn - targetColumn) == 2));
    }

    private boolean isValidKingMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {

        // The king moves one square in any direction, the king has also a special move which is
        // called castling and also involves a rook.

        if (Math.abs(sourceRow - targetRow) < 2 && Math.abs(sourceColumn - targetColumn) < 2)
            return true;
        else {
            System.out.println("moving too far");
            return false;
        }
        // castling
        // ..
    }

    private boolean isValidRookMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        // The rook can move any number of squares along any rank or file, but
        // may not leap over other pieces. Along with the king, the rook is also
        // involved during the king's castling move.

        // first lets check if the path to the target is straight at all
        if (targetRow - sourceRow == 0 ^ targetColumn - sourceColumn == 0) {
            return !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn);

        } else {
            // not moving diagonally
            System.out.println("not moving straight");
            return false;
        }
    }

    private boolean arePiecesBetweenSourceAndTarget(int sourceRow, int sourceColumn,
                                                    int targetRow, int targetColumn) {
        int rowIncrementPerStep = Integer.compare(targetRow, sourceRow),
                columnIncrementPerStep = Integer.compare(targetColumn, sourceColumn);

        int currentRow = sourceRow + rowIncrementPerStep;
        int currentColumn = sourceColumn + columnIncrementPerStep;
        while (true) {
            if (currentRow == targetRow && currentColumn == targetColumn) {
                break;
            }
            if (currentRow < Piece.ROW_1 || currentRow > Piece.ROW_8
                    || currentColumn < Piece.COLUMN_A || currentColumn > Piece.COLUMN_H) {
                break;
            }

            if (this.chessGame.isNonCapturedPieceAtLocation(currentRow, currentColumn)) {
                System.out.println("pieces in between source and target");
                return true;
            }

            currentRow += rowIncrementPerStep;
            currentColumn += columnIncrementPerStep;
        }
        return false;
    }

    public boolean checkValidator(int color) {
        //Piece whiteKing = null, blackKing = null;
        LinkedList<Piece> kings = new LinkedList<>();
        for (Piece p : chessGame.getPieces())
            if (p.getType() == Piece.TYPE_KING) {
                if (p.getColor() == Piece.COLOR_WHITE)
                    kings.add(p);
                else if (p.getColor() == Piece.COLOR_BLACK)
                    kings.add(p);
            }
        boolean blackInCheck = false, whiteInCheck = false;

        for (Piece king : kings) {
            // Check bishops, queens, pawns, and kings
            int[] rows = {1, 1, -1, -1}, columns = {1, -1, -1, 1};
            int currentRow = king.getRow(), currentColumn = king.getColumn();
            Piece checkingPiece = null;
            for (int i = 0; i < rows.length && checkingPiece == null; i++) {
                do {
                    currentRow += rows[i];
                    currentColumn += columns[i];
                    checkingPiece = chessGame.getNonCapturedPieceAtLocation(currentRow, currentColumn);
                } while (checkingPiece == null && currentRow > 0 && currentRow < 7 && currentColumn > 0 && currentColumn < 7);

                if (checkingPiece != null) {
                    if (checkingPiece.getType() == Piece.TYPE_QUEEN ||
                            checkingPiece.getType() == Piece.TYPE_BISHOP ||
                            (checkingPiece.getType() == Piece.TYPE_KING &&
                                    Math.abs(king.getRow() - checkingPiece.getRow()) < 2 &&
                                    Math.abs(king.getColumn() - checkingPiece.getColumn()) < 2) ||
                            (checkingPiece.getType() == Piece.TYPE_PAWN &&
                                    ((king.getColor() == Piece.COLOR_BLACK &&
                                            checkingPiece.getColor() == Piece.COLOR_WHITE &&
                                            checkingPiece.getRow() - king.getRow() == -1 &&
                                            Math.abs(checkingPiece.getColumn() - king.getColumn()) == 1)) ||
                                    (king.getColor() == Piece.COLOR_WHITE && checkingPiece.getColor() == Piece.COLOR_BLACK &&
                                            checkingPiece.getRow() - king.getRow() == 1 &&
                                            Math.abs(checkingPiece.getColumn() - king.getColumn()) == 1))) {
                        if (king.getColor() == Piece.COLOR_BLACK && checkingPiece.getColor() == Piece.COLOR_WHITE)
                            blackInCheck = true;
                        else if (king.getColor() == Piece.COLOR_WHITE && checkingPiece.getColor() == Piece.COLOR_BLACK)
                            whiteInCheck = true;
                    }
                }
            }

            // check rooks and kings and queens
            rows = new int[]{1, 0, -1, 0};
            columns = new int[]{0, 1, 0, -1};
            currentRow = king.getRow();
            currentColumn = king.getColumn();
            checkingPiece = null;

            for (int i = 0; i < rows.length && checkingPiece == null; i++) {
                do {
                    currentRow += rows[i];
                    currentColumn += columns[i];
                    checkingPiece = chessGame.getNonCapturedPieceAtLocation(currentRow, currentColumn);
                } while (checkingPiece == null && currentRow > 0 && currentRow < 7 && currentColumn > 0 && currentColumn < 7);

                if (checkingPiece != null) {
                    if (checkingPiece.getType() == Piece.TYPE_QUEEN ||
                            checkingPiece.getType() == Piece.TYPE_ROOK ||
                            (checkingPiece.getType() == Piece.TYPE_KING &&
                                    Math.abs(king.getRow() - checkingPiece.getRow()) < 2 &&
                                    Math.abs(king.getColumn() - checkingPiece.getColumn()) < 2))
                        if (king.getColor() == Piece.COLOR_BLACK && checkingPiece.getColor() == Piece.COLOR_WHITE)
                            blackInCheck = true;
                        else if (king.getColor() == Piece.COLOR_WHITE && checkingPiece.getColor() == Piece.COLOR_BLACK)
                            whiteInCheck = true;
                }
            }

            //Check knights
            rows = new int[]{2, 2, -2, -2, 1, 1, -1, -1};
            columns = new int[]{1, -1, 1, -1, 2, -2, 2, -2};
            for (int i = 0; i < rows.length; i++) {
                Piece knight = chessGame.getNonCapturedPieceAtLocation(king.getRow() + rows[i], king.getColumn() + columns[i]);
                if (knight != null && knight.getType() == Piece.TYPE_KNIGHT)
                    if (king.getColor() == Piece.COLOR_BLACK && knight.getColor() == Piece.COLOR_WHITE)
                        blackInCheck = true;
                    else if (king.getColor() == Piece.COLOR_WHITE && knight.getColor() == Piece.COLOR_BLACK)
                        whiteInCheck = true;

            }
        }
        return false;
    }

    public static void main(String[] args) {
        ChessGame ch = new ChessGame();
        MoveValidator mo = new MoveValidator(ch);
        boolean isValid;

        int sourceRow;
        int sourceColumn;
        int targetRow;
        int targetColumn;
        int testCounter = 1;

        // 1 ok
        sourceRow = Piece.ROW_2;
        sourceColumn = Piece.COLUMN_D;
        targetRow = Piece.ROW_3;
        targetColumn = Piece.COLUMN_D;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        ch.movePiece(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter + ". test result: " + (true == isValid));
        testCounter++;

        // 2 it's not white's turn
        sourceRow = Piece.ROW_2;
        sourceColumn = Piece.COLUMN_B;
        targetRow = Piece.ROW_3;
        targetColumn = Piece.COLUMN_B;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter + ". test result: " + (false == isValid));
        testCounter++;

        // 3 ok
        sourceRow = Piece.ROW_7;
        sourceColumn = Piece.COLUMN_E;
        targetRow = Piece.ROW_6;
        targetColumn = Piece.COLUMN_E;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        ch.movePiece(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter + ". test result: " + (true == isValid));
        testCounter++;

        // 4 pieces in the way
        sourceRow = Piece.ROW_1;
        sourceColumn = Piece.COLUMN_F;
        targetRow = Piece.ROW_4;
        targetColumn = Piece.COLUMN_C;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter + ". test result: " + (false == isValid));
        testCounter++;

        // 5 ok
        sourceRow = Piece.ROW_1;
        sourceColumn = Piece.COLUMN_C;
        targetRow = Piece.ROW_4;
        targetColumn = Piece.COLUMN_F;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        ch.movePiece(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter + ". test result: " + (true == isValid));
        testCounter++;

        // 6 ok
        sourceRow = Piece.ROW_8;
        sourceColumn = Piece.COLUMN_B;
        targetRow = Piece.ROW_6;
        targetColumn = Piece.COLUMN_C;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        ch.movePiece(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter + ". test result: " + (true == isValid));
        testCounter++;

        // 7 invalid knight move
        sourceRow = Piece.ROW_1;
        sourceColumn = Piece.COLUMN_G;
        targetRow = Piece.ROW_3;
        targetColumn = Piece.COLUMN_G;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter + ". test result: " + (false == isValid));
        testCounter++;

        // invalid knight move
        sourceRow = Piece.ROW_1;
        sourceColumn = Piece.COLUMN_G;
        targetRow = Piece.ROW_2;
        targetColumn = Piece.COLUMN_E;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter + ". test result: " + (false == isValid));
        testCounter++;

        // ok
        sourceRow = Piece.ROW_1;
        sourceColumn = Piece.COLUMN_G;
        targetRow = Piece.ROW_3;
        targetColumn = Piece.COLUMN_H;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        ch.movePiece(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter + ". test result: " + (true == isValid));
        testCounter++;

        // pieces in between
        sourceRow = Piece.ROW_8;
        sourceColumn = Piece.COLUMN_A;
        targetRow = Piece.ROW_5;
        targetColumn = Piece.COLUMN_A;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        ch.movePiece(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter + ". test result: " + (false == isValid));
        testCounter++;

        //ConsoleGui.printCurrentGameState(ch);
    }
}