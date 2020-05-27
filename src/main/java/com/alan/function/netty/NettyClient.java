package com.alan.function.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: netty客户端
 * @Author MengQingHao
 * @Date 2020/5/26 5:40 下午
 */
public class NettyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 50, 1000L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < 100; i++) {
            String msg  = "客户端" + i + "发来问候！";
            LOGGER.info("添加任务：{}   begin", i);
            threadPoolExecutor.execute(new Thread(() -> {
                try {
                    LOGGER.info("已发送：{}", msg);
                    clientSend(msg);
                } catch (InterruptedException e) {
                    LOGGER.error("消息:'{}'发送异常", msg, e);
                }
            }));
            LOGGER.info("添加任务：{}   end", i);
        }
        LOGGER.info("==============over==================");
    }

    /**
     * 定时客户端每2秒发一条消息
     * @param msg 发送消息
     * @author MengQingHao
     * @date 2020/5/26 5:52 下午
     */
    private static void clientSend(String msg) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                });

        Channel channel = bootstrap.connect("127.0.0.1", 8000).channel();
        LOGGER.info("已发送：{}", msg);
        while (true) {
            channel.writeAndFlush(msg);
            Thread.sleep(2000);
        }
    }
}
