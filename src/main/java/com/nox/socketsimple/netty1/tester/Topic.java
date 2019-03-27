package com.nox.socketsimple.netty1.tester;

import com.nox.socketsimple.netty1.Client;
import java.util.Scanner;

public class Topic {
    public static void main(String args[]) {
        Client topic1 = new Client("topic1");
        Client topic2 = new Client("topic2");

        topic1.start();
        topic2.start();

        String val = null;       // 记录输入的字符串
        Scanner input = new Scanner(System.in);

        do {
            System.out.println("请输入：");
            val = input.next();       // 等待输入值
            System.out.println("您输入的是：" + val);

        } while (!val.equals("#"));   // 如果输入的值不是#就继续输入
        System.out.println("你输入了\"#\"，程序已经退出！");
    }
}
