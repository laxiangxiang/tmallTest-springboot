package test;

/**
 * Created by LXX on 2019/2/20.
 */
public class MainTest {

    public static void main(String[] args){
        int a = 55;
        int b = -55;
        System.out.println("a:"+Integer.toBinaryString(a));
        System.out.println("b:"+Integer.toBinaryString(b));
        a = a >> 2;
        b = b >> 2;
        int c = b >>> 2;
        System.out.println("a:"+Integer.toBinaryString(a));
        System.out.println("b:"+Integer.toBinaryString(b));
        System.out.println("C:"+Integer.toBinaryString(c));
        System.out.println("a:"+a);
        System.out.println("b:"+b);
        System.out.println("c:"+c);

        System.out.println(tableSizeFor(10));
    }

    public static final int tableSizeFor(int cap) {
        int MAXIMUM_CAPACITY = 1 << 30;
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
}
