package com.nox.socketsimple.netty1.tester;

import com.nox.socketsimple.netty1.Client;
import com.nox.socketsimple.netty1.MyTopic;
import com.nox.socketsimple.netty1.Topics;

public class Client1 {
    public static void main(String args[]){
        Client client1 = new Client("client1");
        client1.start();
        new Client("client1").start();
        new Client("client1").start();
        new Client("client2").start();




        String val = null;       // 记录输入的字符串
        do {
            System.out.println("请输入：");
            val = input.next();       // 等待输入值
            System.out.println("您输入的是：" + val);
            MyTopic myTopic = Topics.topics.get(val);
            myTopic.notifyObservers("发送消息通知监听的客户端");
        } while (!val.equals("#"));   // 如果输入的值不是#就继续输入
        System.out.println("你输入了\"#\"，程序已经退出！");



    }}
