package com.gystry.io.netty_server;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * @author gystry
 * 创建日期：2021/4/19 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public class PacketCodecHandler extends MessageToMessageCodec {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {

    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {

    }
}
