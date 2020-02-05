package gui;

import java.awt.Image;

//Creates a chess piece and positions it on a specific row and colum
/**
 * 
 * @author Alex and Nikola
*
 */
public class GuiPiece {
	
	private Image img;
	private int x;
	private int y;
	private Piece piece;

	public GuiPiece(Image img, Piece piece) {
		this.img = img;
		this.piece = piece;

		this.resetToUnderlyingPiecePosition();
	}

	public Image getImage() {
		return img;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return img.getHeight(null);
	}

	public int getHeight() {
		return img.getHeight(null);
	}
	
	/**
	 * Returns color of the chess piece
	 * @return int
	 */
	public int getColor() {
		return this.piece.getColor();
	}
	
	@Override
	public String toString() {
		return this.piece+" "+x+"/"+y;
	}

	/**
	 * move the gui piece back to the coordinates that
	 * correspond with the underlying piece's row and column
	 */
	public void resetToUnderlyingPiecePosition() {
		this.x = ChessGui.convertColumnToX(piece.getColumn());
		this.y = ChessGui.convertRowToY(piece.getRow());
	}

	/**
	 * Returns a chess piece
	 * @return piece
	 */
	public Piece getPiece() {
		return piece;
	}

	/**
	 * Returns a boolean
	 * @return boolean
	 */
	public boolean isCaptured() {
		return this.piece.isCaptured();
	}

}
