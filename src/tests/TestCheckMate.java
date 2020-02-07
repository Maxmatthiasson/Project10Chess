package tests;

import gui.Piece;
import logic.ChessGame;

import java.util.LinkedList;

public class TestCheckMate {
    public static void main(String[] args) {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Piece.COLOR_BLACK, Piece.TYPE_KING, Piece.ROW_8, Piece.COLUMN_G));
        pieces.add(new Piece(Piece.COLOR_WHITE, Piece.TYPE_QUEEN, Piece.ROW_6, Piece.COLUMN_G));
        pieces.add(new Piece(Piece.COLOR_WHITE, Piece.TYPE_KING, Piece.ROW_6, Piece.COLUMN_F));
        ChessGame cg = new ChessGame(pieces);

        System.out.println(cg.toString());

        System.out.println("Move queen");
        cg.movePiece(Piece.ROW_6, Piece.COLUMN_G, Piece.ROW_7, Piece.COLUMN_G);

        System.out.println(cg.toString());
    }
}
