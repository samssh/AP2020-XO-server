package ir.sam.XO.server.controller.request;

import ir.sam.XO.server.controller.RequestVisitor;
import ir.sam.XO.server.controller.response.Response;

import java.util.Map;

public class StartGame extends Request {
    public StartGame(Map<String, Object> map) {
    }

    @Override
    public Response execute(RequestVisitor requestVisitor) {
        return requestVisitor.startGame();
    }
}
