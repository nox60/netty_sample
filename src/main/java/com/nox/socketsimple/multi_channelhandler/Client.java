package com.nox.socketsimple.multi_channelhandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by LiuLi on 2019/4/8.
 */
public class Client extends Thread{
    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast("decoder", new StringDecoder());
                        sc.pipeline().addLast("encoder", new StringEncoder());
                        sc.pipeline().addLast(new BaseClient1Handler());
                        sc.pipeline().addLast(new BaseClient2Handler());
                    }
                });
        ChannelFuture cf1 = null;

        try {
            cf1 = b.connect("127.0.0.1", 8776).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cf1.channel().writeAndFlush("Hello Netty Server ,I am a common client");

        try {
            cf1.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        group.shutdownGracefully();
    }

    public static void main(String ars[]){
        new Client().start();
    }
}
