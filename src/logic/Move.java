package logic;

public class Move {
    public final int sourceRow;
    public final int sourceColumn;
    public final int targetRow;
    public final int targetColumn;
    public final Piece piece;
    public Piece captured;

    public Move(int sourceRow, int sourceColumn, int targetRow, int targetColumn, Piece piece) {
        this.sourceRow = sourceRow;
        this.sourceColumn = sourceColumn;
        this.targetRow = targetRow;
        this.targetColumn = targetColumn;
        this.piece = piece;
    }

    public void capture() {
        if (captured != null)
            captured.isCaptured(true);
    }

    public void setCaptured(Piece p) {
        captured = p;
    }
}
