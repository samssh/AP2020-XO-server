package ir.sam.XO.server.controller.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

import java.util.EnumMap;
import java.util.Map;

public class Game {
    private final static Object value = new Object();
    //(1,0),(1,1),(0,1),(-1,1)
    private final static int[] dxs = {1, 1, 0, -1}, dys = {0, 1, 1, 1};
    private final int width, height, winningRace;
    private final GameState gameState;
    @Getter(onMethod_ = @Synchronized)
    @Setter(value = AccessLevel.PACKAGE)
    private volatile GameStatus status;

    public Game(int width, int height, int winningRace) {
        this.width = width;
        this.height = height;
        this.winningRace = winningRace;
        Side side = ((int) (Math.random() * 2)) == 0 ? Side.PLAYER_ONE : Side.PLAYER_TWO;
        status = GameStatus.NOT_STARTED;
        Piece pieceP1, pieceP2;
        if (((int) (Math.random() * 2)) == 0) {
            pieceP1 = Piece.O;
            pieceP2 = Piece.X;
        } else {
            pieceP1 = Piece.X;
            pieceP2 = Piece.O;
        }
        gameState = new GameState(width, height, side, pieceP1, pieceP2);
    }

    void setName(Side side, String username) {
        gameState.setUsername(side, username);
    }


    public synchronized void putPiece(int i, int j, Side side) {
        if (side != gameState.getSideToTurn())
            return;
        if (-1 >= i || i >= width || -1 >= j || j >= height) {
            return;
        }
        gameState.putPiece(i, j, side);
        check(i, j);
        if (status == GameStatus.PLAYING)
            gameState.setSideToTurn(gameState.getSideToTurn().getOther());
    }

    private void check(int i, int j) {
        for (int k = 0; k < dxs.length; k++) {
            int dx = dxs[k], dy = dys[k];
            for (int m = 1 - winningRace; m < 1; m++) {
                Map<Piece, Object> map = new EnumMap<>(Piece.class);
                for (int n = m; n < winningRace + m; n++) {
                    map.put(gameState.getPiece(i + n * dx, j + n * dy), value);
                }
                if (map.size() == 1) {
                    status = GameStatus.ENDED;
                }
            }
        }
    }


    public synchronized Side getSideToTurn() {
        return gameState.getSideToTurn();
    }

    public Piece getPiece(Side side) {
        return gameState.getPlayerPiece(side);
    }

    public String getOpponentUsername(Side side) {
        return gameState.getUsername(side);
    }

    public Piece[][] getCloned() {
        return gameState.getCloned();
    }


}
