package ir.sam.XO.server.controller.response;

import java.util.Map;

public abstract class Response {
    public abstract Map<String, Object> toMap();

    protected String nonNull(String string) {
        if (string == null) string = "";
        return string;
    }

    public static WrongAPI getWrongApi() {
        return WrongAPI.instance;
    }

    public static Void getVoidResponse() {
        return Void.instance;
    }

    private static class WrongAPI extends Response {
        private final static WrongAPI instance = new WrongAPI();
        private final String message;

        private WrongAPI() {
            message = "wrong API";
        }

        @Override
        public Map<String, Object> toMap() {
            return Map.of("message", message);
        }
    }

    private static class Void extends Response {
        private static final Void instance = new Void();

        private Void() {
        }

        @Override
        public Map<String, Object> toMap() {
            return Map.of();
        }
    }

}
