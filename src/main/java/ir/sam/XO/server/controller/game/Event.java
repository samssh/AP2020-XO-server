package ir.sam.XO.server.controller.game;

import lombok.Getter;

public class Event {
    @Getter
    private final int eventNumber,x,y;
    @Getter
    private final Piece piece;

    public Event(int eventNumber, int x, int y, Piece piece) {
        this.eventNumber = eventNumber;
        this.x = x;
        this.y = y;
        this.piece = piece;
    }
}
