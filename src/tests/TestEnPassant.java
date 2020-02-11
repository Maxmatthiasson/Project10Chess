package tests;

import enums.Color;
import enums.Type;
import gui.Piece;
import logic.ChessGame;
import logic.MoveValidator;

import java.util.LinkedList;

public class TestEnPassant {
    public static void main(String[] args) {
        TestEnPassant test = new TestEnPassant();
        test.moveQueenIntoEnPassantIntoMate();
    }

    private void moveQueenIntoEnPassantIntoMate() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.KING, Piece.ROW_8, Piece.COLUMN_G));
        pieces.add(new Piece(Color.BLACK, Type.ROOK, Piece.ROW_8, Piece.COLUMN_H));
        pieces.add(new Piece(Color.BLACK, Type.ROOK, Piece.ROW_8, Piece.COLUMN_F));
        pieces.add(new Piece(Color.BLACK, Type.PAWN, Piece.ROW_7, Piece.COLUMN_G));
        pieces.add(new Piece(Color.WHITE, Type.QUEEN, Piece.ROW_6, Piece.COLUMN_A));
        pieces.add(new Piece(Color.WHITE, Type.KING, Piece.ROW_1, Piece.COLUMN_A));
        ChessGame cg = new ChessGame(pieces);

        System.out.println(cg.toString());

        System.out.println("Move white queen");
        cg.movePiece(Piece.ROW_6, Piece.COLUMN_A, Piece.ROW_6, Piece.COLUMN_B);

        System.out.println(cg.toString());

        System.out.println("Put black pawn in en passant");
        cg.movePiece(Piece.ROW_7, Piece.COLUMN_G, Piece.ROW_5, Piece.COLUMN_G);

        System.out.println(cg.toString());

        System.out.println("Move queen into mate into en passant");
        cg.movePiece(Piece.ROW_6, Piece.COLUMN_B, Piece.ROW_6, Piece.COLUMN_G);
        System.out.println(cg.toString());

    }

    private void moveQueenThroughEnPassantIntoMate() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.KING, Piece.ROW_8, Piece.COLUMN_H));
        pieces.add(new Piece(Color.BLACK, Type.ROOK, Piece.ROW_8, Piece.COLUMN_G));
        pieces.add(new Piece(Color.BLACK, Type.PAWN, Piece.ROW_7, Piece.COLUMN_G));
        pieces.add(new Piece(Color.WHITE, Type.QUEEN, Piece.ROW_6, Piece.COLUMN_A));
        pieces.add(new Piece(Color.WHITE, Type.KING, Piece.ROW_1, Piece.COLUMN_A));
        ChessGame cg = new ChessGame(pieces);

        System.out.println(cg.toString());

        System.out.println("Move white queen");
        cg.movePiece(Piece.ROW_6, Piece.COLUMN_A, Piece.ROW_6, Piece.COLUMN_B);

        System.out.println(cg.toString());

        System.out.println("Put black pawn in en passant");
        cg.movePiece(Piece.ROW_7, Piece.COLUMN_G, Piece.ROW_5, Piece.COLUMN_G);

        System.out.println(cg.toString());

        System.out.println("Move queen into mate through en passant");
        cg.movePiece(Piece.ROW_6, Piece.COLUMN_B, Piece.ROW_6, Piece.COLUMN_H);
        System.out.println(cg.toString());

    }
}
