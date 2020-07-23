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
    private final String[] usernames;
    @Getter(value = AccessLevel.PACKAGE)
    private final Piece[] playerPiece;
    @Getter
    private volatile Piece[][] cloned;

    GameState(int width, int height, Side side) {
        pieces = new Piece[width][height];
        usernames = new String[2];
        playerPiece = new Piece[2];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pieces[i][j] = Piece.EMPTY;
            }
        }
        sideToTurn = side;
    }

    void setUsername(Side side, String username) {
        usernames[side.getIndex()] = username;
    }

    public String getUsername(Side side) {
        return usernames[side.getIndex()];
    }

    public Piece getPlayerPiece(Side side) {
        return playerPiece[side.getIndex()];
    }

    public synchronized void putPiece(int i, int j, Side side) {
        pieces[i][j] = playerPiece[side.getIndex()];
        updateCloned();
    }

    public Piece getPiece(int i, int j, Side side) {
        if (-1 < i && i < pieces.length && -1 < j && j < pieces[0].length)
            return pieces[i][j];
        return Piece.EMPTY;
    }

    private void updateCloned() {
        Piece[][] temp = new Piece[pieces.length][pieces[0].length];
        for (int i = 0; i < pieces.length; i++) {
            System.arraycopy(pieces[i], 0, temp[i], 0, pieces[i].length);
        }
        cloned = temp;
    }
}