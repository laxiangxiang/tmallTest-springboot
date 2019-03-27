package test.singleton;

/**
 * Created by LXX on 2019/2/28.
 * 使用内部类实现延迟加载
 * 利用的原理是：一个类直到被使用时才被初始化，而类初始化的过程是非并行的，这些都由JLS保证。
 */
public class Singleton4 {

    private Singleton4(){};

    public static Singleton4 getInstance(){
        return Holder.instance;
    }

    private static class Holder{
        private static Singleton4 instance = new Singleton4();
    }
}
