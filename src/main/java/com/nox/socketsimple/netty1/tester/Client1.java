package com.nox.socketsimple.netty1.tester;

import com.nox.socketsimple.netty1.Client;

public class Client1 {
    public static void main(String args[]){
        new Client("client1").start();
        new Client("client1").start();
        new Client("client1").start();
        new Client("client2").start();
    }}
