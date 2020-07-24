package ir.sam.XO.server.controller.response;

import ir.sam.XO.server.controller.game.Piece;

import java.util.Map;

public class PlayDetails extends Response {
    private final int eventNumber;
    private final int x, y;
    private final String  piece;
    private final String message;
    private final String playerPiece;
    private final String opponentUsername;

    public PlayDetails(int eventNumber, int x, int y, Piece piece, String message, Piece playerPiece
            , String opponentUsername) {
        this.eventNumber = eventNumber;
        this.x = x;
        this.y = y;
        this.piece = pieceToString(piece);
        this.message = nonNull(message);
        this.playerPiece = pieceToString(playerPiece);
        this.opponentUsername = nonNull(opponentUsername);
    }

    private String pieceToString(Piece piece){
        return piece==null?"":piece.toString();
    }

    public PlayDetails(int eventNumber, int x, int y, Piece piece, String message) {
        this(eventNumber, x, y, piece, message, null, null);
    }

    public PlayDetails(int eventNumber, String message, Piece playerPiece, String opponentUsername) {
        this(eventNumber,-1,-1,null,message,playerPiece,opponentUsername);
    }

    public PlayDetails(String message){
        this(-1,message,null,null);
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of("eventNumber", eventNumber, "x", x, "y", y, "piece", piece
                , "message", message, "playerPiece", playerPiece
                , "opponentUsername", opponentUsername);
    }
}