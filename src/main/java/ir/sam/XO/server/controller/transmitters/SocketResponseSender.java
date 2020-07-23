package ir.sam.XO.server.controller.transmitters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import ir.sam.XO.server.controller.request.Request;
import ir.sam.XO.server.controller.response.Login;
import ir.sam.XO.server.controller.response.Response;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.*;

public class SocketResponseSender implements ResponseSender {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private final Scanner scanner;
    private final PrintStream printStream;
    private String token;

    public SocketResponseSender(Socket socket) throws IOException {
        scanner = new Scanner(socket.getInputStream());
        printStream = new PrintStream(socket.getOutputStream());
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
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            Map<?, ?> map = gson.fromJson(json, Map.class);
            if (token != null) {
                if (!token.equals(map.get("token"))) sendResponse(Response.getWrongApi()); // api
            }
            Class<? extends Request> requestClass = Request.requestType.get(map.get("type"));
            Constructor<? extends Request> constructor = requestClass.getConstructor(Map.class);
            return Optional.of(constructor.newInstance((Map<?, ?>) map.get("request")));
        } catch (JsonSyntaxException | ClassCastException | InvocationTargetException e) {
            sendResponse(Response.getWrongApi()); // api
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void sendResponse(Response response) {
        Map<String, Object> map = new HashMap<>();
        map.put("type",response.getClass().getSimpleName());
        if (response instanceof Login && ((Login) response).isSuccess()) {
            token = generateNewToken();
            map.put("token", token);
        }
        map.put("Body", response.toMap());
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(map);
        printStream.println(json);
    }


    public String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
