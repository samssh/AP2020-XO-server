package ir.sam.XO.server.controller.transmitters;

import ir.sam.XO.server.controller.request.Request;
import ir.sam.XO.server.controller.response.Response;

public interface ResponseSender {
    Request getRequest();

    void sendResponse(Response response);
}
