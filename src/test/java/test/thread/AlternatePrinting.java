package test.thread;

/**
 * Created by LXX on 2019/3/18.
 * 两个线程交替打印偶数奇数
 */
public class AlternatePrinting {
    static class PrintThread implements Runnable{
        static int value = 0;

        @Override
        public void run() {
            while (value <= 100){
                synchronized (PrintThread.class){
                    System.out.println(Thread.currentThread().getName()+":"+value++);
                    PrintThread.class.notify();
                    try {
                        PrintThread.class.wait(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new PrintThread(),"偶数").start();
        new Thread(new PrintThread(),"奇数").start();
    }
}
