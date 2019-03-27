package test.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by LXX on 2019/3/11.
 */
public class PeopleProxy implements People{

    private String proxyClassName;

    public PeopleProxy(String proxyClassName) {
        this.proxyClassName = proxyClassName;
    }

    @Override
    public void doSomething(String doSomething) {
        try {
            System.out.println("before student "+doSomething);
            Method method = Class.forName(this.getClass().getPackage().getName()+"."+proxyClassName).getMethod("doSomething", String.class);
            method.invoke(Class.forName(this.getClass().getPackage().getName()+"."+proxyClassName).newInstance(),doSomething);
            System.out.println("after student "+doSomething);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
