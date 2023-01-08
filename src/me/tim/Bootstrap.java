package me.tim;

import me.tim.net.client.Client;
import me.tim.net.server.Server;

import java.io.IOException;

public class Bootstrap {
    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "-server":
                    int maxClients = 5;
                    int timeout = 100;
                    if (args.length > 1 && !args[1].isEmpty())
                    {
                        maxClients = Integer.parseInt(args[1]);
                    }

                    if (args.length > 2 && !args[2].isEmpty())
                    {
                        timeout = Integer.parseInt(args[2]);

                        if (timeout < 10) {
                            System.err.println("Timeout setting can't be that low!");
                            System.err.println("Falling back to default setting of 100 (s).");
                            System.err.println(" ");
                            timeout = 100;
                        }
                    }

                    new Server(1337, maxClients, timeout).start();
                    break;
                case "-help":
                    System.out.println("Arguments: ");
                    System.out.println("        -client (or empty) -> Launch as a client.");
                    System.out.println("        -server [maximal clients] [timeout] -> Launch as a server.");
                    System.out.println("        -help -> See command help.");
                    break;
                case "-client":
                    new Client("127.0.0.1", 1337).connect();
                    break;
            }
        } else {
            new Client("127.0.0.1", 1337).connect();
        }
    }
}
