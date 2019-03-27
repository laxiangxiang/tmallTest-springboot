package test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by LXX on 2019/3/18.
 * 通过N个线程顺序循环打印从0至100
 *  Semaphore主要用于控制当前活动线程数目，就如同停车场系统一般，
 * 而Semaphore则相当于看守的人，用于控制总共允许停车的停车位的个数，
 * 而对于每辆车来说就如同一个线程，线程需要通过acquire()方法获取许可，而release()释放许可。
 * 如果许可数达到最大活动数，那么调用acquire()之后，便进入等待队列，等待已获得许可的线程释放许可，从而使得多线程能够合理的运行。
 */
public class ThreadLoopPrint {

    public static final int THREAD_NUMBER = 3;

    public static final int MAX_COUNT = 100;

    private static int count = 0;

    public static void main(String[] args) {
        Semaphore[] semaphores = new Semaphore[THREAD_NUMBER];
        for (int i = 0; i < THREAD_NUMBER; i++){
            if (i == 0){
                semaphores[i] = new Semaphore(1);
            }else {
                semaphores[i] = new Semaphore(0);
            }
        }
        for (int i = 0; i < THREAD_NUMBER; i++){
            new Thread(new TestThread(semaphores,i)).start();
        }
    }

    public static class TestThread implements Runnable{

        private Semaphore[] semaphores;

        private int number = 0;

        public TestThread(Semaphore[] semaphores, int number) {
            this.semaphores = semaphores;
            this.number = number;
        }

        @Override
        public void run() {
            try {
                while (true){
                    semaphores[number].acquire();
                    if (count >= MAX_COUNT){
                        System.exit(0);
                        break;
                    }
                    System.out.println(Thread.currentThread().getName()+":"+count);
                    count++;
                    int current = number + 1;
                    if (current >= THREAD_NUMBER){
                        current = 0;
                    }
                    semaphores[current].release();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
