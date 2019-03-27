package test.thread;

/**
 * Created by LXX on 2019/3/18.
 */
public class InterruptDemo {
    static boolean isEnd = false;
    public static void main(String[] args) {
        final Thread sleepThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        Thread busyThread = new Thread(){
            @Override
            public void run() {
                while (!isEnd);
            }
        };

        sleepThread.start();
        busyThread.start();
        sleepThread.interrupt();
        busyThread.interrupt();
        while (sleepThread.isInterrupted()){
            System.out.println("sleepThread interrupted");
        };
        System.out.println("sleepThread isInterrupted: " + sleepThread.isInterrupted());
        System.out.println("busyThread isInterrupted: " + busyThread.isInterrupted());
        System.out.println(busyThread.getState());
        isEnd = true;
        try {
            busyThread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println(busyThread.getState());
    }
}
