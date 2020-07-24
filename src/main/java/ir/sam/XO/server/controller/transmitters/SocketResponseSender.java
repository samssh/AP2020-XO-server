package ir.sam.XO.server.controller.transmitters;

import com.google.gson.*;
import ir.sam.XO.server.controller.request.Request;
import ir.sam.XO.server.controller.response.Login;
import ir.sam.XO.server.controller.response.Response;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.*;

public class SocketResponseSender implements ResponseSender, JsonDeserializer<Map<String, Object>> {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private final Scanner scanner;
    private final PrintStream printStream;
    private String token = null;

    public SocketResponseSender(Socket socket) throws IOException {
        scanner = new Scanner(socket.getInputStream());
        printStream = new PrintStream(socket.getOutputStream(),true);
    }

    @Override
    public Request getRequest() {
        Optional<Request> optionalRequest;
        do {
            String json = scanner.nextLine();
            optionalRequest = toRequest(json);
        } while (optionalRequest.isEmpty());
        return optionalRequest.get();
    }

    private Optional<Request> toRequest(String json) {
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(Map.class, this).create();
            Map<?, ?> map = gson.fromJson(json, Map.class);
            if (token != null) {
                if (!token.equals(map.get("token"))) throw new ClassCastException();
            }
            Class<? extends Request> requestClass = Request.requestType.get(map.get("type"));
            Constructor<? extends Request> constructor = requestClass.getConstructor(Map.class);
            return Optional.of(constructor.newInstance((Map<?, ?>) map.get("body")));
        } catch (JsonSyntaxException | ClassCastException | InvocationTargetException e) {
            e.printStackTrace();
            sendResponse(Response.getWrongApi());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void sendResponse(Response response) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", response.getClass().getSimpleName());
        if (response instanceof Login && ((Login) response).isSuccess()) {
            token = generateNewToken();
            map.put("token", token);
        }
        map.put("body", response.toMap());
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(map);
        printStream.println(json);
        printStream.flush();
    }

    public String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return (Map<String, Object>) read(json);
    }

    public Object read(JsonElement in) {
        if (in.isJsonArray()) {
            List<Object> list = new ArrayList<>();
            JsonArray arr = in.getAsJsonArray();
            for (JsonElement anArr : arr) {
                list.add(read(anArr));
            }
            return list;
        } else if (in.isJsonObject()) {
            Map<String, Object> map = new HashMap<>();
            JsonObject obj = in.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entitySet = obj.entrySet();
            for (Map.Entry<String, JsonElement> entry : entitySet) {
                map.put(entry.getKey(), read(entry.getValue()));
            }
            return map;
        } else if (in.isJsonPrimitive()) {
            JsonPrimitive prim = in.getAsJsonPrimitive();
            if (prim.isBoolean()) {
                return prim.getAsBoolean();
            } else if (prim.isString()) {
                return prim.getAsString();
            } else if (prim.isNumber()) {
                Number num = prim.getAsNumber();
                return num.intValue();
            }
        }
        return null;
    }
}
