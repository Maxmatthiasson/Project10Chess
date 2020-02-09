package enums;

public enum Color {
    BLACK, WHITE;
    public Color reverse() {
        if (this == BLACK)
            return WHITE;
        else
            return BLACK;
    }
}
