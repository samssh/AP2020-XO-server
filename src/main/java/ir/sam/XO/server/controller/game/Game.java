package ir.sam.XO.server.controller.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

public class Game {
    private final int width, height, winningRace;
    private final GameState gameState;
    @Getter(onMethod_= @Synchronized)
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

    void setName(Side side,String username){
        gameState.setUsername(side,username);
    }


    public synchronized void putPiece(int i,int j,Side side){
        // check for empty
        gameState.putPiece(i,j,side);
        // check for winning
    }


    public synchronized Side getSideToTurn(){
        return gameState.getSideToTurn();
    }

    public Piece getPiece(Side side){
        return gameState.getPlayerPiece(side);
    }

    public String getOpponentUsername(Side side){
        return gameState.getUsername(side);
    }

    public Piece[][] getCloned(){
        return gameState.getCloned();
    }



}
