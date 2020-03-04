package gui;

//Creates a chess piece and positions it on a specific row and column

import enums.Color;
import logic.Piece;

import javax.swing.*;
import java.awt.Image;

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
    private boolean added = false;

    private static final Image imgWBishop = new ImageIcon("img/wb.png").getImage();
    private static final Image imgWKing = new ImageIcon("img/wk.png").getImage();
    private static final Image imgWKnight = new ImageIcon("img/wn.png").getImage();
    private static final Image imgWQueen = new ImageIcon("img/wq.png").getImage();
    private static final Image imgWRook = new ImageIcon("img/wr.png").getImage();
    private static final Image imgWPawn = new ImageIcon("img/wp.png").getImage();

    private static final Image imgBBishop = new ImageIcon("img/bb.png").getImage();
    private static final Image imgBKing = new ImageIcon("img/bk.png").getImage();
    private static final Image imgBKnight = new ImageIcon("img/bn.png").getImage();
    private static final Image imgBQueen = new ImageIcon("img/bq.png").getImage();
    private static final Image imgBRook = new ImageIcon("img/br.png").getImage();
    private static final Image imgBPawn = new ImageIcon("img/bp.png").getImage();

    public GuiPiece(Piece piece) {
        this.piece = piece;
        this.resetToUnderlyingPiecePosition();
    }

    private void setImage() {
        switch (piece.getColor()) {
            case WHITE:
                switch (piece.getType()) {
                    case BISHOP:
                        img = imgWBishop;
                        break;
                    case KING:
                        img = imgWKing;
                        break;
                    case KNIGHT:
                        img = imgWKnight;
                        break;
                    case QUEEN:
                        img = imgWQueen;
                        break;
                    case ROOK:
                        img = imgWRook;
                        break;
                    case PAWN:
                        img = imgWPawn;
                        break;
                }
                break;
            case BLACK:
                switch (piece.getType()) {
                    case BISHOP:
                        img = imgBBishop;
                        break;
                    case KING:
                        img = imgBKing;
                        break;
                    case KNIGHT:
                        img = imgBKnight;
                        break;
                    case QUEEN:
                        img = imgBQueen;
                        break;
                    case ROOK:
                        img = imgBRook;
                        break;
                    case PAWN:
                        img = imgBPawn;
                        break;
                }
                break;
        }
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
    public boolean getAdded() {
    	return added;
    }
    public void setAdded(boolean added) {
    		this.added = added;
    }

    /**
     * Returns color of the chess piece
     * @return int
     */
    public Color getColor() {
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
        this.x = GuiHelper.convertColumnToX(piece.getColumn());
        this.y = GuiHelper.convertRowToY(piece.getRow());
        setImage();
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
    public void setCaptured(Boolean isCaptured) {
    	this.piece.isCaptured(isCaptured);
    }
    public boolean isCaptured() {
        return this.piece.isCaptured();
    }
}