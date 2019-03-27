package test.volatiletest;

/**
 * Created by LXX on 2019/3/22.
 */
public class FakeSharing implements  Runnable{

    private final static int THREAD_NUM = 2;

    private final static long CYCLES = 500L*1000L*1000L;

    private final int index;

    private static VolatileLong[] volatileLongs = new VolatileLong[THREAD_NUM];

    static {
        for (int i = 0;i < volatileLongs.length; i++){
            volatileLongs[i] = new VolatileLong();
        }
    }

    public FakeSharing(int index) {
        this.index = index;
    }

    public static void main(String[] args) throws InterruptedException{
        long start = System.nanoTime();
        runTest();
        System.out.println("duration="+(System.nanoTime()-start));
    }

    private static void runTest() throws InterruptedException{
        Thread[] threads = new Thread[THREAD_NUM];
        for (int i = 0; i<threads.length; i++){
            threads[i] = new Thread(new FakeSharing(i));
        }
        for (Thread thread : threads){
            thread.start();
        }
        for (Thread thread : threads){
            thread.join();
        }
    }

    @Override
    public void run() {
        long i = CYCLES + 1;
        while (0 != --i){
            volatileLongs[index].value = i;
        }
    }

    /**
     * 包装后的value对象，这样一个VolatileLong无论什么情况下都可以占满一个缓存行；
     * 一般来说CPU缓存行是64个字节，是CPU缓存的最小单位。
     * long类型占64位，一个缓存行可以存8个long类型数据。
     * 这样如果对volatile修饰的value变量修改后，其它加载了value变量的缓存行立即失效，
     * 无效的缓存行中不包含其它缓存的数据，从而避免重新加载其它数据的开销。
     */
    private final static class VolatileLong{
        public long p1,p2,p3,p4,p5,p6p,p7;//填充

        public volatile long value = 0;

        public long p9,p10,p11,p12,p13,p14,p15;//填充
    }
}
