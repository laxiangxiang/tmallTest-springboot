package test.volatiletest;

import java.io.File;

/**
 * Created by LXX on 2019/3/22.
 * 线程的缓存何时刷新？
 */
public class Demonstration extends Thread{
    boolean keepRunning = true;

    public static void main(String[] args)  throws InterruptedException{
        Demonstration demonstration = new Demonstration();
        demonstration.start();
        System.out.println("start:"+demonstration.keepRunning);
        Thread.sleep(1000);
        demonstration.keepRunning = false;
        System.out.println("end:"+demonstration.keepRunning);
    }

    @Override
    public void run() {
        int x = 1;
        while (keepRunning){
            /**
             * 因为：println是同步方法，加了Synchronized：
             * 当线程释放一个锁时会强制性的将工作内存中之前所有的写操作都刷新到主内存中去，
             * 而获取一个锁则会强制性的加载可访问到的值到线程工作内存中来。
             * 虽然锁操作只对同步方法和同步代码块这一块起到作用，但是影响的却是线程执行操作所使用的所有字段。
             * 结论：（非volatile变量）当进行IO操作或者线程内部调用synchronized修饰的方法或者同步代码块时，线程的缓存会进行刷新，也就是会感知到共享变量的变化。
             */
//            System.out.println("如果你不注释这一行，程序会正常停止！");
//            synchronized (this){
//
//            }
            File file = new File("a.txt");
            x++;
        }
        System.out.println("x:"+x);
    }
}
