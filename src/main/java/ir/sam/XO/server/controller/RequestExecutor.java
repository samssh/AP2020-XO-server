package ir.sam.XO.server.controller;

import ir.sam.XO.server.controller.game.*;
import ir.sam.XO.server.controller.request.Request;
import ir.sam.XO.server.controller.response.*;
import ir.sam.XO.server.controller.transmitters.ResponseSender;
import ir.sam.XO.server.database.Connector;
import ir.sam.XO.server.database.ModelLoader;
import ir.sam.XO.server.model.Player;
import ir.sam.XO.server.model.PlayerState;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestExecutor implements RequestVisitor {
    private final ResponseSender responseSender;
    private final Connector connector;
    private final ModelLoader modelLoader;
    private final GameLobby gameLobby;
    private Game game;
    private volatile boolean running;
    @Getter
    private Player player;
    @Setter
    private Side side;


    public RequestExecutor(ResponseSender responseSender, Connector connector, ModelLoader modelLoader, GameLobby gameLobby) {
        this.responseSender = responseSender;
        this.connector = connector;
        this.modelLoader = modelLoader;
        this.gameLobby = gameLobby;
        running = true;
        new Thread(this::execute).start();
    }

    private void execute() {
        while (running) {
            Request request = responseSender.getRequest();
            Response response = request.execute(this);
            responseSender.sendResponse(response);
        }
    }


    public Response login(String username, String password, int mode) {
        if (mode == 1)
            return signIn(username, password);
        if (mode == 2)
            return signUp(username, password);
        return Response.getWrongApi();
    }

    private Response signIn(String userName, String password) {
        Player fetched = connector.fetch(Player.class, userName);
        if (fetched != null) {
            if (fetched.getPassword().equals(password)) {
                this.player = fetched;
                player.setState(PlayerState.HANGING_ON_MENU);
                connector.save(player);
                return new Login(true, player.getUsername());
            } else {
                return new Login(false, "wrong password");
            }
        } else {
            return new Login(false, "username not exist");
        }
    }

    private Response signUp(String username, String password) {
        Player player = connector.fetch(Player.class, username);
        if (player == null) {
            player = new Player(username, password);
            connector.save(player);
            this.player = player;
            return new Login(true, this.player.getUsername());
        } else {
            return new Login(false, "username already exist");
        }
    }

    @Override
    public Response sendScoreBoard() {
        return new ScoreBoard(modelLoader.getPlayers().stream().map(this::turnPlayerToMap)
                .collect(Collectors.toList()), player);
    }


    private Map<String, Object> turnPlayerToMap(Player player) {
        Map<String, Object> result = new HashMap<>();
        result.put("username", player.getUsername());
        result.put("score", player.getScore());
        result.put("state", player.getState().name());
        return result;
    }

    @Override
    public Response startGame() {
        game = gameLobby.startGameRequest(this);
        return new GoTo(null, "PLAY");
    }

    @Override
    public Response sendGameState(int eventNumber) {
        if (game != null) {
            GameStatus status = game.getStatus();
            if (status == GameStatus.NOT_STARTED) {
                return new PlayDetails("waiting for opponent");
            } else {
                if (eventNumber == 0) {
                    String message = (side == game.getSideToTurn() ? "your" : "opponent") + " turn";
                    return new PlayDetails(eventNumber, message, game.getPiece(side)
                            , game.getOpponentUsername(side));
                } else if (eventNumber > 0) {
                    Event event = game.getEvent(eventNumber);
                    if (event == null) {
                        if (status!=GameStatus.PLAYING) {
                                player.setState(PlayerState.HANGING_ON_MENU);
                                connector.save(player);
                                Game game = this.game;
                                this.game = null;
                            if (status == GameStatus.ENDED) {
                                return new GoTo((side == game.getSideToTurn() ? "win" : "lose"), "MAIN_MENU");
                            }else {
                                return new GoTo("draw", "MAIN_MENU");
                            }
                        } else {
                            return Response.getVoidResponse();
                        }
                    }
                    String message = (side == game.getSideToTurn() ? "your" : "opponent") + " turn";
                    return new PlayDetails(event.getEventNumber(), event.getX(), event.getY()
                            , event.getPiece(), message);
                } else {
                    return Response.getWrongApi();
                }
            }
        }
        return Response.getVoidResponse();
    }

    @Override
    public Response putPiece(int x, int y) {
        game.putPiece(x, y, side);
        return Response.getVoidResponse();
    }

    @Override
    public Response logout() {
        running = false;
        player.setState(PlayerState.OFFLINE);
        connector.save(player);
        return Response.getVoidResponse();
    }
}
