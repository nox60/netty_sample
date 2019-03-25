package com.nox.socketsimple.simple1.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by LiuLi on 2018/2/22.
 */
public class Client {
    public static void main(String[] args) {
        System.out.println("Client start...");

        Socket socket = null;
        PrintWriter write = null;
        BufferedReader in = null;
        BufferedReader br = null;
        try {
            //启动客户端
            System.out.println("客户端启动成功------");

            //socket
            socket = new Socket("127.0.0.1", 5555);

            // 由Socket对象得到输出流，并构造PrintWriter对象, 将使用该对象输出信息到服务端
            write = new PrintWriter(socket.getOutputStream());

            br = new BufferedReader(new InputStreamReader(System.in));

            //获取输入流，并读取服务器端的响应信息
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String readline;

            String messageFromServer;

            while (true) {

                readline = br.readLine(); // 从系统标准输入读入一字符串

                while (null != readline && !readline.equals("end")) {
                    System.out.println("输入:" + readline);

                    //将消息发送给服务器
                    write.println(readline);

                    write.flush();

                    messageFromServer = in.readLine();

                    System.out.println(messageFromServer);

                    readline = null;


                }
            }


        } catch (Exception e) {
            System.out.println("can not listen to:" + e);// 出错，打印出错信息
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
