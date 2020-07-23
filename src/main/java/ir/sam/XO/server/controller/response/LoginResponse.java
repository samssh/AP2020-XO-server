package ir.sam.XO.server.controller.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class LoginResponse extends Response {
    @Getter
    private final boolean success;
    @Getter
    private final String message;
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String,Object> result = new HashMap<>(2);
        result.put("success",success);
        result.put("message",message);
        return result;
    }
}
