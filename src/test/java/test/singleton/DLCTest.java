package test.singleton;

/**
 * Created by LXX on 2019/3/15.
 */
public class DLCTest {

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Singleton3.getInstance();
            }
        },"thread1");
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Singleton3.getInstance();
            }
        },"thread2");
        thread1.start();
        thread2.start();
    }
}
