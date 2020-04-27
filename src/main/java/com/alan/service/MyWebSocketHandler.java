package com.alan.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

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
        LOGGER.info("客户端{}与服务端建立连接，通道开启！", ctx.channel().id().asLongText());
        // 添加到channelGroup通道组
        channelGroup.add(ctx.channel());
    }

    /**
     * 客户端与服务端关闭连接的时候触发
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("客户端{}与服务端断开连接，通道关闭！", ctx.channel().id().asLongText());
        channelGroup.remove(ctx.channel());
    }

    /**
     * 服务端接收客户端的数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        // {"channelId":"1c36bbfffeec6dcf-000002b0-00000001-fa1f40485870d605-9b0d8b7f","message":"tset"}
        JSONObject obj = JSON.parseObject(textWebSocketFrame.text());
        String channelId = obj.getString("channelId");
        String message = obj.getString("message");
        LOGGER.info("客户端{}发送给客户端{}消息:{}", channelHandlerContext.channel().id(), channelId, message);
        Iterator<Channel> channels = channelGroup.iterator();
        while (channels.hasNext()) {
            Channel channel = channels.next();
            if (channel.id().asLongText().equals(channelId)) {
                sendMessage(channel, message);
            }
        }

//        sendAllMessage(message);
    }

    /**
     * 给指定的客户端发消息
     * @param channel
     * @param message
     * @return void
     * @author MengQingHao
     * @date 2020/4/24 5:28 下午
     * @version 1.3.0
     */
    private void sendMessage(Channel channel, String message) {
        String msg = "收到消息：" + message + "，单个响应！";
        channel.writeAndFlush(new TextWebSocketFrame(msg));
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
