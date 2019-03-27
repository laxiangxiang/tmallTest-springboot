package test.NIO.RPC;

import test.NIO.RPC.client.CommonClient;
import test.NIO.RPC.client.DemoClientImpl;
import test.NIO.RPC.service.DemoServerImpl;
import test.NIO.RPC.service.RequestParser;
import test.NIO.RPC.util.RpcInitFactory;
import test.NIO.RPC.util.RpcRegister;

/**
 * Created by LXX on 2019/2/26.
 */
public class Start {

    public static void main(String[] args){
        startService();
        startClient();
    }

    public static void startService(){
        RequestParser parser = new RequestParser(8090);
        RpcRegister.buildRegist().regist(Idemo.class.getName(),new DemoServerImpl());
        new Thread(parser).start();
    }

    public static void startClient(){
        RpcInitFactory factory = new RpcInitFactory("localhost",8090);
        Idemo idemo = new DemoClientImpl(new CommonClient(factory));
        System.out.println(idemo.add(2,1));
    }
}
