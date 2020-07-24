package ir.sam.XO.server.controller.response;

import java.util.Map;

public class GoTo extends Response{
    private final String message;
    private final String panelName;

    public GoTo(String message, String panelName) {
        this.message = nonNull(message);
        this.panelName = nonNull(panelName);
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of("message",message,"panelName", panelName);
    }
}
