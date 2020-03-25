package org.example.socket.server;

import org.example.socket.server.util.Util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws IOException, InterruptedException {

        ServerSocket serverSocket = new ServerSocket(9088);
        ExecutorService workers = Executors.newCachedThreadPool();
        while (true) {
            System.out.println("Waiting client sockets ...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("New connection established");
            Util.addNewConnection(clientSocket);
            Util.handleClientLogin(clientSocket, workers);
        }
    }


}
