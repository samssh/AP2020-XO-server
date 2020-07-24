package ir.sam.XO.server;

import ir.sam.XO.server.controller.SocketManager;
import ir.sam.XO.server.util.Config;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new SocketManager(new Config("./src/main/resources/server.properties"));
    }
}
