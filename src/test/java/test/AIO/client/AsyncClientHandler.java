package test.AIO.client;

import test.AIO.WriteHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * Created by LXX on 2019/2/27.
 */
public class AsyncClientHandler implements Runnable,CompletionHandler<Void,AsyncClientHandler>{

    private String ip;

    private int port;

    private AsynchronousSocketChannel socketChannel;

    private CountDownLatch latch;

    public AsyncClientHandler(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            socketChannel = AsynchronousSocketChannel.open();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        socketChannel.connect(new InetSocketAddress(ip,port),this,this);
        try {
            synchronized (latch){
                latch.wait();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        byte[] bytes = msg.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer,byteBuffer,new WriteHandler(socketChannel,null,false));
    }

    @Override
    public void completed(Void result, AsyncClientHandler attachment) {
        System.out.println(result);
        System.out.println(Thread.currentThread().getName()+",客户端成功连接到服务器");
    }

    @Override
    public void failed(Throwable exc, AsyncClientHandler attachment) {
        System.err.println("连接服务器失败。。。");
        exc.printStackTrace();
        try {
            socketChannel.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
