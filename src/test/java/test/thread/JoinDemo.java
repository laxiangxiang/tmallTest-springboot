package test.thread;

/**
 * Created by LXX on 2019/3/18.
 */
public class JoinDemo {

    public static void main(String[] args) {
        Thread previousThread = Thread.currentThread();
        for (int i =0;i < 10; i++){
            Thread currentThread = new JoinThread(previousThread,i);
            currentThread.start();
            previousThread = currentThread;
        }


    }

    static class JoinThread extends Thread{
        private Thread previousThread;
        private int threadNum;
        public JoinThread(Thread previousThread,int threadNum) {
            this.previousThread = previousThread;
            this.threadNum = threadNum;
        }

        @Override
        public void run() {
            try {
                previousThread.join();
                System.out.println(previousThread.getName()+" terminated.");
                System.out.println(threadNum+":"+Thread.currentThread().getName()+" start.");
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
