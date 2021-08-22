package com.example.week03.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author lizhenjiang
 */
@Slf4j
public class NettyClient {
    private final String ip;
    private final int port;

    public NettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void access() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new HttpResponseDecoder());
                        socketChannel.pipeline().addLast(new HttpRequestEncoder());
                        socketChannel.pipeline().addLast(new ClientHandler());
                    }
                });
        ChannelFuture future = bootstrap.connect(ip, port).sync();

        if (future.isSuccess()) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
//        String request = "客户端请求！";
//        future.channel().writeAndFlush(Unpooled.copiedBuffer(request.getBytes(StandardCharsets.UTF_8)));

        future.channel().closeFuture().sync();
    }

    public static void main(String[] args) {
        int port = 8808;
        NettyClient nettyClient = new NettyClient("127.0.0.1", port);
        try {
            nettyClient.access();
        } catch (InterruptedException e) {
            log.error("客户端异常:{}", e.getMessage());
        }
    }

}
