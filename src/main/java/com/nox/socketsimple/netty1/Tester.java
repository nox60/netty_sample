package com.nox.socketsimple.netty1;

public class Tester {
    public static void main(String args[]){
        //通知所有关注a 事件的观察者
        Object a = Topics.topics;
        Topics.topics.get("a").notifyObservers("aaaa");
    }
}
