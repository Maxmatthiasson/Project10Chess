package logic;

import enums.Color;
import enums.Type;
import gui.Piece;

import static enums.Color.WHITE;

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

        // source piece has right color?
        if (sourcePiece.getColor() != chessGame.getGameState()) {
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
        if (!(isTargetLocationFree() || isTargetLocationCaptureable()) &&
                !(sourcePiece.getType() == Type.King &&  // Special conditions for castling
                        targetPiece.getType() == Type.Rook &&
                        sourcePiece.getColor() == targetPiece.getColor() &&
                        !sourcePiece.isTouched() && !targetPiece.isTouched())) {
            System.out.println("target location not free and not captureable");
            return false;
        }

        // validate piece movement rules
        boolean validPieceMove = false;
        switch (sourcePiece.getType()) {
            case Bishop:
                validPieceMove = isValidBishopMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case King:
                validPieceMove = isValidKingMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case Knight:
                validPieceMove = isValidKnightMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case Pawn:
                validPieceMove = isValidPawnMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case Queen:
                validPieceMove = isValidQueenMove(sourceRow, sourceColumn, targetRow, targetColumn);
                break;
            case Rook:
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

        int steps = sourceRow - targetRow;
        if (sourceColumn == targetColumn && isTargetLocationFree()) {

            if (sourcePiece.getColor() == WHITE) {
                if (steps == -1)
                    return true;
                else if (steps == -2 && !sourcePiece.isTouched())
                    return !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn);
            } else {
                if (steps == 1)
                    return true;
                else if (steps == 2 && !sourcePiece.isTouched())
                    return !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn);
            }
        } else if (isTargetLocationCaptureable()) {
            if (Math.abs(sourceColumn - targetColumn) == 1)
                if ((sourcePiece.getColor() == WHITE && steps == -1) ||
                        (sourcePiece.getColor() == Color.BLACK && steps == 1))
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

        if ((Math.abs(sourceRow - targetRow) == 2 && Math.abs(sourceColumn - targetColumn) == 1) ||
                (Math.abs(sourceRow - targetRow) == 1 && Math.abs(sourceColumn - targetColumn) == 2))
            return true;
        else
            System.out.println("Invalid knight move");
        return false;
    }

    private boolean isValidKingMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {

        // The king moves one square in any direction, the king has also a special move which is
        // called castling and also involves a rook.

        if (targetPiece != null && targetPiece.getType() == Type.Rook && sourcePiece.getColor() == targetPiece.getColor() &&
                !checkValidator(sourcePiece.getColor()) && // One may not castle out of, through, or into check.
                !arePiecesBetweenSourceAndTarget(sourceRow, sourceColumn, targetRow, targetColumn)) {

            boolean isValid = true;
            int reverse = 0;

            int direction = Integer.compare(targetColumn, sourceColumn);
            Move[] castlingMoves = {new Move(sourceRow, sourceColumn, sourceRow, sourceColumn + direction, sourcePiece),
                    new Move(sourceRow, sourceColumn + direction, sourceRow, sourceColumn + (2 * direction), sourcePiece),
                    new Move(targetRow, targetColumn, targetRow, sourceColumn + direction, targetPiece)};

            for (int i = 0; i < castlingMoves.length && isValid; i++) {
                castlingMoves[i].piece.setRow(castlingMoves[i].targetRow);
                castlingMoves[i].piece.setColumn(castlingMoves[i].targetColumn);
                if (checkValidator(sourcePiece.getColor())) {
                    isValid = false;
                    reverse = i;
                }
            }
            if (!isValid) {
                for (int i = reverse; i >= 0; i--) {
                    castlingMoves[i].piece.setRow(castlingMoves[i].sourceRow);
                    castlingMoves[i].piece.setColumn(castlingMoves[i].sourceColumn);
                }
                System.out.println("Castling puts king in check");
                return false;
            } else {
                chessGame.isCastling();
                return true;
            }

        } else {
            if (Math.abs(sourceRow - targetRow) < 2 && Math.abs(sourceColumn - targetColumn) < 2)
                return true;
            else {
                System.out.println("moving too far");
                return false;
            }
        }
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
                columnIncrementPerStep = Integer.compare(targetColumn, sourceColumn),
                currentRow = sourceRow + rowIncrementPerStep,
                currentColumn = sourceColumn + columnIncrementPerStep;

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

    public boolean checkValidator(Color color) {
        Piece king = null;
        for (Piece p : chessGame.getPieces())
            if (p.getType() == Type.King && p.getColor() == color) {
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
            } while (checkingPiece == null && curRow > 0 && curRow < 7 && curCol > 0 && curCol < 7);

            if ((checkingPiece != null &&
                    checkingPiece.getColor() != king.getColor()) &&
                    (checkingPiece.getType() == Type.Queen ||
                            checkingPiece.getType() == Type.Bishop ||
                            (checkingPiece.getType() == Type.King &&
                                    Math.abs(king.getRow() - checkingPiece.getRow()) < 2 &&
                                    Math.abs(king.getColumn() - checkingPiece.getColumn()) < 2) ||
                            (checkingPiece.getType() == Type.Pawn &&
                                    (king.getColor() == checkingPiece.getColor().reverse() &&
                                            Math.abs(checkingPiece.getColumn() - king.getColumn()) == 1 &&
                                            ((checkingPiece.getColor() == WHITE &&
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
            } while (checkingPiece == null && curRow > 0 && curRow < 7 && curCol > 0 && curCol < 7);

            if ((checkingPiece != null &&
                    checkingPiece.getColor() != king.getColor() &&
                    (checkingPiece.getType() == Type.Queen ||
                            checkingPiece.getType() == Type.Rook ||
                            (checkingPiece.getType() == Type.King &&
                                    Math.abs(king.getRow() - checkingPiece.getRow()) < 2 &&
                                    Math.abs(king.getColumn() - checkingPiece.getColumn()) < 2))))
                return true;
        }

        //Check knights
        row = new int[]{2, 2, -2, -2, 1, 1, -1, -1};
        col = new int[]{1, -1, 1, -1, 2, -2, 2, -2};

        for (int i = 0; i < row.length; i++) {
            checkingPiece = chessGame.getNonCapturedPieceAtLocation(king.getRow() + row[i], king.getColumn() + col[i]);
            if (checkingPiece != null && checkingPiece.getColor() != king.getColor() && checkingPiece.getType() == Type.Knight)
                return true;
        }

        return false;
    }
}