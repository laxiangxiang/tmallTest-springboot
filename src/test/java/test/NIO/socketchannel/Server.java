package test.NIO.socketchannel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by LXX on 2019/2/25.
 */
public class Server {

    public static void main(String[] args){
        Server server = new Server();
//        server.service();
        server.serviceWithSelector();
    }
    public void service(){
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(8090));
            //非阻塞模式
            serverSocketChannel.configureBlocking(false);
            while (true){
                //获取请求连接
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null){
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    socketChannel.read(buffer);
                    buffer.flip();
                    if (buffer.hasRemaining()){
                        System.out.println(">>>服务端收到数据："+new String(buffer.array()));
                    }
                    buffer.clear();
                    //构造返回的报文，分为头部和主体，实际情况可以构造复杂的报文协议，这里只演示，不做特殊设计
                    ByteBuffer header = ByteBuffer.allocate(6);
                    header.put("[head]".getBytes());
                    ByteBuffer body = ByteBuffer.allocate(1024);
                    body.put("i am body!".getBytes());
                    header.flip();
                    body.flip();
                    ByteBuffer[] byteBuffers = {header,body};
                    //聚集，把多个缓存写入一个通道中
                    socketChannel.write(byteBuffers);
                    socketChannel.close();
                }else {
                    Thread.sleep(1000);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void serviceWithSelector(){
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(8090));
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            //向选择器注册通道的接受事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true){
                //获取已经准备好的通道数量
                int readyChannels = selector.selectNow();
                //如果没有准备好，重试
                if (readyChannels == 0){
                    continue;
                }
                //获取准备好的通道中的事件集合
                Set selectKeys = selector.selectedKeys();
                Iterator iterator = selectKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = (SelectionKey) iterator.next();
                    if (key.isAcceptable()){
                        //在自己注册的事件中写业务逻辑
                        ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverSocketChannel1.accept();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);
                        buffer.flip();
                        if (buffer.hasRemaining()){
                            System.out.println(">>>服务端收到数据："+new String(buffer.array()));
                        }
                        buffer.clear();
                        ByteBuffer header = ByteBuffer.allocate(6);
                        header.put("[head]".getBytes());
                        ByteBuffer body = ByteBuffer.allocate(1024);
                        body.put("i am body!".getBytes());
                        System.out.println(header.mark());
                        header.flip();
                        body.flip();
                        ByteBuffer[] buffers = {header,body};
                        socketChannel.write(buffers);
                        socketChannel.close();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
