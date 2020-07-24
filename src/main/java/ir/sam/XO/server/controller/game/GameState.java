package ir.sam.XO.server.controller.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class GameState {
    @Getter(value = AccessLevel.PACKAGE)
    private final Piece[][] pieces;
    @Getter
    @Setter
    private Side sideToTurn;
    @Getter(value = AccessLevel.PACKAGE)
    private final Piece[] playerPiece;

    GameState(int width, int height, Side side, Piece pieceP1, Piece pieceP2) {
        pieces = new Piece[width][height];
        playerPiece = new Piece[2];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pieces[i][j] = Piece.EMPTY;
            }
        }
        sideToTurn = side;
        playerPiece[0] = pieceP1;
        playerPiece[1] = pieceP2;
    }

    public Piece getPlayerPiece(Side side) {
        return playerPiece[side.getIndex()];
    }

    synchronized void putPiece(int i, int j, Side side) {
        pieces[i][j] = playerPiece[side.getIndex()];
    }

    Piece getPiece(int i, int j) {
        if (-1 < i && i < pieces.length && -1 < j && j < pieces[0].length)
            return pieces[i][j];
        return Piece.EMPTY;
    }
}