package com.gystry.io.netty_server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @author gystry
 * 创建日期：2021/4/19 16
 * 邮箱：gystry@163.com
 * 描述：
 */
public class BOutboundChannelHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("BOutboundChannelHandler-write-->"+msg);
        super.write(ctx, msg, promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        System.out.println("BOutboundChannelHandler-read-->");
        super.read(ctx);
    }


}
