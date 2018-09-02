package com.xueyou.demo.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wuxueyou on 2018/9/2.
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    static final Logger LOG = LoggerFactory.getLogger(NettyHttpServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        LOG.info(fullHttpRequest.getUri());
    }
}
