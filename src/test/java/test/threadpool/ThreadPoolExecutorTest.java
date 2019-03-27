package test.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by LXX on 2019/3/5.
 */
public class ThreadPoolExecutorTest {

    public static void main(String[] args){
//        cachedThreadPool();
//        fixedThreadPool();
//        scheduledThreadPool();
        singleThreadExecutor();
    }

    /**
     * 可缓存线程池，先查看池中有没有以前建立的线程，如果有并且可用时会只用以前的线程，如果没有可用的线程，将创建一个新线程并添加到池中。
     * 未使用60秒的线程将被终止并从池中删除。因此，长时间保持闲置的池将不会消耗任何资源。
     */
    public static void cachedThreadPool(){
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0;i<10;i++){
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            int j = i;
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+":"+j+"正在被执行");
                }
            });
        }
        cachedThreadPool.shutdown();
    }

    /**
     * 可重用固定个数的线程池，如果池中的线程都处于运行状态，则会将任务保存到无界队列中。
     */
    public static void fixedThreadPool(){
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        for (int i = 0;i < 10; i++){
            int j = i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName()+" "+j+" 正在被执行");
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            });
        }
        fixedThreadPool.shutdown();
    }

    public static void scheduledThreadPool(){
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);
        //延迟一秒执行
        scheduledThreadPool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+" 延迟一秒执行");
            }
        },1, TimeUnit.SECONDS);
        //延迟1秒后每隔3秒执行一次
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+" 延迟1秒后每隔3秒执行一次");
            }
        },1,3,TimeUnit.SECONDS);
        scheduledThreadPool.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+" 延迟执行");
            }
        },2,3,TimeUnit.SECONDS);
        scheduledThreadPool.shutdown();
    }

    /**
     *创建一个使用无界队列的单个线程的线程池，任务保证顺序执行，并且任何时候都不超过一个任务被激活。
     * 如果这个单个线程在执行过程中发生故障而终止，如果需要执行后继任务，会创建新的线程。
     */
    public static void singleThreadExecutor(){
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0;i < 10; i++){
            final int index = i;
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName()+" 正在被执行，打印值是："+index);
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
        singleThreadExecutor.shutdown();
    }

}
