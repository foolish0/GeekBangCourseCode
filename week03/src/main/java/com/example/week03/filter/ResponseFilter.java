package com.example.week03.filter;

import io.netty.handler.codec.http.FullHttpResponse;

public interface ResponseFilter {
    public void filter(FullHttpResponse response);
}
