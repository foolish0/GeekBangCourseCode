package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static socket.Service.service;

/**
 * @author lizhenjiang
 */
public class SocketServer01 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8881);
        while (true) {
            Socket socket = serverSocket.accept();
            service(socket);
        }
    }
}
