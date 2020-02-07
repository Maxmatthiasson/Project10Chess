package tests;

import gui.Piece;
import logic.ChessGame;
import logic.MoveValidator;

public class TestMoveValidator {
    public static void main(String[] args) {
        ChessGame ch = new ChessGame();
        MoveValidator mo = new MoveValidator(ch);
        boolean isValid;

        int sourceRow; int sourceColumn; int targetRow; int targetColumn;
        int testCounter = 1;

        // ok
        sourceRow = Piece.ROW_2; sourceColumn = Piece.COLUMN_D;
        targetRow = Piece.ROW_3; targetColumn = Piece.COLUMN_D;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        ch.movePiece(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter+". test result: "+(true==isValid));
        testCounter++;

        // it's not white's turn
        sourceRow = Piece.ROW_2; sourceColumn = Piece.COLUMN_B;
        targetRow = Piece.ROW_3; targetColumn = Piece.COLUMN_B;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter+". test result: "+(false==isValid));
        testCounter++;

        // ok
        sourceRow = Piece.ROW_7; sourceColumn = Piece.COLUMN_E;
        targetRow = Piece.ROW_6; targetColumn = Piece.COLUMN_E;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        ch.movePiece(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter+". test result: "+(true==isValid));
        testCounter++;

        // pieces in the way
        sourceRow = Piece.ROW_1; sourceColumn = Piece.COLUMN_F;
        targetRow = Piece.ROW_4; targetColumn = Piece.COLUMN_C;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter+". test result: "+(false==isValid));
        testCounter++;

        // ok
        sourceRow = Piece.ROW_1; sourceColumn = Piece.COLUMN_C;
        targetRow = Piece.ROW_4; targetColumn = Piece.COLUMN_F;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        ch.movePiece(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter+". test result: "+(true==isValid));
        testCounter++;

        // ok
        sourceRow = Piece.ROW_8; sourceColumn = Piece.COLUMN_B;
        targetRow = Piece.ROW_6; targetColumn = Piece.COLUMN_C;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        ch.movePiece(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter+". test result: "+(true==isValid));
        testCounter++;

        // invalid knight move
        sourceRow = Piece.ROW_1; sourceColumn = Piece.COLUMN_G;
        targetRow = Piece.ROW_3; targetColumn = Piece.COLUMN_G;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter+". test result: "+(false==isValid));
        testCounter++;

        // invalid knight move
        sourceRow = Piece.ROW_1; sourceColumn = Piece.COLUMN_G;
        targetRow = Piece.ROW_2; targetColumn = Piece.COLUMN_E;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter+". test result: "+(false==isValid));
        testCounter++;

        // ok
        sourceRow = Piece.ROW_1; sourceColumn = Piece.COLUMN_G;
        targetRow = Piece.ROW_3; targetColumn = Piece.COLUMN_H;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        ch.movePiece(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter+". test result: "+(true==isValid));
        testCounter++;

        // pieces in between
        sourceRow = Piece.ROW_8; sourceColumn = Piece.COLUMN_A;
        targetRow = Piece.ROW_5; targetColumn = Piece.COLUMN_A;
        isValid = mo.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn);
        ch.movePiece(sourceRow, sourceColumn, targetRow, targetColumn);
        System.out.println(testCounter+". test result: "+(false==isValid));
        testCounter++;
    }
}