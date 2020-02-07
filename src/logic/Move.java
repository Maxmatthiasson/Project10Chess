package logic;

import gui.Piece;

public class Move {
    public final int sourceRow;
    public final int sourceColumn;
    public final int targetRow;
    public final int targetColumn;
    public final Piece piece;

    public Move(int sourceRow, int sourceColumn, int targetRow, int targetColumn, Piece piece) {
        this.sourceRow = sourceRow;
        this.sourceColumn = sourceColumn;
        this.targetRow = targetRow;
        this.targetColumn = targetColumn;
        this.piece = piece;
    }
}
