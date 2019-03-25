package com.nox.socketsimple.simple1.server;

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
        BufferedReader in = null;
        PrintWriter writer = null;
        try {
            server = new ServerSocket(5555);
            //指定绑定的端口，并监听此端口。
            System.out.println("--服务器启动成功");

            socket=server.accept();

            writer=new PrintWriter(socket.getOutputStream());

            //调用accept()方法开始监听，等待客户端的连接
            //使用accept()阻塞等待客户请求，有客户
            //请求到来则产生一个Socket对象，并继续执行
            //socket = server.accept();

            //获取输入流，并读取客户端信息
            String line;

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                line = in.readLine();
                while( null != line ){
                    System.out.println("收到 Client 输入 : " + line);
                    writer.println("服务器已经收到你的信息："+ line);
                    writer.flush();
                    line = null;
                }

            }


        } catch (Exception e) {//出错，打印出错信息
            e.printStackTrace();
        } finally {
            //5、关闭资源
            try {
                in.close(); //关闭Socket输入流
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close(); //关闭Socket
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close(); //关闭ServerSocket
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.close();
        }
    }
}
