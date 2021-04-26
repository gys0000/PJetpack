package com.gystry.io.netty_server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author gystry
 * 创建日期：2020/12/24 15
 * 邮箱：gystry@163.com
 * 描述：
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("---->");
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boos = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ChannelFuture channelFuture = serverBootstrap.group(boos, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
//                        ChannelPipeline pipeline = nioSocketChannel.pipeline();
//                        pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
//                        pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
//                        pipeline.addLast(new SimpleChannelInboundHandler<String>() {
//                            @Override
//                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
//                                System.out.println("server:------> " + s);
//                            }
//                        });

                        nioSocketChannel.pipeline().addLast(new ServerHandler());
                        nioSocketChannel.pipeline().addLast(new AInBoundChannelHandler());
                        nioSocketChannel.pipeline().addLast(new BInBoundChannelHandler());
                        nioSocketChannel.pipeline().addLast(new AOutboundChannelHandler());
                        nioSocketChannel.pipeline().addLast(new BOutboundChannelHandler());
                    }
                })
                .bind(8001);


    }
}
