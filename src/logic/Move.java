package logic;

public class Move {
    public final int sourceRow;
    public final int sourceColumn;
    public final int targetRow;
    public final int targetColumn;
    public final Piece piece;
    public final Piece captured;

    public Move(int sourceRow, int sourceColumn, int targetRow, int targetColumn, Piece piece, Piece captured) {
        this.sourceRow = sourceRow;
        this.sourceColumn = sourceColumn;
        this.targetRow = targetRow;
        this.targetColumn = targetColumn;
        this.piece = piece;
        this.captured = captured;
    }

    public boolean flipCapture() {
        if (captured != null) {
            captured.isCaptured(!captured.isCaptured());
            return true;
    } else
        return false;
    }
}
