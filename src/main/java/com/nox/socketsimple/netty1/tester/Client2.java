package com.nox.socketsimple.netty1.tester;

import com.nox.socketsimple.netty1.Client;

public class Client2 {
    public static void main(String args[]){
        System.out.println("Test add line");
        new Client("client2").start();
    }
}
