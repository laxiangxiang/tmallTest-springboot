package test.volatiletest;

/**
 * Created by LXX on 2019/3/1.
 */
public class LongAtomicTest implements Runnable{

    private static long field = 0;

    private volatile long value;

    public long getValue(){
        return value;
    }

    public void setValue(long value){
        this.value = value;
    }

    public LongAtomicTest(long value){
        this.setValue(value);
    }

    @Override
    public void run() {
        int i = 0;
        while (i < 100000){
            LongAtomicTest.field = this.getValue();
            i++;
            long temp = LongAtomicTest.field;
            System.out.println(temp);
            if (temp != 1L && temp != -1L){
                System.out.println("出现错误"+temp);
                System.exit(0);
            }
        }
        System.out.println("运行真确");
    }

    public static void main(String[] args)throws InterruptedException{
        String arch = System.getProperty("sun.arch.data.model");
        System.out.println(arch+"-bit");
        LongAtomicTest t1 = new LongAtomicTest(1);
        LongAtomicTest t2 = new LongAtomicTest(-1);
        Thread T1 = new Thread(t1);
        Thread T2 = new Thread(t2);
        T1.start();
        T2.start();
        T1.join();
        T2.join();
    }
}
