package com.example.week03.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface RequestFilter {
    void filter(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx);
}
