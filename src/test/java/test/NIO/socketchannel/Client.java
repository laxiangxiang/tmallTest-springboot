package test.NIO.socketchannel;

import io.netty.buffer.ByteBuf;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by LXX on 2019/2/25.
 */
public class Client {

    public static void main(String[] args){
        Client client = new Client();
        client.request();
    }

    public void request(){
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost",8090));
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("来自客户端的请求".getBytes());
            buffer.flip();
            if (buffer.hasRemaining()){
                socketChannel.write(buffer);
            }
            buffer.clear();
            //接受服务端的返回
            ByteBuffer header = ByteBuffer.allocate(6);
            ByteBuffer body = ByteBuffer.allocate(1024);
            ByteBuffer[] buffers = {header,body};
            //分散：从一个通道中把内容写入多个缓存中
            socketChannel.read(buffers);
            header.flip();
            body.flip();
            System.out.println(header.mark());
            System.out.println(body.mark());
            if (header.hasRemaining()){
                System.out.println(">>>客户端接受头部数据："+new String(header.array()));
            }
            if (header.hasRemaining()){
                System.out.println(">>>客户端接受body数据："+new String(body.array()));
            }
            socketChannel.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
