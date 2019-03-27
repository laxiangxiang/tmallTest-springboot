package test.proxy;

/**
 * Created by LXX on 2019/3/11.
 */
public class Student implements People{

    private static String doSomething ;

    @Override
    public void doSomething(String doSomething) {
        System.out.println("I am a student,I am doing "+doSomething);
    }
}
