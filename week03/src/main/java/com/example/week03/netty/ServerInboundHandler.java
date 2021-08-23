package com.example.week03.netty;

import com.example.week03.filter.RequestFilter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;

/**
 * @author lizhenjiang
 */
@Slf4j
public class ServerInboundHandler extends ChannelInboundHandlerAdapter {
    private String url;
    private ServerOutboundHandler outboundHandler;
    private RequestFilter filter = new RequestFilter() {
        @Override
        public void filter(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
            fullHttpRequest.headers().set("Time", "Today");
        }
    };


    public ServerInboundHandler(String url) {
        this.url = url;
        outboundHandler = new ServerOutboundHandler(url);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.write("服务端收到消息！");
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
//        String url = "http://localhost:8808";
//        url = url + request.uri();
//        HttpGet httpGet = new HttpGet(url);
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        httpClient.execute(httpGet);

        outboundHandler.handle(request, ctx, filter);

    }

    private void handlerDemo(FullHttpRequest request, ChannelHandlerContext ctx, String body) {
        FullHttpResponse response = null;
        try {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(body.getBytes(StandardCharsets.UTF_8)));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());
        } catch (Exception e) {
            System.out.println("处理出错：" + e.getMessage());
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT);
        } finally {
            if (request != null) {
                if (!HttpUtil.isKeepAlive(request)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(HttpHeaderNames.CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
                ctx.flush();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
