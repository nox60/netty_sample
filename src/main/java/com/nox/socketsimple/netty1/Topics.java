package com.nox.socketsimple.netty1;


import java.util.concurrent.ConcurrentHashMap;

public class Topics {

    //全局单例的hashmap, 现在只是随便写的
    public static ConcurrentHashMap<String,MyTopic> topics = new ConcurrentHashMap<String,MyTopic>();
}
