package com.nox.socketsimple.mulitconns.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by LiuLi on 2018/2/22.
 */
public class Server {
    public static void main(String args[]) {
        ServerSocket server = null;
        Socket socket = null;
        try {
            server = new ServerSocket(5555);
            //指定绑定的端口，并监听此端口。
            System.out.println("----服务器启动成功");

            while(true){
                socket = server.accept();
                new Thread(new ServerThread(socket)).start();
            }

        } catch (Exception e) {//出错，打印出错信息
            e.printStackTrace();
        } finally {

        }
    }
}
