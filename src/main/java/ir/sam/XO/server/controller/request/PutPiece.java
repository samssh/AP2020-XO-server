package ir.sam.XO.server.controller.request;

import ir.sam.XO.server.controller.RequestVisitor;
import ir.sam.XO.server.controller.response.Response;
import ir.sam.XO.server.controller.transmitters.WrongApiException;

import java.util.Map;

public class PutPiece extends Request {
    private final int x, y;

    public PutPiece(Map<String, Object> map) throws WrongApiException {
        super(map);
        try {
            y = (int) map.get("y");
            x = (int) map.get("x");
        } catch (ClassCastException e) {
            throw new WrongApiException(e);
        }
    }

    @Override
    public Response execute(RequestVisitor requestVisitor) {
        return requestVisitor.putPiece(x, y);
    }
}
