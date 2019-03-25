package com.nox.socketsimple.netty1;

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
            try {
                obj.writeAndFlush(msgBody.toString() + ", 发生时间：" + System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
