package ir.sam.XO.server.controller.response;

import ir.sam.XO.server.controller.game.Piece;

import java.util.Map;

public class PlayDetails extends Response {
    private final Piece piece;
    private final String message;
    private final String opponentUsername;
    private final Piece[][] board;

    public PlayDetails(Piece piece, String message, String opponentUsername, Piece[][] board) {
        this.piece = piece;
        this.message = message;
        this.opponentUsername = opponentUsername;
        this.board = board;
    }

    public PlayDetails(String message) {
        this(null, message, null, null);
    }


    @Override
    public Map<String, Object> toMap() {
        return Map.of("piece", piece, "message", message
                , "opponentUsername", opponentUsername, "board", board);
    }
}
