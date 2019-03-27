package test.NIO.RPC.client;

import test.NIO.RPC.Idemo;
import test.NIO.RPC.util.RpcRequest;
import test.NIO.RPC.util.RpcResponse;

/**
 * Created by LXX on 2019/2/26.
 * 客户端实例
 */
public class DemoClientImpl implements Idemo {

    private CommonClient client;

    public DemoClientImpl(CommonClient client) {
        this.client = client;
    }

    @Override
    public Integer add(Integer i, Integer j) {
        RpcRequest request = new RpcRequest();
        request.setServiceVersion("123");
        request.setInterfaceName(Idemo.class.getName());
        request.setMethodName("add");
        request.setParameters(new Integer[] {i,j});
        request.setParameterTypes(new Class[] {Integer.class,Integer.class});
        RpcResponse response = client.request(request);
        if (null != response){
            return Integer.parseInt(response.getResult().toString());
        }
        return null;
    }
}
