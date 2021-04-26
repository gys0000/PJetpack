package com.gystry.io.netty_server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * @author gystry
 * 创建日期：2021/4/19 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public class AInBoundChannelHandler extends ChannelInboundHandlerAdapter {

   final static AttributeKey<String> signee = AttributeKey.newInstance("sign");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        Attribute<String> attr = ctx.pipeline().channel().attr(signee);
        String sign = attr.get();
        System.out.println("AInBoundChannelHandler--channelRead->"+msg+"-sign:"+sign+":"+ctx.pipeline().channel().hasAttr(signee));
        super.channelRead(ctx, msg);
    }
}

