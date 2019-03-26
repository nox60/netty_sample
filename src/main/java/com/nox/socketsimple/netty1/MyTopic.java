package com.nox.socketsimple.netty1;

import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;

public class MyTopic implements Subject {

    private List<MyChannel> observers;

    public MyTopic() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void register(MyChannel obj) {
        if (!observers.contains(obj))

            observers.add(obj);
    }

    @Override
    public void unregister(MyChannel obj) {
        observers.remove(obj);
    }

    @Override
    public void notifyObservers(Object msgBody) {
        System.out.println("有 "+this.observers.size()+ " 个客户端需要被通知到");
        for (MyChannel obj : this.observers) {
            try {
                String finalString = msgBody.toString() + ", 发生时间：" + System.currentTimeMillis();
                obj.getChannel().writeAndFlush(Unpooled.copiedBuffer(finalString.getBytes()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
