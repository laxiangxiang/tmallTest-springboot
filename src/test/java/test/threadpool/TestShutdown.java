package test.threadpool;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by LXX on 2019/3/5.
 */
public class TestShutdown {

    public static void main(String[] args){
        try {
//            testShutdown(111);
            testShutdownNow(222);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void testShutdown(int startNo) throws InterruptedException{
        ExecutorService executorService = new ThreadPoolExecutor(2,3,1000, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10));
        for (int i = 0;i < 10; i++){
            try {
                executorService.execute(getTask(i+startNo));
            }catch (RejectedExecutionException e){
                System.out.println("isShutdown:"+executorService.isShutdown());
            }
            if (i == 5){
                executorService.shutdown();
            }
        }
        //先把线程池状态设置为shutdown状态，线程池不会立即退出，直到添加到线程池中的任务全部执行完成，才会退出，但是不能再往池中添加任务，否则会抛出RejectedExecutionException。
//        executorService.shutdown();
        System.out.println(executorService.awaitTermination(1,TimeUnit.MINUTES));
    }

    public static void testShutdownNow(int startNo) throws InterruptedException{
        List<Runnable> tasks = null;
        ExecutorService executorService = new ThreadPoolExecutor(2,3,1000,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10));
        for (int i = 0; i < 10; i ++){
            try {
                executorService.execute(getTask(i+startNo));
            }catch (RejectedExecutionException e){
                System.out.println("isShutdown:"+executorService.isShutdown());
            }
            if (i == 5){
                tasks = executorService.shutdownNow();
            }
        }
        Thread.sleep(1000);
        //线程池的状态会变成stop状态，并试图停止所有正在执行的线程，不在处理还在队列中等待的任务，而是返回这些任务。不能再往池中添加任务，否则会抛出RejectedExecutionException。
        //它试图终止线程的方法是通过调用Thread.interrupt()方法来实现的，，如果线程中没有sleep(),wait(),Condition,定时锁等应用，线程池会等待所有正在执行的任务都执行完成了才能退出。
//        List<Runnable> tasks = executorService.shutdownNow();
        System.out.println(executorService.awaitTermination(1,TimeUnit.MINUTES));
        System.out.println(tasks.size());
        tasks.forEach(runnable -> runnable.run());
    }
    private static Runnable getTask(int columnIndex) {
        final Random random = new Random();
        final int no = columnIndex;
        Runnable task  = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6*1000);//shutdownNow()尝试中断正在执行的线程
                    System.out.println(Thread.currentThread().getName()+":"+no+"-->"+random.nextInt());
                }catch (InterruptedException e){//中断成功，抛出异常，在catch中继续执行任务
                    System.out.println(Thread.currentThread().getName()+":"+no+" has error ");
                    try {
                        Thread.sleep(1000);//会再次中断
                    }catch (InterruptedException e1){
                        System.out.println(Thread.currentThread().getName()+":"+no+" has error e1");
                    }
                }
            }
        };
        return task;
    }
}
