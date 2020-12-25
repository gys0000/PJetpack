package com.gystry.io.netty_client;

import com.gystry.io.protocol.PacketCodeC;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author gystry
 * 创建日期：2020/12/25 15
 * 邮箱：gystry@163.com
 * 描述：@ChannelHandler.Sharable  TODO 作用
 *Indicates that the same instance of the annotated ChannelHandler can be added to one or more ChannelPipelines multiple times without a race condition.
If this annotation is not specified, you have to create a new handler instance every time you add it to a pipeline because it has unshared state such as member variables.
This annotation is provided for documentation purpose, just like the JCIP annotations
 */
@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String decode2Str = PacketCodeC.INSTANCE.decode2Str(byteBuf);
        System.out.println("收到服务端消息-->：" + decode2Str);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("与服务器失去连接");
        NettyClient.connect();
    }
}

