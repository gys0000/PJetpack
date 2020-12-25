package com.gystry.io.netty_client;

import com.gystry.io.protocol.PacketCodeC;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author gystry
 * 创建日期：2020/12/24 15
 * 邮箱：gystry@163.com
 * 描述：
 */
public class NettyClient {

    private static ClientHandler dispatcher;
    private static Bootstrap bootstrap;
    private static NioEventLoopGroup group;
    private static ChannelFuture channelFuture;

    public static void main(String[] args) throws InterruptedException {
        init();
        connect();
        Channel channel = channelFuture.channel();
        //创建Scanner对象，接受从控制台输入
        Scanner input = new Scanner(System.in);
        while (true) {
            //接受String类型
            String str = input.next();
            //输出结果
            System.out.println(str);
            System.out.println("------->");

            ByteBuf byteBuf = PacketCodeC.INSTANCE.encodeStr2Byte(channel.alloc(), str);
            channel.writeAndFlush(byteBuf);
        }

    }

    static void init() {
        dispatcher = new ClientHandler();
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast("handler",dispatcher);
                    }
                });
    }

    private static int countdown =3;

    static void connect() {
        channelFuture = bootstrap.connect("127.0.0.1", 8000).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("----->connected 已连接");
                } else {
                    System.out.println("----->unconnected 未连接");
                    channelFuture.channel().eventLoop().schedule(() -> {
                        connect();
//                        if (countdown < 30) {
//                            countdown += 5;
//                        }
                    }, countdown, TimeUnit.SECONDS);
                }
            }
        });
    }

}
