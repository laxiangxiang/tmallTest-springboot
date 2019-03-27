package test.singleton;

/**
 * Created by LXX on 2019/2/28.
 */
public class Singleton2 {

    private static Singleton2 instance = null;

    private Singleton2(){};

    /**
     * 延时加载,懒汉模式
     * @return
     */
    public static Singleton2 getInstance(){
        if (instance == null){
            instance = new Singleton2();
        }
        return instance;
    }

    /**
     * 同步延时加载模式
     * @return
     */
    public static synchronized Singleton2 getInstanceSynchronize(){
        if (instance == null){
            instance = new Singleton2();
        }
        return instance;
    }
}
