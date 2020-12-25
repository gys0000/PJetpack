package com.gystry.io.netty_server;

import com.gystry.io.protocol.PacketCodeC;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author gystry
 * 创建日期：2020/12/25 14
 * 邮箱：gystry@163.com
 * 描述：
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf requestByteBuf = (ByteBuf) msg;
        String decode2Str = PacketCodeC.INSTANCE.decode2Str(requestByteBuf);

        System.out.println("接收到客户端消息--->：" + decode2Str + "------字节数据:" + requestByteBuf.toString() + ":" + ctx.channel().remoteAddress());
        if (decode2Str.startsWith("re--")) {
            ByteBuf confirmMsg = PacketCodeC.INSTANCE.encodeStr2Byte(ctx.alloc(), "server confirm message");
            ctx.channel().writeAndFlush(confirmMsg);

        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("上线的客户端地址：" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("下线的客户端地址：" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }
}
