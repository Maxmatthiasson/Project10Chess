package tests;

import enums.Color;
import enums.Type;
import logic.Piece;
import logic.ChessGame;

import java.util.LinkedList;

public class TestCastling {
    public static void main(String[] args) {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.KING, Piece.ROW_8, Piece.COLUMN_E));
        pieces.add(new Piece(Color.BLACK, Type.ROOK, Piece.ROW_8, Piece.COLUMN_H));
        pieces.add(new Piece(Color.WHITE, Type.PAWN, Piece.ROW_2, Piece.COLUMN_A));
        pieces.add(new Piece(Color.WHITE, Type.KING, Piece.ROW_6, Piece.COLUMN_F));
        ChessGame cg = new ChessGame(pieces);

        System.out.println(cg.toString());

        cg.movePiece(Piece.ROW_2, Piece.COLUMN_A, Piece.ROW_3, Piece.COLUMN_A);

        System.out.println(cg.toString());

        System.out.println("Castling");
        cg.movePiece(Piece.ROW_8, Piece.COLUMN_E, Piece.ROW_8, Piece.COLUMN_G);

        System.out.println(cg.toString());
    }
}
