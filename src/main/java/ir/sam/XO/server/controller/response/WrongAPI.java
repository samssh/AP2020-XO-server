package ir.sam.XO.server.controller.response;

import java.util.Map;

public class WrongAPI extends Response {
    private final String message;
    private final static WrongAPI instance = new WrongAPI();

    public static WrongAPI getInstance() {
        return instance;
    }

    private WrongAPI() {
        message = "wrong API";
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of("message",message);
    }
}
