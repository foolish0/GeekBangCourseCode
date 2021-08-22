package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static socket.Service.service;

/**
 * @author lizhenjiang
 */
public class SocketServer03 {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() + 2
        );
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor();
        final ServerSocket serverSocket = new ServerSocket(8883);
        while (true) {
            final Socket socket = serverSocket.accept();
            executorService.execute(() -> service(socket, "SocketServer03"));
        }
    }
}
