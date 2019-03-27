package test.classload;

/**
 * Created by LXX on 2019/3/21.
 */
public class ClassLoadTest {
    private static ClassLoadTest test = new ClassLoadTest();

    static int x;
    static int y = 0;

    public ClassLoadTest() {
        x++;
        y++;
    }

    public static void main(String[] args) {
        System.out.println(test.x);
        System.out.println(test.y);
    }
}
