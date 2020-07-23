package ir.sam.XO.server.controller;

import ir.sam.XO.server.controller.response.Response;

public interface RequestVisitor {
    Response login(String username, String password, int mode);

    Response sendScoreBoard();

    Response startGame();

    Response sendGameState();

    Response putPiece(int x,int y);

    Response logout();
}
