import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class NioClient {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        int connectionCount = 1000;

        for (int i = 0; i < connectionCount; i++) {
            System.out.println("Socket " + i);
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);

            socketChannel.connect(new InetSocketAddress("server", 8080));

            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            selector.select();

            for (SelectionKey key : selector.selectedKeys()) {
                if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    if (channel.finishConnect()) {
                        System.out.println("Connected to server");
                        // Close the channel
                        channel.close();
                    } else {
                        System.err.println("Connection failed");
                    }
                }
            }
            selector.selectedKeys().clear();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        selector.close();
        System.out.println("Done");
    }
}