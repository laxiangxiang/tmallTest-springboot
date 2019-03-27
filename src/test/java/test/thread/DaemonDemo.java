package test.thread;

/**
 * Created by LXX on 2019/3/18.
 */
public class DaemonDemo {

    public static void main(String[] args) {
        Thread daemonThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println("i am alive");
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("finally block");
                    }
                }
            }
        });
        //设置守护线程要先于start()方法,
        // 否则会报java.lang.IllegalThreadStateException
        // 但是该线程还是会执行，只不过会当做正常的用户线程执行。
        daemonThread.setDaemon(true);
        daemonThread.start();
        //确保main线程结束前能给daemonThread能够分到时间片
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
