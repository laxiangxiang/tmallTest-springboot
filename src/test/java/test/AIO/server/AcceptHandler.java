package test.AIO.server;

import test.AIO.ReadHandler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by LXX on 2019/2/27.
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel,AsynchronousServerHandle>{

    @Override
    public void completed(AsynchronousSocketChannel result, AsynchronousServerHandle attachment) {
        Server.clientCount++;
        System.out.println(Thread.currentThread().getName()+",连接的客户端数量："+Server.clientCount);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        result.read(byteBuffer,byteBuffer,new ReadHandler(null,result,true));
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerHandle attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}
