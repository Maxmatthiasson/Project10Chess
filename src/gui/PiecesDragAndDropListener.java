package gui;

import enums.Type;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

/**
 * 
 * @author Alex and Nikola
 *
 */
public class PiecesDragAndDropListener implements MouseListener, MouseMotionListener {

	private List<GuiPiece> guiPieces;
	private ChessGui chessGui;
	
	private GuiPiece dragPiece;
	private int sx,sy;
	private int dragOffsetX;
	private int dragOffsetY;
	

	public PiecesDragAndDropListener(List<GuiPiece> guiPieces, ChessGui chessGui) {
		this.guiPieces = guiPieces;
		this.chessGui = chessGui;
	}

	public GuiPiece getDragPiece() {
		return dragPiece;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent evt) {
		int x = evt.getPoint().x;
		int y = evt.getPoint().y;
		
		// find out which piece to move.
		// we check the list from top to bottom
		// (therefore we iterate in reverse order)
		//
		for (int i = guiPieces.size()-1; i >= 0; i--) {
			GuiPiece guiPiece = guiPieces.get(i);
			if (guiPiece.isCaptured()) continue;

			if(ChessGui.mouseOverPiece(guiPiece,x,y)){
				
				if (chessGui.getGameState() == guiPiece.getColor() && chessGui.getColor() == guiPiece.getColor()){
					// calculate offset, because we do not want the drag piece
					// to jump with it's upper left corner to the current mouse
					// position
					//
					dragOffsetX = x - guiPiece.getX();
					dragOffsetY = y - guiPiece.getY();
					dragPiece = guiPiece;
					sx = x;
					sy = y;
					break;
				}
			}
		}
		
		// move drag piece to the top of the list
		if(dragPiece != null){
			guiPieces.remove( dragPiece );
			guiPieces.add(dragPiece);
		}
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		if( dragPiece != null){
			int x = evt.getPoint().x - dragOffsetX;
			int y = evt.getPoint().y - dragOffsetY;
            Type orgType = dragPiece.getPiece().getType();
			// set game piece to the new location if possible
			chessGui.setNewPieceLocation(dragPiece, x, y);
			Type newType = dragPiece.getPiece().getType();
			String promotion = (orgType != newType ? "-" + newType.toString() : "");
			chessGui.sendMove("MOVE" + sx + "-" + sy + "-" + x + "-" +  y + promotion);
			chessGui.repaint();
			dragPiece = null;
		}
		chessGui.clearPossibleMoves();
	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		if(dragPiece != null){
			
			int x = evt.getPoint().x - dragOffsetX;
			int y = evt.getPoint().y - dragOffsetY;

			dragPiece.setX(x);
			dragPiece.setY(y);
			
			chessGui.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}
}