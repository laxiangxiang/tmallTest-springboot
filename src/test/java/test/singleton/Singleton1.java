package test.singleton;

/**
 * Created by LXX on 2019/2/28.
 * 非延迟加载单例模式、饿汗模式
 */
public class Singleton1 {

    private Singleton1(){};

    private static final Singleton1 instance = new Singleton1();

    public static Singleton1 geInstance(){
        return instance;
    }
}
