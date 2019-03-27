package test.AIO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by LXX on 2019/2/27.
 */
public class ReadHandler implements CompletionHandler<Integer,ByteBuffer> {

    private AsynchronousSocketChannel clientSocketChannel;

    private AsynchronousSocketChannel serverSocketChannel;

    private AsynchronousSocketChannel socketChannel;

    private boolean clientOrServer;

    public ReadHandler(AsynchronousSocketChannel clientSocketChannel, AsynchronousSocketChannel serverSocketChannel, boolean clientOrServer) {
        this.clientSocketChannel = clientSocketChannel;
        this.serverSocketChannel = serverSocketChannel;
        this.clientOrServer = clientOrServer;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] message = new byte[attachment.remaining()];
        attachment.get(message);
        try {
            String expression = new String(message,"utf-8");
//            String expression = new String(attachment.array(),"utf-8");
            if (clientOrServer){
                System.out.println(Thread.currentThread().getName()+",服务器收到消息："+expression);
                doWrite(expression+"<>");
            }else {
                System.out.println(Thread.currentThread().getName()+",客户端收到消息："+expression);
//                clientSocketChannel.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void doWrite(String result){
        if (clientOrServer)
            socketChannel = serverSocketChannel;
        else
            socketChannel = clientSocketChannel;
        byte[] bytes = result.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer, byteBuffer, new WriteHandler(clientSocketChannel,serverSocketChannel,clientOrServer));
    }
    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("read failed");
    }
}
