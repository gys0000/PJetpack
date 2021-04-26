package com.gystry.io.netty_server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author gystry
 * 创建日期：2021/4/19 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public class BInBoundChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("BInBoundChannelHandler--channelRead->"+msg);
        super.channelRead(ctx, msg);
    }
}
