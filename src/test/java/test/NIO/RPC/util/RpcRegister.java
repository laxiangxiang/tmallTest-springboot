package test.NIO.RPC.util;

import java.util.HashMap;

/**
 * Created by LXX on 2019/2/25.
 * 注册服务提供者，注册中心实现类
 */
public class RpcRegister {

    private HashMap<String,Object> registMap = new HashMap<>();

    private static RpcRegister register = new RpcRegister();

    public static RpcRegister buildRegist(){
        return register;
    }

    public RpcRegister regist(String interfaceName,Object o){
        registMap.put(interfaceName,o);
        return this;
    }

    public Object findService(String interfaceName){
        return registMap.get(interfaceName);
    }
}
