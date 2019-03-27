package test.lambda;

/**
 * Created by LXX on 2019/2/25.
 */
public class Test {

    public static void main(String[] args){
        Closure closure = new Closure();
        closure.createInnerClosure1(10).increment(1);
//        closure.new InnerClosure1(10).increment(1);
    }
}
