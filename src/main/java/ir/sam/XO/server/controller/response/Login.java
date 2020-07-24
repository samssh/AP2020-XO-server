package ir.sam.XO.server.controller.response;

import lombok.Getter;

import java.util.Map;

public class Login extends Response {
    @Getter
    private final boolean success;
    @Getter
    private final String message;

    public Login(boolean success, String message) {
        this.success = success;
        this.message = nonNull(message);
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of("success", success, "message", message);
    }
}
