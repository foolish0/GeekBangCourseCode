package com.example.week03.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 处理服务端返回的数据
 * @author lizhenjiang
 */
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("客户端请求");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端收到消息：" + msg.toString());

        ByteBuf buf = (ByteBuf) msg;
        byte[] buffer = new byte[buf.readableBytes()];
        buf.readBytes(buffer);
        String message = new String(buffer, StandardCharsets.UTF_8);
        log.info("Client,接收到服务端发来的消息:" + message);

        ByteBuf byteBuf = Unpooled.copiedBuffer("你好，server", StandardCharsets.UTF_8);
        ctx.writeAndFlush(byteBuf);

//        try {
//            FullHttpResponse response = (FullHttpResponse) msg;
//            log.info("\n######response:#####\n{}", response);
//            if (response.status() == HttpResponseStatus.OK) {
//                log.info("请求成功：{}", response);
//            } else {
//                log.info("请求失败!");
//            }
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.write("客户端收到消息！");
        ctx.flush();;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
