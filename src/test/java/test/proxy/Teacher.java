package test.proxy;

/**
 * Created by LXX on 2019/3/11.
 */
public class Teacher implements People {

    @Override
    public void doSomething(String doSomething) {
        System.out.println("I am a teacher,I am "+doSomething);
    }
}
