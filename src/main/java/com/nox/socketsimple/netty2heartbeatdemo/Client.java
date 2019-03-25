package com.nox.socketsimple.netty2heartbeatdemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

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
                        //这里对 readerIdleTime进行说明，就是字面含义，就是超过5秒没有收到来自另外一端的信息了。
                        //这里对 writeIdleTime进行说明，就是字面含义，就是超过5秒没有往另外一段写入数据了。
                        sc.pipeline().addLast(new IdleStateHandler(5,0,0, TimeUnit.SECONDS));
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });



        ChannelFuture cf1 = b.connect("127.0.0.1", 8765).sync();
        /*
        //ChannelFuture cf2 = b.connect("127.0.0.1", 8764).sync();  //可以使用多个端口
        //发送消息, Buffer类型. write需要flush才发送, 可用writeFlush代替
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("ttt".getBytes()));
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("bbb".getBytes()));
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("ddd".getBytes()));
        //Thread.sleep(2000);
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("ACCadfasfsadfsafasdfasdfasfdasdfsafdasfdasdfasdfasfdasdfasdfasdfasfasdfsdfasdfasdfasfafeeeeeeeeeeeeeeeeeeeeACCadfasfsadfsafasdfasdfasfdasdfsafdasfdasdfasdfasfdasdfasdfasdfasfasdfsdfasdfasdfasfafeeeeeeeeeeeeeeeeeeeeACCadfasfsadfsafasdfasdfasfdasdfsafdasfdasdfasdfasfdasdfasdfasdfasfasdfsdfasdfasdfasfafeeeeeeeeeeeeeeeeeeeeACCadfasfsadfsafasdfasdfasfdasdfsafdasfdasdfasdfasfdasdfasdfasdfasfasdfsdfasdfasdfasfafeeeeeeeeeeeeeeeeeeeeACCadfasfsadfsafasdfasdfasfdasdfsafdasfdasdfasdfasfdasdfasdfasdfasfasdfsdfasdfasdfasfafeeeeeeeeeeeeeeeeeeeeACCadfasfsadfsafasdfasdfasfdasdfsafdasfdasdfasdfasfdasdfasdfasdfasfasdfsdfasdfasdfasfafeeeeeeeeeeeeeeeeeeeeACCadfasfsadfsafasdfasdfasfdasdfsafdasfdasdfasdfasfdasdfasdfasdfasfasdfsdfasdfasdfasfafeeeeeeeeeeeeeeeeeeee".getBytes()));
        //cf2.channel().writeAndFlush(Unpooled.copiedBuffer("999".getBytes()));
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("fff".getBytes()));


        for(int i =0;i<100;i++){
            ByteBuf message = Unpooled.buffer(3);
            message.writeBytes("1".getBytes());
            cf1.channel().writeAndFlush(message);

        }*/

        for( int i = 0;i<10000;i++){
            cf1.channel().writeAndFlush(Unpooled.copiedBuffer("try to send message to server...".getBytes()));
            Thread.currentThread().sleep(2000);//保持往服务器写，则服务器的 writeIdle就不会有效了。
        }

        cf1.channel().closeFuture().sync();
        //cf2.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}