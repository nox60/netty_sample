package com.nox.socketsimple.mulitconns.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by LiuLi on 2018/2/23.
 */
public class ServerThread implements Runnable {

    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        PrintWriter writer = null;
        BufferedReader in = null;
        try {
            writer = new PrintWriter(socket.getOutputStream());
            //获取输入流，并读取客户端信息
            String line;

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                line = in.readLine();
                while (null != line) {
                    System.out.println("收到 Client 输入 : " + line);
                    writer.println("服务器已经收到你的信息：" + line);
                    writer.flush();
                    line = null;
                }
            }
        } catch (Exception e) {
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
        }
    }
}
