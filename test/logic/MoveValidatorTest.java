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
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        assertTrue(validator.isMoveValid(1, 2, 2, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidWhitePawnTwoStep() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        assertTrue(validator.isMoveValid(1, 2, 3, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidWhitePawnOneStepOnOpponent() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 2, 2));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        assertFalse(validator.isMoveValid(1, 2, 2, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidWhitePawnTwoStepOverOpponent() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 2, 2));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        assertFalse(validator.isMoveValid(1, 2, 3, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidWhitePawnBackwards() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        assertFalse(validator.isMoveValid(1, 2, 1, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidWhitePawnDiagonalNoCapture() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        assertFalse(validator.isMoveValid(1, 2, 2, 3));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidWhitePawnDiagonalCapture() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        Piece capture = new Piece(Color.BLACK, Type.PAWN, 2, 3);
        pieces.add(capture);
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        assertTrue(validator.isMoveValid(1, 2, 2, 3));
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
        MoveValidator validator = new MoveValidator(game);
        game.movePiece(1, 2, 2, 2);
        assertTrue(validator.isMoveValid(6, 2, 5, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidBlackPawnTwoStep() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 6, 2));
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        pieces.add(new Piece(Color.BLACK, Type.KING, 7, 7));
        pieces.add(new Piece(Color.WHITE, Type.KING, 0, 0));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        game.movePiece(1, 2, 2, 2);
        assertTrue(validator.isMoveValid(6, 2, 4, 2));
    }


    @org.junit.jupiter.api.Test
    void isMoveValidBlackPawnOneStepOnOpponent() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 6, 2));
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 4, 2));
        pieces.add(new Piece(Color.BLACK, Type.KING, 7, 7));
        pieces.add(new Piece(Color.WHITE, Type.KING, 0, 0));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        game.movePiece(4, 2, 5, 2);
        assertFalse(validator.isMoveValid(6, 2, 5, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidBlackPawnTwoStepOverOpponent() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 6, 2));
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 4, 2));
        pieces.add(new Piece(Color.BLACK, Type.KING, 7, 7));
        pieces.add(new Piece(Color.WHITE, Type.KING, 0, 0));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        game.movePiece(4, 2, 5, 2);
        assertFalse(validator.isMoveValid(6, 2, 4, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidBlackPawnBackwards() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 6, 2));
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        pieces.add(new Piece(Color.BLACK, Type.KING, 7, 7));
        pieces.add(new Piece(Color.WHITE, Type.KING, 0, 0));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        game.movePiece(1, 2, 2, 2);
        assertFalse(validator.isMoveValid(6, 2, 7, 2));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidBlackPawnDiagonalNoCapture() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.BLACK, Type.PAWN, 6, 2));
        pieces.add(new Piece(Color.WHITE, Type.PAWN, 1, 2));
        pieces.add(new Piece(Color.BLACK, Type.KING, 7, 7));
        pieces.add(new Piece(Color.WHITE, Type.KING, 0, 0));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        game.movePiece(1, 2, 2, 2);
        assertFalse(validator.isMoveValid(6, 2, 5, 3));
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
        MoveValidator validator = new MoveValidator(game);
        game.movePiece(4, 3, 5, 3);
        assertTrue(validator.isMoveValid(6, 2, 5, 3));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidRookMoveStraight() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.ROOK, 0, 0));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        assertTrue(validator.isMoveValid(0, 0, 4, 0));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidRookMoveStraightOverPiece() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.ROOK, 0, 0));
        pieces.add(new Piece(Color.BLACK, Type.BISHOP, 2, 0));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        assertFalse(validator.isMoveValid(0, 0, 4, 0));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidRookCapture() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.ROOK, 0, 0));
        pieces.add(new Piece(Color.BLACK, Type.BISHOP, 4, 0));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        assertTrue(validator.isMoveValid(0, 0, 4, 0));
    }

    @org.junit.jupiter.api.Test
    void isMoveValidRookMoveDiagonal() {
        LinkedList<Piece> pieces = new LinkedList<>();
        pieces.add(new Piece(Color.WHITE, Type.ROOK, 0, 0));
        ChessGame game = new ChessGame(pieces);
        MoveValidator validator = new MoveValidator(game);
        assertFalse(validator.isMoveValid(0, 0, 4, 4));
    }

    @org.junit.jupiter.api.Test
    void checkValidator() {
    }

    @org.junit.jupiter.api.Test
    void mateValidator() {
    }
}