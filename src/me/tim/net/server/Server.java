package me.tim.net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ServerSocket socket;
    private final ArrayList<ClientHandler> clients;
    private final ExecutorService pool;

    public Server(int port, int maxClients, int timeout) {
        System.out.println("Launching server using following options:");
        System.out.println("            maxClients: " + maxClients);
        System.out.println("            timeout (s): " + timeout);
        System.out.println(" ");
        this.clients = new ArrayList<>();
        this.pool = Executors.newFixedThreadPool(maxClients);

        try {
            this.socket = new ServerSocket(port);
            this.socket.setSoTimeout(timeout * 1000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() throws IOException {
        while (true) {
            System.out.println("Waiting for connections!");
            Socket clientSocket = this.socket.accept();
            System.out.println("Client connected! > " + clientSocket.getRemoteSocketAddress().toString());
            System.out.println(" ");
            ClientHandler handler = new ClientHandler(clientSocket, clients);
            this.clients.add(handler);
            pool.execute(handler);
        }
    }
}
