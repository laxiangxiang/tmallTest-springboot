package test.singleton;

/**
 * Created by LXX on 2019/2/28.
 * 双重检测同步延迟加载
 */
public class Singleton3 {

    private static volatile Singleton3 instance = null;

    private Singleton3(){};

    public static Singleton3 getInstance(){
        System.out.println(Thread.currentThread().getName()+"进入getInstance()方法");
        if (instance == null){
            synchronized (Singleton3.class){
                System.out.println(Thread.currentThread().getName()+"进入同步代码块");
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                if (instance == null){
                    /**
                     * 这段程序存在三个步骤
                     * 1.为单例对象分配内存空间
                     * 2.
                     */
                    instance = new Singleton3();
                }
            }
            System.out.println(Thread.currentThread().getName()+"结束同步代码块");
        }else {
            System.out.println(Thread.currentThread().getName()+"没有进入同步代码块");
        }
        return instance;
    }
}
