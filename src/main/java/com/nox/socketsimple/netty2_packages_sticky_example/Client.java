package com.nox.socketsimple.netty2_packages_sticky_example;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by LiuLi on 2018/5/8.
 */
public class Client {

    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new LineBasedFrameDecoder(1024));
                      //  sc.pipeline().addLast(new StringDecoder());
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });

        ChannelFuture cf1 = b.connect("127.0.0.1", 8765).sync();
        //ChannelFuture cf2 = b.connect("127.0.0.1", 8764).sync();  //可以使用多个端口
        //发送消息, Buffer类型. write需要flush才发送, 可用writeFlush代替
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("ttt".getBytes()));
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("bbb".getBytes()));
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("ddd".getBytes()));
     //   Thread.sleep(2000);
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("ACCadfasfsadfsafasdfasdfasfdasdfsafdasfdasdfasdfasfdasdfasdfasdfasfasdfsdfasdfasdfasfafeeeeeeeeeeeeeeeeeeeeACCadfasfsadfsafasdfasdfasfdasdfsafdasfdasdfasdfasfdasdfasdfasdfasfasdfsdfasdfasdfasfafeeeeeeeeeeeeeeeeeeeeACCadfasfsadfsafasdfasdfasfdasdfsafdasfdasdfasdfasfdasdfasdfasdfasfasdfsdfasdfasdfasfafeeeeeeeeeeeeeeeeeeeeACCadfasfsadfsafasdfasdfasfdasdfsafdasfdasdfasdfasfdasdfasdfasdfasfasdfsdfasdfasdfasfafeeeeeeeeeeeeeeeeeeeeACCadfasfsadfsafasdfasdfasfdasdfsafdasfdasdfasdfasfdasdfasdfasdfasfasdfsdfasdfasdfasfafeeeeeeeeeeeeeeeeeeeeACCadfasfsadfsafasdfasdfasfdasdfsafdasfdasdfasdfasfdasdfasdfasdfasfasdfsdfasdfasdfasfafeeeeeeeeeeeeeeeeeeeeACCadfasfsadfsafasdfasdfasfdasdfsafdasfdasdfasdfasfdasdfasdfasdfasfasdfsdfasdfasdfasfafeeeeeeeeeeeeeeeeeeee".getBytes()));
        //cf2.channel().writeAndFlush(Unpooled.copiedBuffer("999".getBytes()));
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("fff".getBytes()));


        for(int i =0;i<100;i++){
            ByteBuf message = Unpooled.buffer(3);
            String temp = "1eeeeeeeeeeee"+System.getProperty("line.separator");
            message.writeBytes(temp.getBytes());
            cf1.channel().writeAndFlush(message);

        }
        cf1.channel().closeFuture().sync();
        //cf2.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}