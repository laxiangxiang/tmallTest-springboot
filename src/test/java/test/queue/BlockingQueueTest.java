package test.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by LXX on 2019/3/15.
 */
public class BlockingQueueTest {

    private static ArrayList<Integer> list = new ArrayList<Integer>(){
        {
            add(1);
            add(2);
            add(3);
            add(4);
        }
    };

    private static ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(5,true,list);

    private static SynchronousQueue synchronousQueue = new SynchronousQueue();

    private static PriorityBlockingQueue<Student> priorityBlockingQueue = new PriorityBlockingQueue();

    private static volatile boolean isOver = false;
    public static void main(String[] args) {
//        synchronousQueueTest();
        priorityBlockingQueueTest();
    }

    public static void synchronousQueueTest(){
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!isOver){
                        Thread.sleep(1000);
//                        synchronousQueue.put(1);
                        synchronousQueue.offer(1,2000, TimeUnit.MILLISECONDS);
                        System.out.println(Thread.currentThread().getName()+"put one");
                    }
                    System.out.println(Thread.currentThread().getName()+" is over");
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        },"thread1");
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!isOver){
                        Thread.sleep(1000);
//                        synchronousQueue.take();
                        synchronousQueue.poll(2000,TimeUnit.MILLISECONDS);
                        System.out.println(Thread.currentThread().getName()+"take one");
                    }
                    System.out.println(Thread.currentThread().getName()+" is over");
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        },"thread2");
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!isOver){
                        Thread.sleep(1000);
//                        synchronousQueue.take();
                        synchronousQueue.poll(2000,TimeUnit.MILLISECONDS);
                        System.out.println(Thread.currentThread().getName()+"take one");
                    }
                    System.out.println(Thread.currentThread().getName()+" is over");
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        },"thread3");
        thread1.start();
        thread2.start();
        thread3.start();
        try {
            Thread.sleep(6000);
            isOver = true;
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void priorityBlockingQueueTest(){
        priorityBlockingQueue.put(new Student("1",1));
        priorityBlockingQueue.put(new Student("1",0));
        priorityBlockingQueue.put(new Student("1",5));
        priorityBlockingQueue.put(new Student("1",4));
        priorityBlockingQueue.put(new Student("1",9));
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (priorityBlockingQueue.size() != 0){
                        System.out.println(Thread.currentThread().getName()+":"+priorityBlockingQueue.take());
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        },"thread1");
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (priorityBlockingQueue.size() != 0){
                        System.out.println(Thread.currentThread().getName()+":"+priorityBlockingQueue.take());
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        },"thread2");
        thread1.start();
        thread2.start();
    }
}
