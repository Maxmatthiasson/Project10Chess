package logic;

import enums.Color;
import enums.Type;

import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveValidatorTest {

    // Test white pawn moves
    @org.junit.jupiter.api.Test
    void isMoveValidWhitePawnOneStep() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 0));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        ChessGame game = new ChessGame(pieces);
        assertTrue(game.movePiece(1, 2, 2, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidWhitePawnTwoStep() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 0));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        ChessGame game = new ChessGame(pieces);
        assertTrue(game.movePiece(1, 2, 3, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidWhitePawnOneStepOnOpponent() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 2, 2));
        ChessGame game = new ChessGame(pieces);
        assertFalse(game.movePiece(1, 2, 2, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidWhitePawnTwoStepOverOpponent() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 2, 2));
        ChessGame game = new ChessGame(pieces);
        assertFalse(game.movePiece(1, 2, 3, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidWhitePawnBackwards() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        ChessGame game = new ChessGame(pieces);
        assertFalse(game.movePiece(1, 2, 1, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidWhitePawnDiagonalNoCapture() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        ChessGame game = new ChessGame(pieces);
        assertFalse(game.movePiece(1, 2, 2, 3));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidWhitePawnDiagonalCapture() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        Piece capture = new Piece(Color.BLACK, Type.PAWN, 2, 3);
        pieces.add(capture);
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 0));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        ChessGame game = new ChessGame(pieces);
        assertTrue(game.movePiece(1, 2, 2, 3));
    }

    // Black pawn moves (game starts for white, so white must make the first move)
    @org.junit.jupiter.api.Test
    void isMoveValidBlackPawnOneStep() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 6, 2));
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        pieces.add(new Piece(Color.BLACK, Type.KING, 7, 7));
        pieces.add(new Piece(Color.WHITE, Type.KING, 0, 0));
        ChessGame game = new ChessGame(pieces);
        game.movePiece(1, 2, 2, 2);
        assertTrue(game.movePiece(6, 2, 5, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidBlackPawnTwoStep() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 6, 2));
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        pieces.add(new Piece(Color.BLACK, Type.KING, 7, 7));
        pieces.add(new Piece(Color.WHITE, Type.KING, 0, 0));
        ChessGame game = new ChessGame(pieces);
        game.movePiece(1, 2, 2, 2);
        assertTrue(game.movePiece(6, 2, 4, 2));
    }


    @org.junit.jupiter.api.Test
    void isMoveValidBlackPawnOneStepOnOpponent() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 6, 2));
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 4, 2));
        pieces.add(new Piece(Color.BLACK, Type.KING, 7, 7));
        pieces.add(new Piece(Color.WHITE, Type.KING, 0, 0));
        ChessGame game = new ChessGame(pieces);
        game.movePiece(4, 2, 5, 2);
        assertFalse(game.movePiece(6, 2, 5, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidBlackPawnTwoStepOverOpponent() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 6, 2));
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 4, 2));
        pieces.add(new Piece(Color.BLACK, Type.KING, 7, 7));
        pieces.add(new Piece(Color.WHITE, Type.KING, 0, 0));
        ChessGame game = new ChessGame(pieces);
        game.movePiece(4, 2, 5, 2);
        assertFalse(game.movePiece(6, 2, 4, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidBlackPawnBackwards() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 6, 2));
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        pieces.add(new Piece(Color.BLACK, Type.KING, 7, 7));
        pieces.add(new Piece(Color.WHITE, Type.KING, 0, 0));
        ChessGame game = new ChessGame(pieces);
        game.movePiece(1, 2, 2, 2);
        assertFalse(game.movePiece(6, 2, 7, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidBlackPawnDiagonalNoCapture() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 6, 2));
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        pieces.add(new Piece(Color.BLACK, Type.KING, 7, 7));
        pieces.add(new Piece(Color.WHITE, Type.KING, 0, 0));
        ChessGame game = new ChessGame(pieces);
        game.movePiece(1, 2, 2, 2);
        assertFalse(game.movePiece(6, 2, 5, 3));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidBlackPawnDiagonalCapture() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 6, 2));
        Piece capture = new Piece(Color.WHITE, Type.PAWN, 4, 3);
        pieces.add(capture);
        pieces.add(new Piece(Color.BLACK, Type.KING, 7, 7));
        pieces.add(new Piece(Color.WHITE, Type.KING, 0, 0));
        ChessGame game = new ChessGame(pieces);
        game.movePiece(4, 3, 5, 3);
        assertTrue(game.movePiece(6, 2, 5, 3));
    }

    // Test rook moves
    @org.junit.jupiter.api.Test
    void isMoveValidRookMoveStraight() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.ROOK, 0, 1));
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 0));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        ChessGame game = new ChessGame(pieces);
        assertTrue(game.movePiece(0, 1, 4, 1));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidRookMoveStraightOverPiece() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.ROOK, 0, 0));
        pieces.add(new Piece(Color.BLACK, Type.BISHOP, 2, 0));
        ChessGame game = new ChessGame(pieces);
        assertFalse(game.movePiece(0, 0, 4, 0));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidRookCapture() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.ROOK, 0, 0));
        pieces.add(new Piece(Color.BLACK, Type.BISHOP, 4, 0));
        pieces.add(new Piece(Color.WHITE, Type.KING,7, 0));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        ChessGame game = new ChessGame(pieces);
        assertTrue(game.movePiece(0, 0, 4, 0));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidRookMoveDiagonal() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.ROOK, 0, 0));
        ChessGame game = new ChessGame(pieces);
        assertFalse(game.movePiece(0, 0, 4, 4));
    }

     //Test knight moves
    @org.junit.jupiter.api.Test
    void isMoveValidKnightMove() {
        ChessGame game = new ChessGame(new LinkedList<>());
        game.startPositions();
        assertTrue(game.movePiece(0, 1, 2, 0));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidKnightInvalidMove() {
        ChessGame game = new ChessGame(new LinkedList<>());
        game.startPositions();
        assertFalse(game.movePiece(0, 1, 2, 1));
    }


    @org.junit.jupiter.api.Test
    void isMoveValidKnightInvalidMove2() {
        ChessGame game = new ChessGame(new LinkedList<>());
        game.startPositions();
        assertFalse(game.movePiece(0, 1, 1, 3));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidKnightCapture() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.KNIGHT, 0, 1));
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 2, 0));
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 0));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        ChessGame game = new ChessGame(pieces);
        assertTrue(game.movePiece(0, 1, 2, 0));
    }

    // Test bishop moves
    @org.junit.jupiter.api.Test
    void isMoveValidBishopDiagonal() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.BISHOP,0, 2));
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 0));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        ChessGame game = new ChessGame(pieces);
        assertTrue(game.movePiece(0, 2, 4, 6));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidBishopStraight() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.BISHOP,0, 2));
        ChessGame game = new ChessGame(pieces);
        assertFalse(game.movePiece(0, 2, 4, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidBishopPieceInWay() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.BISHOP,0, 2));
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 2, 4));
        ChessGame game = new ChessGame(pieces);
        assertFalse(game.movePiece(0, 2, 4, 6));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidBishopCapture() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.BISHOP,0, 2));
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 2, 4));
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 0));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        ChessGame game = new ChessGame(pieces);
        assertTrue(game.movePiece(0, 2, 2, 4));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidQueenDiagonal() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.QUEEN,0, 3));
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 0));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        ChessGame game = new ChessGame(pieces);
        assertTrue(game.movePiece(0, 3, 4, 7));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidQueenStraight() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.QUEEN,0, 3));
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 0));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        ChessGame game = new ChessGame(pieces);
        assertTrue(game.movePiece(0, 3, 4, 3));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidQueenCrooked() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.QUEEN,0, 3));
        ChessGame game = new ChessGame(pieces);
        assertFalse(game.movePiece(0, 3, 4, 4));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidQueenPieceInWay() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.QUEEN,0, 3));
        pieces.add(new Piece(Color.BLACK, Type.PAWN,2, 5));
        ChessGame game = new ChessGame(pieces);
        assertFalse(game.movePiece(0, 3, 4, 7));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidQueenCapture() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.QUEEN,0, 3));
        pieces.add(new Piece(Color.BLACK, Type.PAWN,2, 5));
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 0));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        ChessGame game = new ChessGame(pieces);
        assertTrue(game.movePiece(0, 3, 2, 5));
    }


    @org.junit.jupiter.api.Test
    void isMoveValidKingStraight() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 4));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        ChessGame game = new ChessGame(pieces);
        assertTrue(game.movePiece(0, 4, 1, 4));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidKingDiagonal() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 4));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        ChessGame game = new ChessGame(pieces);
        assertTrue(game.movePiece(0, 4, 1, 5));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidKingTooFar() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 4));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        assertFalse(game.movePiece(0, 4, 2, 6));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidKingPieceInWay() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 4));
        pieces.add(new Piece(Color.WHITE, Type.PAWN,1, 5));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        assertFalse(game.movePiece(0, 4, 1, 5));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidKingCapture() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 4));
        pieces.add(new Piece(Color.BLACK, Type.ROOK,1, 5));
        pieces.add(new Piece(Color.BLACK, Type.KING,0, 0));
        ChessGame game = new ChessGame(pieces);
        assertTrue(game.movePiece(0, 4, 1, 5));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidEnPassantCapture() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 0));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        Piece pawn = new Piece(Color.WHITE, Type.PAWN,1, 5);
        pieces.add(pawn);
        pieces.add(new Piece(Color.BLACK, Type.PAWN,3, 4));
        ChessGame game = new ChessGame(pieces);
        game.movePiece(1, 5, 3, 5);
        game.movePiece(3, 4, 2, 5);
        assertTrue(pawn.isCaptured());
    }


    @org.junit.jupiter.api.Test
    void isMoveValidEnPassantGone() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 0));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        Piece pawn = new Piece(Color.WHITE, Type.PAWN,1, 5);
        pieces.add(pawn);
        pieces.add(new Piece(Color.BLACK, Type.PAWN,3, 4));
        ChessGame game = new ChessGame(pieces);
        game.movePiece(1, 5, 3, 5);
        game.movePiece(0,0,1,1);
        game.movePiece(7,7,6,6);
        game.movePiece(3, 4, 2, 5);
        assertFalse(pawn.isCaptured());
    }

    @org.junit.jupiter.api.Test
    void isMoveValidEnPassantIntoCheck() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.KING,3, 3));
        pieces.add(new Piece(Color.WHITE, Type.KING,7, 7));
        Piece pawn = new Piece(Color.WHITE, Type.PAWN,1, 2);
        pieces.add(pawn);
        pieces.add(new Piece(Color.WHITE, Type.ROOK,3, 1));
        pieces.add(new Piece(Color.BLACK, Type.ROOK,2, 1));
        ChessGame game = new ChessGame(pieces);
        game.movePiece(1, 2, 3, 2);
        assertFalse(game.movePiece(2,1,2,2) && !pawn.isCaptured());
    }

    @org.junit.jupiter.api.Test
    void isMoveValidEnPassantOutOfCheck() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.KING,4, 4));
        pieces.add(new Piece(Color.WHITE, Type.KING,7, 7));
        Piece pawn = new Piece(Color.WHITE, Type.PAWN,1, 3);
        pieces.add(pawn);
        pieces.add(new Piece(Color.WHITE, Type.ROOK,3, 0));
        pieces.add(new Piece(Color.BLACK, Type.ROOK,2, 5));
        ChessGame game = new ChessGame(pieces);
        System.out.println(game.toString());
        game.movePiece(1, 3, 3, 3);
        assertTrue(game.movePiece(2,5,2,3) && pawn.isCaptured());
    }

    @org.junit.jupiter.api.Test
    void isMoveValidCastling() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 4));
        pieces.add(new Piece(Color.WHITE, Type.ROOK,0, 0));
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        assertTrue(game.movePiece(0,4, 0, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidCastlingOutOfCheck() {
        System.out.println("Castling out of check");
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 4));
        pieces.add(new Piece(Color.BLACK, Type.BISHOP, 2, 4));
        pieces.add(new Piece(Color.WHITE, Type.ROOK,0, 0));
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 7));
        ChessGame game = new ChessGame(pieces);
        game.movePiece(1,7,2,7);
        game.movePiece(2,4,1,5);
        assertFalse(game.movePiece(0,4, 0, 0));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidCastlingThroughCheck() {
        System.out.println("Castling through check");
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 4));
        pieces.add(new Piece(Color.BLACK, Type.ROOK, 1, 2));
        pieces.add(new Piece(Color.WHITE, Type.ROOK,0, 0));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        assertFalse(game.movePiece(0,4, 0, 0));
    }


    @org.junit.jupiter.api.Test
    void isMoveValidCastlingIntoCheck() {
        System.out.println("Castling through check");
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.KING,7, 7));
        pieces.add(new Piece(Color.WHITE, Type.KING,0, 4));
        pieces.add(new Piece(Color.BLACK, Type.ROOK, 1, 2));
        pieces.add(new Piece(Color.WHITE, Type.ROOK,0, 0));
        ChessGame game = new ChessGame(pieces);
        assertFalse(game.movePiece(0,4, 0, 0));
    }

    @org.junit.jupiter.api.Test
    void checkValidator() {
    }

    @org.junit.jupiter.api.Test
    void mateValidator() {
    }
}