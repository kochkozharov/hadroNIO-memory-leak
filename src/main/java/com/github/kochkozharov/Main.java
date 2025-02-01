package com.github.kochkozharov;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Provide 2 args: server/client/test_client and port number");
        }

        String mode = args[0];
        int portNumber;

        try {
            portNumber = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Port number must be a valid integer");
        }

        if (mode.equalsIgnoreCase("server")) {
            new NioServer().start(portNumber);
        } else if (mode.equalsIgnoreCase("client")) {
            try (var scanner = new Scanner("Test")) {
                new NioClient().start(portNumber, scanner);
            }
        } else if (mode.equalsIgnoreCase("test_client")) {
            for (int i = 0; i < 10000; ++i) {
                System.out.println("Connection " + i);
                try (var scanner = new Scanner("Test")) {
                    new NioClient().start(portNumber, scanner);
                }
            }
        } else {
            throw new IllegalArgumentException("Unknown mode: " + mode);
        }
    }
}
