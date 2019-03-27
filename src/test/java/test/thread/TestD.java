package test.thread;

/**
 * Created by LXX on 2019/2/22.
 */
public class TestD {

    public String name;

    public static void main(String[] args){
        new Thread(new ThreadA()).start();
        try {
            Thread.sleep(5000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        new Thread(new ThreadB()).start();
    }

    static class ThreadA implements Runnable{
        @Override
        public void run() {
            synchronized (TestD.class){
                System.out.println("enter thread A...");
                System.out.println("A is sleeping...");
                try {
                    //调用wait()，线程会放弃对象锁，进入等待此对象的等待锁定池
                    TestD.class.wait();
                    System.out.println("A awake");
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println("A is going on...");
                System.out.println("A is over");
            }
        }
    }

    static class ThreadB implements Runnable{
        @Override
        public void run() {
            synchronized (TestD.class){
                System.out.println("enter thread B...");
                System.out.println("B is sleeping...");
                try {
                    //调用notify()方法，是为了唤醒等待对象锁的线程,参与锁竞争
                    TestD.class.notify();
                    //sleep()方法导致了程序暂停执行指定的时间，让出cpu该其他线程，
                    //但是他的监控状态依然保持者，当指定的时间到了又会自动恢复运行状态。
                    //在调用sleep()方法的过程中，线程不会释放对象锁。
                    Thread.sleep(1000);
                    System.out.println("B awake");
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            System.out.println("B is going on...");
            System.out.println("B is over");
        }
    }
}
