package test.NIO.RPC.util;

import lombok.Data;

import java.util.Arrays;

/**
 * Created by LXX on 2019/2/25.
 */
@Data
public class RpcRequest {

    private String requestId;

    private String serviceVersion;

    //请求接口名称
    private String interfaceName;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameters;

    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestId='" + requestId + '\'' +
                ", serviceVersion='" + serviceVersion + '\'' +
                ", interfaceName='" + interfaceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
