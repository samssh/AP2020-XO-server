package ir.sam.XO.server.controller.game;

public enum  Side {
    PLAYER_ONE{
        @Override
        Side getOther() {
            return PLAYER_TWO;
        }

        @Override
        int getIndex() {
            return 0;
        }

    },PLAYER_TWO{
        @Override
        Side getOther() {
            return PLAYER_ONE;
        }

        @Override
        int getIndex() {
            return 1;
        }
    };

    abstract Side getOther();

    abstract int getIndex();

}
