package ir.sam.XO.server.database;

import ir.sam.XO.server.model.Player;
import ir.sam.XO.server.util.Loop;

import java.util.ArrayList;
import java.util.List;

public class ModelLoader {
    private List<Player> players;
    private final Loop worker;
    private final Object lock;
    private final Connector connector;

    public ModelLoader(Connector connector) {
        this.connector = connector;
        players = connector.fetchAll(Player.class);
        worker = new Loop(1, this::update);
        worker.start();
        lock = new Object();
    }

    private void update() {
        synchronized (lock) {
            players = connector.executeHQL("from Player order by score desc",Player.class);
        }
    }

    public List<Player> getPlayers() {
        synchronized (lock) {
            return new ArrayList<>(players);
        }
    }

    public void stop(){
        worker.stop();
    }
}