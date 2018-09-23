package com.xueyou.demo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

/**
 * Created by wuxueyou on 2018/9/2.
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    static final Logger LOG = LoggerFactory.getLogger(NettyHttpServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        Profiler profiler = new Profiler("Timing profiler");
        profiler.start("handle request");
        LOG.info(fullHttpRequest.getUri());
        LOG.info(fullHttpRequest.getMethod().toString());
        profiler.start("handle response");
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set("content-Type", "text/html;charset=UTF-8");
        StringBuilder sb = new StringBuilder();
        sb.append("test");
        ByteBuf responseBuf = Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8);
        response.content().writeBytes(responseBuf);
        responseBuf.release();
        profiler.start("write response");
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        profiler.stop();
        int spentMs = (int) profiler.elapsedTime() / 1000000;
        LOG.warn(profiler.toString());
        LOG.warn("Total Time is {}ms", spentMs);
    }
}
