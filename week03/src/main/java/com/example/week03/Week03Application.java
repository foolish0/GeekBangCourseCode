package com.example.week03;

import com.example.week03.client.NettyClient;
import com.example.week03.netty.NettyHttpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TreeMap;

@Slf4j
@SpringBootApplication
public class Week03Application {

    public static void main(String[] args) {
        //    public static void main(String[] args) {
        //        SpringApplication.run(Week03Application.class, args);
        //    }
        int port = 8808;

        NettyHttpServer server = new NettyHttpServer(port, "http://localhost:8881");

        try {
            server.accept();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//        Runnable server = () -> {
//            NettyHttpServer nettyHttpServer = new NettyHttpServer(port);
//            try {
//                nettyHttpServer.accept();
//            } catch (InterruptedException e) {
//                log.error("服务端启动出错: {}", e.getMessage());
//            }
//        };
//
//
//        Runnable client = () -> {
//            NettyClient nettyClient = new NettyClient("http://localhost", port);
//            try {
//                nettyClient.access();
//            } catch (InterruptedException e) {
//                log.error("客户端异常:{}", e.getMessage());
//            }
//        };
//
//        Thread serverThread = new Thread(server);
//        log.info("启动客户端.....");
//        serverThread.start();
//
//        Thread clientThread = new Thread(client);
//        log.info("启动客户端.....");
//        clientThread.start();
    }

}
