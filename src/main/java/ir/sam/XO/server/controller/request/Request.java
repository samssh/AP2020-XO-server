package ir.sam.XO.server.controller.request;

import ir.sam.XO.server.controller.RequestVisitor;
import ir.sam.XO.server.controller.response.Response;

import java.util.Map;

public abstract class Request {
    public static final Map<Object, Class<? extends Request>> requestType =
            Map.of("login", LoginRequest.class
                    , "scoreBoard", ScoreBoardRequest.class);

    public abstract Response execute(RequestVisitor requestVisitor);
}