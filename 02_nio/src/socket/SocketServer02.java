package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static socket.Service.service;

/**
 * @author lizhenjiang
 */
public class SocketServer02 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8882);
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                service(socket);
            }).start();
        }
    }
}
