package gui;

import logic.Piece;

public class GuiHelper {

    private static final int BOARD_START_X = 301;
    private static final int BOARD_START_Y = 51;

    private static final int SQUARE_WIDTH = 50;
    private static final int SQUARE_HEIGHT = 50;

    private static final int PIECE_WIDTH = 48;
    private static final int PIECE_HEIGHT = 48;

    private static final int PIECES_START_X = BOARD_START_X + (int) (SQUARE_WIDTH / 2.0 - PIECE_WIDTH / 2.0);
    private static final int PIECES_START_Y = BOARD_START_Y + (int) (SQUARE_HEIGHT / 2.0 - PIECE_HEIGHT / 2.0);

    private static final int DRAG_TARGET_SQUARE_START_X = BOARD_START_X - (int) (PIECE_WIDTH / 2.0);
    private static final int DRAG_TARGET_SQUARE_START_Y = BOARD_START_Y - (int) (PIECE_HEIGHT / 2.0);


    /**
     * convert logical column into x coordinate
     *
     * @param column
     * @return x coordinate for column
     */
    public static int convertColumnToX(int column) {
        return PIECES_START_X + SQUARE_WIDTH * column;
    }

    /**
     * convert logical row into y coordinate
     *
     * @param row
     * @return y coordinate for row
     */
    public static int convertRowToY(int row) {
        return PIECES_START_Y + SQUARE_HEIGHT * (Piece.ROW_8 - row);
    }

    /**
     * convert x coordinate into logical column
     *
     * @param x
     * @return logical column for x coordinate
     */
    public static int convertXToColumn(int x) {
        return (x - DRAG_TARGET_SQUARE_START_X) / SQUARE_WIDTH;
    }

    /**
     * convert y coordinate into logical row
     *
     * @param y
     * @return logical row for y coordinate
     */
    public static int convertYToRow(int y) {
        return Piece.ROW_8 - (y - DRAG_TARGET_SQUARE_START_Y) / SQUARE_HEIGHT;
    }
}
