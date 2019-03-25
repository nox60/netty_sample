package com.nox.socketsimple.netty1;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class MyTopic implements Subject {

    private List<ChannelHandlerContext> observers;

    public MyTopic() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void register(ChannelHandlerContext obj) {
        if (!observers.contains(obj))
            observers.add(obj);
    }

    @Override
    public void unregister(ChannelHandlerContext obj) {
        observers.remove(obj);
    }

    @Override
    public void notifyObservers(Object msgBody) {
        for (ChannelHandlerContext obj : this.observers) {
            System.out.println("需要通知客户端");
            try {
                String finalString = msgBody.toString() + ", 发生时间：" + System.currentTimeMillis();
                obj.writeAndFlush(Unpooled.copiedBuffer(finalString.getBytes()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
