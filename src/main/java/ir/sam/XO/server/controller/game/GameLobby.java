package ir.sam.XO.server.controller.game;

import ir.sam.XO.server.controller.RequestExecutor;
import ir.sam.XO.server.database.Connector;
import ir.sam.XO.server.model.PlayerState;
import ir.sam.XO.server.util.Config;

public class GameLobby {
    private Game gameNotStarted;
    private RequestExecutor waiting;
    private int height = 7, width = 7, winningRace = 4;
    private final Connector connector;

    public GameLobby(Config config, Connector connector) {
        this.connector = connector;
        config.getProperty(Integer.class,"height").ifPresent(integer -> height = integer);
        config.getProperty(Integer.class,"width").ifPresent(integer -> width = integer);
        config.getProperty(Integer.class,"winningRace")
                .ifPresent(integer -> winningRace = integer);
    }


    public synchronized Game startGameRequest(RequestExecutor requestExecutor) {
        if (gameNotStarted == null) {
            gameNotStarted = new Game(width,height,winningRace,connector);
            waiting = requestExecutor;
            requestExecutor.setSide(Side.PLAYER_ONE);
            requestExecutor.getPlayer().setState(PlayerState.WAITING_FOR_GAME);
            connector.save(requestExecutor.getPlayer());
            gameNotStarted.setName(Side.PLAYER_ONE,waiting.getPlayer());
            return gameNotStarted;
        }else {
            if (waiting!=requestExecutor){
                Game game = gameNotStarted;
                gameNotStarted.setName(Side.PLAYER_TWO,requestExecutor.getPlayer());
                requestExecutor.getPlayer().setState(PlayerState.PLAYING_GAME);
                waiting.getPlayer().setState(PlayerState.PLAYING_GAME);
                connector.save(requestExecutor.getPlayer());
                connector.save(waiting.getPlayer());
                requestExecutor.setSide(Side.PLAYER_TWO);
                game.setStatus(GameStatus.PLAYING);
                gameNotStarted = null;
                waiting = null;
                return game;
            }else{
                return gameNotStarted;
            }
        }
    }
}
