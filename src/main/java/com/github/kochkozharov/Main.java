package com.github.kochkozharov;
import java.util.Scanner;

public class Main {

    final static int PORT_NUMBER = 12345;

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Provide 1 arg: server or client");
        }
            if (args[0].equalsIgnoreCase("server")) {
                new NioServer().start(PORT_NUMBER);
            } else if (args[0].equalsIgnoreCase("client")) {
                try (var scanner = new Scanner("Test")) {
                    new NioClient().start(PORT_NUMBER, scanner);
                }
            }
            else if (args[0].equalsIgnoreCase("test_client")) {
                for (int i = 0; i < 3000; ++i) {
                    System.out.println("Connection " + i);
                    try (var scanner = new Scanner("Test")) {
                        new NioClient().start(PORT_NUMBER, scanner);
                    }
                }
            }
            else {
                throw new IllegalArgumentException("Unknown arg");
            }
    }
}
