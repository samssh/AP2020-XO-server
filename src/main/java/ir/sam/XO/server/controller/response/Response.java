package ir.sam.XO.server.controller.response;

import java.util.Map;

public abstract class Response {
    public abstract Map<String, Object> toMap();

    public static WrongAPI getWrongApi() {
        return WrongAPI.instance;
    }


    public static VoidResponse getVoidResponse() {
        return VoidResponse.instance;
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

    private static class VoidResponse extends Response {
        private static final VoidResponse instance = new VoidResponse();

        private VoidResponse() {
        }

        @Override
        public Map<String, Object> toMap() {
            return Map.of();
        }
    }

}
