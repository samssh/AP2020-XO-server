package ir.sam.XO.server.controller.game;

import lombok.Getter;
import lombok.Synchronized;

public class Game {
    private final int width, height, winningRace;
    private final GameState gameState;
    @Getter(onMethod_= @Synchronized)
    private volatile boolean started;

    public Game(int width, int height, int winningRace) {
        this.width = width;
        this.height = height;
        this.winningRace = winningRace;
        Side side = ((int) (Math.random() * 2)) == 0 ? Side.PLAYER_ONE : Side.PLAYER_TWO;
        started = false;
        gameState = new GameState(width, height, side);
        Piece pieceP1,pieceP2;
        if (((int) (Math.random() * 2)) == 0){
            pieceP1 = Piece.O;
            pieceP2 = Piece.X;
        }else {
            pieceP1 = Piece.X;
            pieceP2 = Piece.O;
        }
        gameState.getPlayerPiece()[0] = pieceP1;
        gameState.getPlayerPiece()[1] = pieceP2;
    }
}
