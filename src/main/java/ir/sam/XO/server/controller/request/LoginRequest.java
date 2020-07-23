package ir.sam.XO.server.controller.request;

import ir.sam.XO.server.controller.RequestVisitor;
import ir.sam.XO.server.controller.response.Response;
import ir.sam.XO.server.controller.transmitters.WrongApiException;
import lombok.Getter;

import java.util.Map;

public class LoginRequest extends Request {
    @Getter
    private final String userName, password;
    @Getter
    private final int mode;

    public LoginRequest(Map<String, Object> map) throws WrongApiException {
        try {
            this.userName = (String) map.get("userName");
            this.password = (String) map.get("password");
            this.mode = (int) map.get("mode");
        } catch (ClassCastException exception) {
            throw new WrongApiException(exception);
        }
    }

    @Override
    public Response execute(RequestVisitor requestVisitor) {
        return requestVisitor.login(userName,password,mode);
    }
}