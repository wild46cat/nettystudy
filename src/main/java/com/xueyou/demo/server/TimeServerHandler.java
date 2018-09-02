package com.xueyou.demo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import javax.sound.midi.Soundbank;
import java.util.Date;


/**
 * Created by wuxueyou on 2018/7/7.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    private int count = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("The time server receive order:" + body);
//        String currentTime = "".equals(body) ? String.valueOf(new Date().getTime()) : "BAD ORDER\r\n";
        String currentTime = "ERROR";
        if (body.equals("abc")) {
            currentTime = String.valueOf(new Date().getTime()) + "|" + ++count;
            System.out.println(count);
        }
        ByteBuf resp = Unpooled.copiedBuffer((currentTime + "\r\n").getBytes());
        ctx.write(resp);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
