package com.alan.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import com.alan.service.MyWebSocketHandler;

/**
 * @Description: netty配置
 * @Author MengQingHao
 * @Date 2020/4/24 4:41 下午
 * @Version 1.3.0
 */
@Component
public class NettyConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyConfig.class);

    @Value("${server.port}")
    private int port;

    @PostConstruct
    public void start() {
        LOGGER.info("NettyConfig start ---------------");
        //构造两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //服务端启动辅助类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(this.port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            LOGGER.info("收到新连接：local->{}  remote->{}", socketChannel.localAddress().toString(), socketChannel.remoteAddress().toString());
                            socketChannel.pipeline().addLast(new HttpServerCodec());
                            socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                            socketChannel.pipeline().addLast(new HttpObjectAggregator(8192));
                            socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler("/mqh", "WebSocket", true, 65536 * 10));
                            socketChannel.pipeline().addLast(new MyWebSocketHandler());
                        }
                    });

            // 服务器异步创建绑定
            ChannelFuture future = bootstrap.bind(8080).sync();
            // 关闭服务器通道
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
