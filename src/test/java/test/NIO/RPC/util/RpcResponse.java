package test.NIO.RPC.util;

import lombok.Data;

/**
 * Created by LXX on 2019/2/25.
 */
@Data
public class RpcResponse {

    private String requestId;

    private Exception exception;

    private Object result;
}
