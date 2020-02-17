package logic;

import enums.Color;
import enums.Type;

/**
 * 
 * @author Alex and Nikola
 *
 */
public class Piece {

	private final Color color;
	
	private Type type;

	//Chess is played on a square board of
	//eight rows (called ranks and denoted with numbers 1 to 8)
	//and eight columns (called files and denoted with letters a to h) of squares.
	private int row;
	
	public static final int ROW_1 = 0;
	public static final int ROW_2 = 1;
	public static final int ROW_3 = 2;
	public static final int ROW_4 = 3;
	public static final int ROW_5 = 4;
	public static final int ROW_6 = 5;
	public static final int ROW_7 = 6;
	public static final int ROW_8 = 7;
	
	private int column;
	
	public static final int COLUMN_A = 0;
	public static final int COLUMN_B = 1;
	public static final int COLUMN_C = 2;
	public static final int COLUMN_D = 3;
	public static final int COLUMN_E = 4;
	public static final int COLUMN_F = 5;
	public static final int COLUMN_G = 6;
	public static final int COLUMN_H = 7;
	
	private boolean isCaptured = false;
	private boolean onStartingPlace = true;
	private boolean enPassant = false;

	public boolean isEnPassant() {
		return enPassant;
	}

	public void resetEnPassant() {
		enPassant = false;
	}

	public Piece(Color color, Type type, int row, int column) {
		this.row = row;
		this.column = column;
		this.color = color;
		this.type = type;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public void setRow(int row) {
		if (type == Type.PAWN && onStartingPlace && Math.abs(this.row - row) == 2)
			enPassant = true;
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public Color getColor() {
		return this.color;
	}
	
	@Override
	public String toString() {
		String strColor = (this.color==Color.WHITE?"white":"black");

		String strRow = getRowString(this.row);
		String strColumn = getColumnString(this.column);
		
		return strColor+" "+getTypeString()+" "+strRow+"/"+strColumn;
	}

	public String getColorAndType() {
		return (color == Color.WHITE ? "W" : "B") + getTypeString();
	}

	public void setType(Type type) { this.type = type; }

	public Type getType() {
		return this.type;
	}

	public String getTypeString() {
		String strType = "unknown";
		switch (this.type) {
			case BISHOP: strType = "B";break;
			case KING: strType = "K";break;
			case KNIGHT: strType = "N";break;
			case PAWN: strType = "P";break;
			case QUEEN: strType = "Q";break;
			case ROOK: strType = "R";break;
		}
		return strType;
	}
	
	public static String getRowString(int row){
		String strRow = "unknown";
		switch (row) {
			case ROW_1: strRow = "1";break;
			case ROW_2: strRow = "2";break;
			case ROW_3: strRow = "3";break;
			case ROW_4: strRow = "4";break;
			case ROW_5: strRow = "5";break;
			case ROW_6: strRow = "6";break;
			case ROW_7: strRow = "7";break;
			case ROW_8: strRow = "8";break;
		}
		return strRow;
	}
	
	public static String getColumnString(int column){
		String strColumn = "unknown";
		switch (column) {
			case COLUMN_A: strColumn = "A";break;
			case COLUMN_B: strColumn = "B";break;
			case COLUMN_C: strColumn = "C";break;
			case COLUMN_D: strColumn = "D";break;
			case COLUMN_E: strColumn = "E";break;
			case COLUMN_F: strColumn = "F";break;
			case COLUMN_G: strColumn = "G";break;
			case COLUMN_H: strColumn = "H";break;
		}
		return strColumn;
	}

	public void isCaptured(boolean isCaptured) {
		this.isCaptured = isCaptured;
	}

	public boolean isCaptured() {
		return this.isCaptured;
	}

	public void touch() {
		onStartingPlace = false;
	}

	public boolean onStartingPlace() {
		return onStartingPlace;
	}

}
