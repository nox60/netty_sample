package com.nox.socketsimple.netty1.tester;

import com.nox.socketsimple.netty1.Topics;

public class ServerNotifier1 {
    public static void main(String args[]){
        //通知所有关注a 事件的观察者
        Object a = Topics.topics;
        Topics.topics.get("client1").notifyObservers("aaaa");
    }
}
