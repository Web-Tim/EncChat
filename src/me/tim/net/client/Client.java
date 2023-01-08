package me.tim.net.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private final Socket socket;

    public Client(String address, int port) {
        try {
            this.socket = new Socket(address, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect() throws IOException {
        ServerConnection connection = new ServerConnection(this.socket);
        BufferedReader keys = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);

        new Thread(connection).start();
        boolean exit = false;
        while (!exit) {
            System.out.print("> ");
            String cmd = keys.readLine();

            switch (cmd.toLowerCase()) {
                case "exit":
                    exit = true;
                    break;
                case "help":
                    System.out.println("Command Help: ");
                    System.out.println("        [any message] -> send a broadcast message to every user.");
                    System.out.println("        exit -> properly exit.");
                    System.out.println("        help -> see help.");
                    System.out.println("        setuser [name] -> set your username.");
                    System.out.println("        getuser (-random) -> get/generate your username.");
                    break;
                default:
                    out.println(cmd);
                    break;
            }
        }
        this.socket.close();
        System.exit(0);
    }
}
