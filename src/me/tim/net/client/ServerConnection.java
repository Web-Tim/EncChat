package me.tim.net.client;

import java.io.*;
import java.net.Socket;

public class ServerConnection implements Runnable {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public ServerConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String response = this.in.readLine();
                if (response == null) break;

                System.out.println(response);
                System.out.print("> ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
