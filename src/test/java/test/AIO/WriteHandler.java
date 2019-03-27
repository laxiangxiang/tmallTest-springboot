package test.AIO;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by LXX on 2019/2/27.
 */
public class WriteHandler implements CompletionHandler<Integer,ByteBuffer> {

    private AsynchronousSocketChannel clientSocketChannel;

    private AsynchronousSocketChannel serverSocketChannel;

    private AsynchronousSocketChannel socketChannel;

    private boolean clientOrServer;

    public WriteHandler(AsynchronousSocketChannel clientSocketChannel, AsynchronousSocketChannel serverSocketChannel, boolean clientOrServer) {
        this.clientSocketChannel = clientSocketChannel;
        this.serverSocketChannel = serverSocketChannel;
        this.clientOrServer = clientOrServer;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (clientOrServer){
            socketChannel = serverSocketChannel;
        }else {
            socketChannel = clientSocketChannel;
        }
        //如果没有发送完，继续发送
        if (attachment.hasRemaining()){
            socketChannel.write(attachment,attachment,this);
        }else {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            socketChannel.read(byteBuffer,byteBuffer,new ReadHandler(clientSocketChannel,serverSocketChannel,clientOrServer));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("write failed");
    }
}
