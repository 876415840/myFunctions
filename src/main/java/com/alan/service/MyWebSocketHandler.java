package com.alan.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @Description: websocket处理
 * @Author MengQingHao
 * @Date 2020/4/24 5:14 下午
 * @Version 1.3.0
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyWebSocketHandler.class);

    public static ChannelGroup channelGroup;
    static {
        channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    /**
     * 客户端与服务端建立连接的时候触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("客户端与服务端建立连接，通道开启！");
        // 添加到channelGroup通道组
        channelGroup.add(ctx.channel());
    }

    /**
     * 客户端与服务端关闭连接的时候触发
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("客户端与服务端断开连接，通道关闭！");
        channelGroup.remove(ctx.channel());
    }

    /**
     * 服务端接收客户端的数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String message = textWebSocketFrame.text();
        LOGGER.info("服务端收到数据：{}  local->{}  remote->{}", message, channelHandlerContext.channel().localAddress().toString(), channelHandlerContext.channel().remoteAddress().toString());
        if (System.currentTimeMillis() % 2 == 0) {
            sendMessage(channelHandlerContext, message);
        } else {
            sendAllMessage(message);
        }
    }

    /**
     * 给指定的客户端发消息
     * @param channelHandlerContext
     * @param message
     * @return void
     * @author MengQingHao
     * @date 2020/4/24 5:28 下午
     * @version 1.3.0
     */
    private void sendMessage(ChannelHandlerContext channelHandlerContext, String message) {
        String msg = "收到消息：" + message + "，单个响应！";
        channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame(msg));
    }

    /**
     * 发送群消息，其他客户端也能收到
     * @param message
     * @return void
     * @author MengQingHao
     * @date 2020/4/24 5:26 下午
     * @version 1.3.0
     */
    private void sendAllMessage(String message) {
        String msg = "收到消息：" + message + "，群发响应！";
        channelGroup.writeAndFlush(new TextWebSocketFrame(msg));
    }


}
