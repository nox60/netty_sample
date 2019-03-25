package com.nox.socketsimple.nio1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Client {

    SocketChannel sc = null;

    Selector selector = null;

    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    ByteBuffer readBuffer = ByteBuffer.allocate(1024);

    public static void main(String args[]) {
        new Client().startClient(8855);
    }

    public void startClient(int port) {
        try {
            sc = SocketChannel.open();

            //打开selector
            selector = selector.open();

            // 注册为非阻塞通道
            sc.configureBlocking(false);

            //连接到套接字
            sc.connect(new InetSocketAddress("localhost", port));

            //注册关注的事件
            sc.register(selector, SelectionKey.OP_CONNECT);

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try{
                selector.select();

                Iterator<SelectionKey> it = selector.selectedKeys().iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    sc = (SocketChannel) key.channel();

                    try {
                        if (key.isConnectable()) {
                            if (sc.isConnectionPending()) {
                                // 结束连接，以完成整个连接过程
                                sc.finishConnect();
                                System.out.println("客户端连接成功");
                                sc.register(selector, SelectionKey.OP_WRITE);
                            }
                        } else if( key.isWritable()) {
                            String message = "发送给服务器的消息";
                            //ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes());
                            writeBuffer.clear();
                            writeBuffer.put(message.getBytes());
                            //将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
                            writeBuffer.flip();
                            sc.write(writeBuffer);

                            //注册写操作,每个chanel只能注册一个操作，最后注册的一个生效
                            //如果你对不止一种事件感兴趣，那么可以用“位或”操作符将常量连接起来
                            //int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
                            //使用interest集合
                            sc.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable() ){

                            SocketChannel client = (SocketChannel) key.channel();
                            //将缓冲区清空以备下次读取
                            readBuffer.clear();
                            int num = client.read(readBuffer);
                            System.out.println("收到服务器消息 ： "+new String(readBuffer.array(),0, num));
                            //注册读操作，下一次读取
                            sc.register(selector, SelectionKey.OP_WRITE);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    it.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}