package ir.sam.XO.server.controller.game;

import ir.sam.XO.server.controller.RequestExecutor;
import ir.sam.XO.server.util.Config;

public class GameLobby {
    private Game gameNotStarted;
    private int height = 7, width = 7, winningRace = 4;

    public GameLobby(Config config) {
        config.getProperty(Integer.class,"height").ifPresent(integer -> height = integer);
        config.getProperty(Integer.class,"width").ifPresent(integer -> width = integer);
        config.getProperty(Integer.class,"winningRace")
                .ifPresent(integer -> winningRace = integer);
    }


    public synchronized Game startGameRequest(RequestExecutor requestExecutor) {
        if (gameNotStarted == null) {
            gameNotStarted = new Game(width,height,winningRace);
            requestExecutor.setSide(Side.PLAYER_ONE);

        }else {

        }
        return null;
    }
}
