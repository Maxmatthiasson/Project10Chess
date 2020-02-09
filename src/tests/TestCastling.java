package tests;

import enums.Type;
import gui.Piece;
import logic.ChessGame;

import java.awt.*;
import java.util.LinkedList;

public class TestCastling {
    public static void main(String[] args) {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.King, Piece.ROW_8, Piece.COLUMN_E));
        pieces.add(new Piece(Color.BLACK, Type.Rook, Piece.ROW_8, Piece.COLUMN_H));
        pieces.add(new Piece(Color.WHITE, Type.Pawn, Piece.ROW_2, Piece.COLUMN_A));
        pieces.add(new Piece(Color.WHITE, Type.King, Piece.ROW_6, Piece.COLUMN_F));
        ChessGame cg = new ChessGame(pieces);

        System.out.println(cg.toString());

        cg.movePiece(Piece.ROW_2, Piece.COLUMN_A, Piece.ROW_3, Piece.COLUMN_A);

        System.out.println(cg.toString());

        System.out.println("Castling");
        cg.movePiece(Piece.ROW_8, Piece.COLUMN_E, Piece.ROW_8, Piece.COLUMN_H);

        System.out.println(cg.toString());
    }
}
