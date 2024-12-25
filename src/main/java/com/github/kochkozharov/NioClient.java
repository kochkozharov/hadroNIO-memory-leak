package com.github.kochkozharov;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NioClient {

    public void start(final int portNumber, final Scanner scanner) {
        try (var serverChannel = SocketChannel.open();) {
            serverChannel.connect(new InetSocketAddress("server", portNumber));
            serverChannel.configureBlocking(true);
            System.out.println("Connection established!");
            var buffer = ByteBuffer.allocate(1024);
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                if (line.equalsIgnoreCase("quit")) {
                    break;
                }
                line += System.lineSeparator();
                buffer.clear().put(line.getBytes()).flip();
                while (buffer.hasRemaining()) {
                    serverChannel.write(buffer);
                }
                buffer.clear();

                var bytesRead = serverChannel.read(buffer);
                if (bytesRead > 0) {
                    buffer.flip();
                    var readData = new byte[bytesRead];
                    buffer.get(readData);
                    System.out.print("SERVER: " + new String(readData));
                    buffer.clear();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}