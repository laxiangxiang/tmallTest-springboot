package test.NIO.RPC.client;

import com.alibaba.fastjson.JSON;
import test.NIO.RPC.util.RpcInitFactory;
import test.NIO.RPC.util.RpcRequest;
import test.NIO.RPC.util.RpcResponse;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * Created by LXX on 2019/2/26.
 * 通用客户端,发送请求通用类
 */
public class CommonClient {

    private RpcInitFactory rpcInitFactory;

    public CommonClient(RpcInitFactory rpcInitFactory) {
        this.rpcInitFactory = rpcInitFactory;
    }

    public <T> T request(RpcRequest request){
        RpcResponse response = null;
        request.setRequestId(UUID.randomUUID().toString());
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(rpcInitFactory.getIp(),rpcInitFactory.getPort()));
            ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
            byteBuffer.put(JSON.toJSONBytes(request));
            byteBuffer.put(">".getBytes());
            byteBuffer.flip();
            if (byteBuffer.hasRemaining()){
                socketChannel.write(byteBuffer);
            }
            byteBuffer.clear();
            ByteBuffer body = ByteBuffer.allocate(2048);
            socketChannel.read(body);
            body.flip();
            if (body.hasRemaining()){
                response = JSON.parseObject(new String(body.array()),RpcResponse.class);
            }
            body.clear();
            socketChannel.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return (T)response;
    }
}
