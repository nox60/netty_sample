package com.nox.socketsimple.nio1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {

    ServerSocketChannel serverSocketChannel = null;

    Selector selector = null;

    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    ByteBuffer readBuffer = ByteBuffer.allocate(1024);

    public static void main(String args[]) {
        new Server().startServer(8855);
    }

    public void startServer(int port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            // 服务器配置为非阻塞
            serverSocketChannel.configureBlocking(false);
            // 检索与此通道关联的服务器套接字
            ServerSocket serverSocket = serverSocketChannel.socket();
            // 进行服务的绑定
            serverSocket.bind(new InetSocketAddress("localhost", port));

            // 通过open()方法找到Selector
            selector = Selector.open();

            // 注册到selector，等待连接
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey sk = iterator.next();
                    handlerConnect(sk);
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 处理请求
    private void handlerConnect(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel server = null;
        SocketChannel client = null;

        // 测试此键的通道是否已准备好接受新的套接字连接。
        if (selectionKey.isAcceptable()) {
            // 返回为之创建此键的通道。
            server = (ServerSocketChannel) selectionKey.channel();
            // 此方法返回的套接字通道（如果有）将处于阻塞模式。
            client = server.accept();
            // 配置为非阻塞
            client.configureBlocking(false);

            System.out.println("有客户端连接上来");

            client.register(selector, SelectionKey.OP_READ);
        } else if ( selectionKey.isReadable() ){
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

            // Clear out our read buffer so it's ready for new data
            this.readBuffer.clear();
            // Attempt to read off the channel
            int numRead;
            try {
                numRead = socketChannel.read(this.readBuffer);
            } catch (IOException e) {
                // The remote forcibly closed the connection, cancel
                // the selection key and close the channel.
                selectionKey.cancel();
                socketChannel.close();

                return;
            }

            String str = new String(readBuffer.array(), 0, numRead);
            System.out.println(str);
            socketChannel.register(selector, SelectionKey.OP_WRITE);


        } else if ( selectionKey.isWritable() ){
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            writeBuffer.clear();
            String reply = "服务器回复信息";

            //向Buffer写入
            writeBuffer.put(reply.getBytes());

            //切换Buffer读写状态
            writeBuffer.flip();

            //从Buffer里面读出，写入到channel里面
            socketChannel.write(writeBuffer);
            socketChannel.register(selector, SelectionKey.OP_READ);

        }
    }
}
