package gui;

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
		// we check the list from top to buttom
		// (therefore we itereate in reverse order)
		//
		for (int i = this.guiPieces.size()-1; i >= 0; i--) {
			GuiPiece guiPiece = this.guiPieces.get(i);
			if (guiPiece.isCaptured()) continue;

			if(ChessGui.mouseOverPiece(guiPiece,x,y)){
				
				if (this.chessGui.getGameState() == guiPiece.getColor() && this.chessGui.getColor() == guiPiece.getColor()){
					// calculate offset, because we do not want the drag piece
					// to jump with it's upper left corner to the current mouse
					// position
					//
					this.dragOffsetX = x - guiPiece.getX();
					this.dragOffsetY = y - guiPiece.getY();
					this.dragPiece = guiPiece;
					sx = x;
					sy = y;
					break;
				}
			}
		}
		
		// move drag piece to the top of the list
		if(this.dragPiece != null){
			this.guiPieces.remove( this.dragPiece );
			this.guiPieces.add(this.dragPiece);
		}
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		if( this.dragPiece != null){
			int x = evt.getPoint().x - this.dragOffsetX;
			int y = evt.getPoint().y - this.dragOffsetY;
			
			// set game piece to the new location if possible
			//
			chessGui.setNewPieceLocation(this.dragPiece, x, y);
			chessGui.sendMove("MOVE" + sx + "-" + sy + "-" + dragPiece.getX() + "-" +  dragPiece.getY() + "-" + x + "-" + y);
			System.out.println("sx:\t" + sx + "\tsy:\t" + sy + "\nx:\t" + x + "\ty:\t" + y + "\ndX:\t" + dragPiece.getX() + "\tdY:\t" + dragPiece.getY());
			this.chessGui.repaint();
			this.dragPiece = null;
		}
	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		if(this.dragPiece != null){
			
			int x = evt.getPoint().x - this.dragOffsetX;
			int y = evt.getPoint().y - this.dragOffsetY;
			
//			System.out.println(
//					"row:"+ChessGui.convertYToRow(y)
//					+" column:"+ChessGui.convertXToColumn(x));
			
			this.dragPiece.setX(x);
			this.dragPiece.setY(y);
			
			this.chessGui.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}
}