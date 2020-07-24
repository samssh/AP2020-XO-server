package ir.sam.XO.server.controller.request;

import ir.sam.XO.server.controller.RequestVisitor;
import ir.sam.XO.server.controller.response.Response;
import ir.sam.XO.server.controller.transmitters.WrongApiException;

import java.util.Map;

public class LogoutRequest extends Request{
    public LogoutRequest(Map<String, Object> map){

    }

    @Override
    public Response execute(RequestVisitor requestVisitor) {
        return requestVisitor.logout();
    }
}
