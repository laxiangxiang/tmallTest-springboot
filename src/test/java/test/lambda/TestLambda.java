package test.lambda;

import java.util.function.Supplier;

/**
 * Created by LXX on 2019/2/22.
 */
public class TestLambda {

    private int a = 1;

    public static void main(String[] args){
        System.out.println(test().get());
        System.out.println(test2().get());
        new TestLambda().in();
    }

    public void in(){
        new inner().print();
    }
    public static Supplier<Integer> test(){
        int i = 1;
        return new Supplier<Integer>() {
            @Override
            public Integer get() {
                return i;
            }
        };
    }

    public static Supplier<Integer> test2(){
        int i = 2;
        return () -> {
            return i;
        };
    }

    public static Supplier<Integer> test3(){
        int i = 3;
        i ++;
        return () -> {
//            return i;
            return null;
        };
    }

    private class inner{
        private int b = ++a;

        public void print(){
            System.out.println(b);
        }
    }
}
