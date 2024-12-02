import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioServer {

    public static void main(String[] args) throws IOException {
        // Create a selector
        Selector selector = Selector.open();

        // Create a server socket channel
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(8080));
        serverChannel.configureBlocking(false);

        // Register the server channel with the selector for accepting connections
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server is listening on port 8080...");

        while (true) {
            // Wait for events
            selector.select();

            // Iterate over the selected keys
            for (SelectionKey key : selector.selectedKeys()) {
                if (key.isAcceptable()) {
                    // Accept the connection
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel clientChannel = ssc.accept();
                    if (clientChannel != null) {
                        System.out.println("Accepted connection from " + clientChannel.getRemoteAddress());
                        // Close the connection immediately
                        clientChannel.close();
                    }
                }
            }
            // Clear the selected keys
            selector.selectedKeys().clear();
        }
    }
}