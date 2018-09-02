package com.xueyou.demo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {
    static final Logger LOG = LoggerFactory.getLogger(HttpServer.class);
    public static int MAX_CHUNK_SIZE = 1024 * 1024 * 30;

    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast("http-decoder", new HttpRequestDecoder())
                                    .addLast("http-chunk-aggregator",
                                            new HttpObjectAggregator(HttpServer.MAX_CHUNK_SIZE))
                                    .addLast("http-encoder", new HttpResponseEncoder())
                                    .addLast("compressor", new HttpContentCompressor())
                                    .addLast("handler", new NettyHttpServerHandler());
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            LOG.info(">>>>>>netty start port:{}<<<<<<", port);
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 45678;
        try {
            new HttpServer().bind(port);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
