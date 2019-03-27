package test.NIO.RPC.service;

import test.NIO.RPC.Idemo;

/**
 * Created by LXX on 2019/2/26.
 * 服务端提供具体服务实例
 */
public class DemoServerImpl implements Idemo {
    @Override
    public Integer add(Integer i, Integer j) {
        return i+j;
    }
}
