package socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

/**
 * @author lizhenjiang
 */
public class Service {
    public static void service(Socket socket, String body) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
//            String body = "hello,nio";
            printWriter.println("Content-Length:" + body.getBytes().length);
            printWriter.println();
//            body = body.toUpperCase(Locale.getDefault());
//            String[] strings = body.split(",");
//            for (String str : strings) {
//                printWriter.write(str);
//            }
            printWriter.write(body);
            printWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
