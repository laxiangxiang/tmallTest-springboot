package test.volatiletest;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by LXX on 2019/2/27.
 */
public class VolatileTest {
    public volatile int a = 0;//volatile不保证原子性，只保证可见性和有序性

    public int b = 0;

    public int c = 0;
    private Lock lock = new ReentrantLock();

    public AtomicInteger d = new AtomicInteger(0);

    public static void main(String[] args)throws InterruptedException{
        VolatileTest volatileTest = new VolatileTest();
        for (int i = 0;i < 10;i++){
            new Thread(volatileTest.new TestThread()).start();
        }
        while (Thread.activeCount() > 2)//保证前面线程都执行完
            Thread.yield();
        System.out.println(volatileTest.a);
        System.out.println(volatileTest.b);
        System.out.println(volatileTest.c);
        System.out.println(volatileTest.d.get());
    }

    class TestThread implements Runnable{
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
            for (int i = 0;i<10000;i++){
                adda();
                addb();
                addc();
                addd();
            }
        }
    }

    public void adda(){
        a++;//不是原子性操作，
    }

    /**
     * 使用synchronized保证原子性，可见性，有序性
     * 或者使用lock
     */
    public synchronized void addb(){
        b++;
    }

    public void addc(){
        lock.lock();
        try {
            c++;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 使用原子类
     */
    public void addd(){
        d.getAndIncrement();
    }
}
