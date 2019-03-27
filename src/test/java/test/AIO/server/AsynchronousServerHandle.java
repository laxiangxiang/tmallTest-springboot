package test.AIO.server;

import test.AIO.ReadHandler;
import test.AIO.server.AcceptHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * Created by LXX on 2019/2/27.
 */
public class AsynchronousServerHandle implements Runnable{

    public CountDownLatch latch;

    public AsynchronousServerSocketChannel serverSocketChannel;

    private int port;

    public AsynchronousServerHandle(int port) {
        this.port = port;
        try {
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.println(Thread.currentThread().getName()+",服务端已启动，端口号："+port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        // CountDownLatch初始化
        // 它的作用：在完成一组正在执行的操作之前，允许当前的现场一直阻塞
        // 此处，让现场在此阻塞，防止服务端执行完成后退出
        // 也可以使用while(true)+sleep
        // 生成环境就不需要担心这个问题，因为服务端是不会退出的
        latch = new CountDownLatch(1);
            serverSocketChannel.accept(this, new AcceptHandler());
            try {
                synchronized (latch) {
                    latch.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}
