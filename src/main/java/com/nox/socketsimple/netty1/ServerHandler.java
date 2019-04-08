package com.nox.socketsimple.netty1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.io.UnsupportedEncodingException;

/**
 * Created by LiuLi on 2018/5/8.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server channelRead..");
        // System.out.println(ctx.channel().remoteAddress() + "->Server :" + msg.toString());
        //System.out.println(msg.toString());

        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = null;
        try {
            body = new String(req, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(body);


        //判断消息是否含有TOPIC_SELECT的信息，标明channel需要关注的对象
        if (body != null && body.indexOf("TOPIC_SELECT") > -1) {
            //   System.out.println("TOPIC_SELECT ----- "+body);
            String topicName = body.replace("TOPIC_SELECT||", "");

            //从消息中解析出关注的topic
            MyTopic myTopic = null;
            MyChannel myChannel = new MyChannel();

            synchronized (Topics.topics) {
                try {
                    if (!Topics.topics.containsKey(topicName)) {//没有该key
                        myTopic = new MyTopic();
                        System.out.println("新关注的客户端，客户端ID：" + ctx.channel().id() + ",关注主题:" + topicName);
                    } else {
                        myTopic = Topics.topics.get(topicName);
                        System.out.println("已经存在的主题，加入新的关注客户端");
                    }

                    myChannel.setChannel(ctx.channel());
                    myTopic.register(myChannel);
                    Topics.topics.put(topicName, myTopic);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ( body != null && body.indexOf("TOPIC_UPDATE") > -1 ) {
            //   System.out.println("TOPIC_SELECT ----- "+body);
            String topicName = body.replace("TOPIC_UPDATE||", "");

            //从消息中解析出关注的topic
            MyTopic myTopic = Topics.topics.get(topicName);
            MyChannel myChannel = new MyChannel();
            myTopic = Topics.topics.get(topicName);

            myTopic.notifyObservers(topicName + "发送消息通知监听的客户端");
        }

        //ctx.channel().writeAndFlush(Unpooled.copiedBuffer(("server
        // write some thing to client").getBytes()));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //cause.printStackTrace();
        ctx.close();
    }



    /*

    //这里将ctx缓存起来，以便 sendMsg方法使用，但是这里不够了解ctx的底层细节，需要去研究是否会出问题。
    private ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("server channel active... ");
        //System.out.println("channelActive");

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "utf-8");

       // System.out.println("message body: " + body);

        if (body != null && body.indexOf("TOPIC_SELECT") > -1 ) {
         //   System.out.println("TOPIC_SELECT ----- "+body);
            String topicName = body.replace("TOPIC_SELECT||", "");

            //从消息中解析出关注的topic

            MyTopic myTopic = null;
            MyChannel myChannel = new MyChannel();

            try{
                if (!Topics.topics.containsKey(topicName)) {
                    myTopic = new MyTopic();
                    myChannel.setChannel(ctx.channel());
                    myTopic.register(myChannel);
                    System.out.println("新关注的客户端，客户端ID：" + ctx.channel().id() + ",关注主题:" + topicName);
                } else {
                    myTopic = Topics.topics.get(topicName);
                    myTopic.register(myChannel);
                }
                Topics.topics.put(topicName, myTopic);
            }catch(Exception e){
                e.printStackTrace();
            }

        }

        String response = "返回给客户端的响应：" + body;

        ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));

        // future完成后触发监听器, 此处是写完即关闭(短连接). 因此需要关闭连接时, 要通过server端关闭. 直接关闭用方法ctx[.channel()].close()
        //.addListener(ChannelFutureListener.CLOSE);
        //System.out.println("channelRead");
    }

    public void sendMsg(Object msg) throws Exception {
        this.ctx.writeAndFlush("通知客户端：" + msg.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
            throws Exception {
        System.out.println("读完了");
        ctx.flush();
        //System.out.println("channelReadComplete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable t)
            throws Exception {
        ctx.close();
        //System.out.println("exceptionCaught");
    }
    */
}