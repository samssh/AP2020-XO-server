package ir.sam.XO.server.controller;

import ir.sam.XO.server.controller.game.GameLobby;
import ir.sam.XO.server.controller.transmitters.ResponseSender;
import ir.sam.XO.server.controller.transmitters.SocketResponseSender;
import ir.sam.XO.server.database.Connector;
import ir.sam.XO.server.database.ModelLoader;
import ir.sam.XO.server.util.Config;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketManager {
    private int port = 8000;
    private String address="";
    private final ServerSocket serverSocket;
    private final Connector connector;
    private final ModelLoader modelLoader;
    private final GameLobby gameLobby;
    private volatile boolean running;

    public SocketManager(Config config) throws IOException {
        config.getProperty(Integer.class,"port").ifPresent(integer -> port = integer);
        config.getProperty(String.class,"address").ifPresent(s -> address = s);
        InetSocketAddress socketAddress = new InetSocketAddress(address,port);
        serverSocket = new ServerSocket(port);
        serverSocket.bind(socketAddress);
        connector = new Connector();
        modelLoader = new ModelLoader(connector);
        gameLobby = new GameLobby(config);
        accept();
    }

    private void accept(){
        while (running){
            try {
                Socket socket = serverSocket.accept();
                ResponseSender responseSender = new SocketResponseSender(socket);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getOrders(){

    }
}
