package ir.sam.XO.server.controller.request;

import ir.sam.XO.server.controller.RequestVisitor;
import ir.sam.XO.server.controller.response.Response;
import ir.sam.XO.server.controller.transmitters.WrongApiException;

import java.util.Map;

public abstract class Request {
    public static final Map<Object, Class<? extends Request>> requestType =
            Map.of("Login", LoginRequest.class
                    , "ScoreBoard", ScoreBoardRequest.class
                    , "StartGame", StartGame.class
                    , "PlayDetails", PlayDetailsRequest.class
                    , "PutPiece", PutPiece.class
                    ,"Logout",LogoutRequest.class);

    @SuppressWarnings("RedundantThrows")
    public Request(Map<String, Object> map) throws WrongApiException {
    }

    public abstract Response execute(RequestVisitor requestVisitor);
}