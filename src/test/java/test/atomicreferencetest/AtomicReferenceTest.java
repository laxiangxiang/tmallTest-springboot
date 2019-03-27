package test.atomicreferencetest;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by LXX on 2019/3/14.
 * 可重入自旋锁
 */
public class AtomicReferenceTest {

    public static void main(String[] args) {
        AtomicReferenceTest test = new AtomicReferenceTest();
        for (int i = 0;i < 3;i++){
            new Thread(test.new TestThread(),String.valueOf(i)).start();
        }
    }
    private AtomicReference<Thread> cas = new AtomicReference<>();
    private int count;
    public void lock(){
        Thread current = Thread.currentThread();
        // 如果当前线程已经获取到了锁，线程数增加一，然后返回
        if (current == cas.get()){
            count++;
            return;
        }
        while (!cas.compareAndSet(null,current)){
            System.out.println(current.getName()+"获取锁失败");
        }
        System.out.println(current.getName()+"获取所成功");
    }

    public void unlock(){
        Thread current = Thread.currentThread();
        // 如果大于0，表示当前线程多次获取了该锁，释放锁通过count减一来模拟
        if (current == cas.get()){
            if (count > 0){
                count--;
            }else {
                // 如果count==0，可以将锁释放，这样就能保证获取锁的次数与释放锁的次数是一致的了。
                cas.compareAndSet(current,null);
                System.out.println(current.getName()+"解锁成功");
            }
        }
    }

    public class TestThread implements Runnable{
        @Override
        public void run() {
            try {
                lock();
                Thread.sleep(100);
                unlock();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
