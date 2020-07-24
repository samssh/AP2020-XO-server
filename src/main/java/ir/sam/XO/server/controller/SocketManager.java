package ir.sam.XO.server.controller;

import ir.sam.XO.server.controller.game.GameLobby;
import ir.sam.XO.server.controller.transmitters.ResponseSender;
import ir.sam.XO.server.controller.transmitters.SocketResponseSender;
import ir.sam.XO.server.database.Connector;
import ir.sam.XO.server.database.ModelLoader;
import ir.sam.XO.server.util.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class SocketManager {
    private final ServerSocket serverSocket;
    private final Connector connector;
    private final ModelLoader modelLoader;
    private final GameLobby gameLobby;
    private volatile boolean running;

    public SocketManager(Config config) throws IOException {
        int port = config.getProperty(Integer.class, "port").orElse(8000);
        serverSocket = new ServerSocket(port);
        connector = new Connector();
        modelLoader = new ModelLoader(connector);
        gameLobby = new GameLobby(config, connector);
        running = true;
        new Thread(this::getOrders).start();
        accept();
    }

    private void accept() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                ResponseSender responseSender = new SocketResponseSender(socket);
                new RequestExecutor(responseSender, connector, modelLoader, gameLobby);
            } catch (IOException ignore) {
            }
        }
    }

    private void getOrders() {
        Scanner scanner = new Scanner(System.in);
        while (running) {
            System.out.println("type exit to shutdown server. make sure no client connected");
            if ("exit".equals(scanner.nextLine())) {
                running = false;
                connector.close();
                modelLoader.stop();
                try {
                    serverSocket.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
}
