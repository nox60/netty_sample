package com.nox.socketsimple.netty1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;

/**
 * Created by LiuLi on 2018/5/8.
 */
public class ClientHandler extends ChannelHandlerAdapter {

    private String topic;

    public ClientHandler(String topic){
        this.topic = topic;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("active, the topic is "+topic);
        ctx.channel().writeAndFlush("TOPIC_SELECT||"+topic);
        //System.out.println("channelActive, the channel id is " + ctx.channel().id());

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //System.out.println("打印topic");

        AttributeKey<String> nameAttrKey = AttributeKey.valueOf("topic");

        Attribute<String> attr = ctx.channel().attr(nameAttrKey);

        //System.out.println("  ===============  " + attr.get());

        try {
            ByteBuf buf = (ByteBuf) msg;
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            String body = new String(req, "utf-8");
            System.out.println(body);
            //System.out.println("Client ---- : client id is: " + ctx.channel().id() + body);
        } finally {
            // 记得释放xxxHandler里面的方法的msg参数: 写(write)数据, msg引用将被自动释放不用手动处理; 但只读数据时,!必须手动释放引用数
            ReferenceCountUtil.release(msg);
        }
        //System.out.println("channelRead");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("channelReadComplete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        //System.out.println("exceptionCaught");
        ctx.close();
    }
}