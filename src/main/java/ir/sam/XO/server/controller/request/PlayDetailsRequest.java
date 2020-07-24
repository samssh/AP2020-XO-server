package ir.sam.XO.server.controller.request;

import ir.sam.XO.server.controller.RequestVisitor;
import ir.sam.XO.server.controller.response.Response;
import ir.sam.XO.server.controller.transmitters.WrongApiException;

import java.util.Map;

public class PlayDetailsRequest extends Request {
    private final int eventNumber;

    public PlayDetailsRequest(Map<String, Object> map) throws WrongApiException {
        try {
            eventNumber = (int) map.get("eventNumber");
        } catch (ClassCastException e) {
            throw new WrongApiException(e);
        }
    }

    @Override
    public Response execute(RequestVisitor requestVisitor) {
        return requestVisitor.sendGameState(eventNumber);
    }
}
