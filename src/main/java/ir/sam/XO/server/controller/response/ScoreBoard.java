package ir.sam.XO.server.controller.response;

import ir.sam.XO.server.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreBoard extends Response{
    private final List<Map<String,Object>> players;
    private final Player currentPlayer;

    public ScoreBoard(List<Map<String, Object>> players, Player currentPlayer) {
        this.players = players;
        this.currentPlayer = currentPlayer;
    }


    @Override
    public Map<String, Object> toMap() {
        Map<String,Object> result = new HashMap<>();
        result.put("players",players);
        result.put("currentPlayer",currentPlayer);
        return result;
    }
}
