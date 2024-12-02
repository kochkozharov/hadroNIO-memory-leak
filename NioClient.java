import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class NioClient {

    public static void main(String[] args) throws IOException {
        // Create a selector
        Selector selector = Selector.open();

        // Number of times to connect
        int connectionCount = 1000;

        for (int i = 0; i < connectionCount; i++) {
            // Create a socket channel
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);

            // Connect to the server
            socketChannel.connect(new InetSocketAddress("server", 8080));

            // Register the channel with the selector for connecting
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            // Wait for the connection to complete
            selector.select();

            // Iterate over the selected keys
            for (SelectionKey key : selector.selectedKeys()) {
                if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    if (channel.finishConnect()) {
                        System.out.println("Connected to server");
                        // Perform operations if needed
                        // Close the channel
                        channel.close();
                    } else {
                        System.err.println("Connection failed");
                    }
                }
            }
            // Clear the selected keys
            selector.selectedKeys().clear();

            // Optional: add a small delay between connections
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Close the selector
        selector.close();
    }
}