package com.nox.socketsimple.netty1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * Created by LiuLi on 2018/5/8.
 */
public class Client extends Thread {

    private String topic;

    public Client(String body) {
        this.topic = body;
    }

    private Channel channel = null;

    public Channel getChannel(){
        return this.channel;
    }

    public void setChannel(Channel channel){
        this.channel = channel;
    }

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
                        sc.pipeline().addLast(new ClientHandler(topic));
                    }
                });
        ChannelFuture cf1 = null;
        try {
            cf1 = b.connect("127.0.0.1", 8765).sync();
            setChannel(cf1.channel());
            //System.out.println("客户端：" + cf1.channel().id() + "关注" + this.topic);

            //AttributeKey<String> srcdataAttrKey = AttributeKey.valueOf("topic");
            //Attribute<String> srcdataAttr = cf1.channel().attr(srcdataAttrKey);
            //srcdataAttr.set(topic);

            //发送消息告诉服务端: 设置了关注的主题
            //cf1.channel().writeAndFlush(Unpooled.copiedBuffer("TOPIC_SET".getBytes()));
            cf1.channel().writeAndFlush(Unpooled.copiedBuffer(("TOPIC_SELECT||" + topic).getBytes()));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Channe
        // 息, Buffer类型. write需要flush才发送, 可用writeFlush代替
        //cf1.channel().writeAndFlush(Unpooled.copiedBuffer(this.topic.getBytes()));

        try {
            cf1.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //cf2.channel().closeFuture().sync();
        //group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        new Client("a").start();
        new Client("b").start();
        new Client("a").start();
        new Client("a").start();
        new Client("c").start();
    }
}