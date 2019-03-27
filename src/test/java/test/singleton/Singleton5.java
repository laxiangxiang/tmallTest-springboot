package test.singleton;

/**
 * Created by LXX on 2019/2/28.
 */
public class Singleton5 {
    private static final ThreadLocal perThreadInstance = new ThreadLocal();
    private static Singleton5 singleton5;
    private Singleton5(){};

    public static Singleton5 getInstance(){
        if (perThreadInstance.get() == null){
            //每个线程第一次都会调用
            synchronized (Singleton5.class){
                if (singleton5 == null){
                    singleton5 = new Singleton5();
                }
            }
            perThreadInstance.set(singleton5);
        }
        return singleton5;
    }
}
