package ir.sam.XO.server.controller.request;

import ir.sam.XO.server.controller.RequestVisitor;
import ir.sam.XO.server.controller.response.Response;

public class StartGame extends Request{
    @Override
    public Response execute(RequestVisitor requestVisitor) {
        return requestVisitor.startGame();
    }
}
