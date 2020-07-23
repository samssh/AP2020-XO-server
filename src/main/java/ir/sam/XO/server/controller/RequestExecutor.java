package ir.sam.XO.server.controller;

import ir.sam.XO.server.controller.game.Side;
import ir.sam.XO.server.controller.request.Request;
import ir.sam.XO.server.controller.response.LoginResponse;
import ir.sam.XO.server.controller.response.Response;
import ir.sam.XO.server.controller.response.ScoreBoard;
import ir.sam.XO.server.controller.response.WrongAPI;
import ir.sam.XO.server.controller.transmitters.ResponseSender;
import ir.sam.XO.server.database.Connector;
import ir.sam.XO.server.database.ModelLoader;
import ir.sam.XO.server.model.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestExecutor implements RequestVisitor {
    private final ResponseSender responseSender;
    private final Connector connector;
    private final ModelLoader modelLoader;
    private volatile boolean running;
    @Getter
    private Player player;
    @Setter
    private Side side;


    public RequestExecutor(ResponseSender responseSender, Connector connector, ModelLoader modelLoader) {
        this.responseSender = responseSender;
        this.connector = connector;
        this.modelLoader = modelLoader;
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
        return WrongAPI.getInstance();
    }

    private Response signIn(String userName, String password) {
        Player fetched = connector.fetch(Player.class, userName);
        if (fetched != null) {
            if (fetched.getPassword().equals(password)) {
                this.player = fetched;
                return new LoginResponse(true, player.getUsername());
            } else {
                return new LoginResponse(false, "wrong password");
            }
        } else {
            return new LoginResponse(false, "username not exist");
        }
    }

    private Response signUp(String username, String password) {
        Player player = connector.fetch(Player.class, username);
        if (player == null) {
            player = new Player(username, password);
            connector.save(player);
            this.player = player;
            return new LoginResponse(true, this.player.getUsername());
        } else {
            return new LoginResponse(false, "username already exist");
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
        result.put("state", player.getState());
        return result;
    }

    @Override
    public Response startGame() {
        return null;
    }


}
