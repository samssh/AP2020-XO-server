package ir.sam.XO.server.controller.game;

import ir.sam.XO.server.database.Connector;
import ir.sam.XO.server.model.Player;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

import java.util.EnumMap;
import java.util.LinkedList;
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
    private final LinkedList<Event> events;
    private final Player[] players;
    private final Connector connector;

    public Game(int width, int height, int winningRace, Connector connector) {
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
        events = new LinkedList<>();
        players = new Player[2];
        this.connector = connector;
    }

    void setName(Side side, Player player) {
        players[side.getIndex()] = player;
    }


    public synchronized void putPiece(int i, int j, Side side) {
        if (side != gameState.getSideToTurn())
            return;
        if (-1 >= i || i >= width || -1 >= j || j >= height) {
            return;
        }
        if (gameState.getPiece(i,j)!=Piece.EMPTY)
            return;
        gameState.putPiece(i, j, side);
        events.add(new Event(events.size() + 1, i, j, gameState.getPlayerPiece()[side.getIndex()]));
        check(i, j);
        checkDraw();
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
                    players[gameState.getSideToTurn().getIndex()].win();
                    connector.save(players[gameState.getSideToTurn().getIndex()]);
                    players[gameState.getSideToTurn().getOther().getIndex()].lose();
                    connector.save(players[gameState.getSideToTurn().getOther().getIndex()]);
                    status = GameStatus.ENDED;
                }
            }
        }
    }

    private void checkDraw(){
        Map<Piece,Object> map = new EnumMap<>(Piece.class);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map.put(gameState.getPiece(i,j),value);
            }
        }
        if (!map.containsKey(Piece.EMPTY)){
            players[gameState.getSideToTurn().getIndex()].draw();
            connector.save(players[gameState.getSideToTurn().getIndex()]);
            players[gameState.getSideToTurn().getOther().getIndex()].draw();
            connector.save(players[gameState.getSideToTurn().getOther().getIndex()]);
            status = GameStatus.DRAW;
        }
    }

    public synchronized Event getEvent(int eventNumber){
        if (eventNumber>events.size()){
            return null;
        }
        return events.get(eventNumber-1);
    }

    public synchronized Side getSideToTurn() {
        return gameState.getSideToTurn();
    }

    public Piece getPiece(Side side) {
        return gameState.getPlayerPiece(side);
    }

    public String getOpponentUsername(Side side) {
        return players[side.getOther().getIndex()].getUsername();
    }
}
