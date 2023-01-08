package me.tim.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private static final String[] names = {
            "Kalia",
            "Malka",
            "Jefferson",
            "Dane",
            "Kari",
            "Lizbeth",
            "Lucas",
            "Ernest",
            "Elian",
            "Maryjane",
            "Isidro",
            "Abraham",
            "Graham",
            "Alicia",
            "Nicolle",
            "Sussybaka69420"
    };

    private final Socket client;
    private final BufferedReader in;
    private final PrintWriter out;
    private final ArrayList<ClientHandler> clients;
    private String username;

    public ClientHandler(Socket client, ArrayList<ClientHandler> clients) throws IOException {
        this.client = client;
        this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        this.out = new PrintWriter(this.client.getOutputStream(), true);
        this.clients = clients;
        this.username = this.client.getRemoteSocketAddress().toString();
    }

    private void broadcastMessage(String message) {
        for (ClientHandler handler : this.clients) {
            if (handler == this) continue;

            handler.out.println(this.username + " > " + message);
        }
    }

    private void setUsername(String username) {
        if (!username.isEmpty()) {
            System.out.println(" ");
            System.out.println("Somebody has been renamed!");
            System.out.println("Original: " + this.username);
            System.out.println("New Username: " + username);
            System.out.println(" ");
            this.username = username;
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = this.in.readLine();
                if (msg == null) break;
                if (msg.contains("setuser")) {
                    String newUser = msg.split(" ")[1];
                    this.setUsername(newUser);
                } else if (msg.contains("getuser")) {
                    String[] args = msg.split(" ");
                    if (args.length > 1) {
                        if (!args[1].isEmpty() && args[1].equalsIgnoreCase("-random")) {
                            this.setUsername(names[(int) (Math.random() * 16)]);
                            this.out.println("You're lucky, there was a chance of 6.25% for you to get this name!");
                            this.out.println("Your new Username: " + this.username);
                        }
                    } else {
                        this.out.println(this.username);
                    }
                } else {
                    broadcastMessage(msg);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.out.close();
            try {
                this.in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
