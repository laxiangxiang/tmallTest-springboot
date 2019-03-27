package test.NIO.RPC.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.IntegerCodec;
import test.NIO.RPC.Idemo;
import test.NIO.RPC.util.RpcRegister;
import test.NIO.RPC.util.RpcRequest;
import test.NIO.RPC.util.RpcResponse;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by LXX on 2019/2/25.
 * 请求处理，解析出客户端需要的服务
 */
public class RequestParser implements Runnable{
    private int port;

    public RequestParser(int port){
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true){
                int readyChannels = selector.selectNow();
                if (readyChannels != 0) continue;
                Set selectedKeys = selector.selectedKeys();
                Iterator iterator = selectedKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = (SelectionKey) iterator.next();
                    if (key.isAcceptable()){
                        ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverSocketChannel1.accept();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
                        socketChannel.read(byteBuffer);
                        byteBuffer.flip();
                        String receiveStr = new String(byteBuffer.array());
                        if (byteBuffer.hasRemaining()){
                            System.out.println(">>>服务端收到数据："+receiveStr);
                            //判断接受的内容是否有结束符，如果有，说明是一个请求结束
                            if (receiveStr.contains(">")){
                                RpcRequest request = JSON.parseObject(receiveStr.replace(">",""),RpcRequest.class);
                                RpcResponse response = new RpcResponse();
                                response.setRequestId(request.getRequestId());
                                System.out.println(request);
                                //使用java反射
//                                Class<?> nativeInterface = Class.forName(request.getInterfaceName());
//                                Method nativeMethod = nativeInterface.getMethod(request.getMethodName(),request.getParameterTypes());
//                                if (null != nativeMethod){
//                                    Object o = nativeMethod.invoke(RpcRegister.buildRegist().findService(request.getInterfaceName()),request.getParameters());
//                                    response.setException(null);
//                                    response.setResult(o);
//                                }
                                //使用方法句柄
                                MethodType methodType = MethodType.methodType(Integer.class, Integer.class,Integer.class);
                                MethodHandles.Lookup lookup = MethodHandles.lookup();
                                MethodHandle methodHandle = lookup.findVirtual(Class.forName(request.getInterfaceName()),request.getMethodName(),methodType);
                                try {
                                    Object o = methodHandle.invoke(RpcRegister.buildRegist().findService(request.getInterfaceName()),
                                            request.getParameters()[0],
                                            request.getParameters()[1]);
                                    response.setException(null);
                                    response.setResult(o);
                                }catch (Throwable throwable){
                                    throwable.printStackTrace();
                                }
                                byteBuffer.clear();
                                byteBuffer.put(JSON.toJSONBytes(response));
                                byteBuffer.flip();
                                socketChannel.write(byteBuffer);
                            }
                        }
                        socketChannel.close();
                    }else if (key.isConnectable()){

                    }else if (key.isReadable()){

                    }else if (key.isWritable()){

                    }
                    iterator.remove();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
