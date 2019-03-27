package test.proxy;

import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by LXX on 2019/3/11.
 */
public class ProxyTest {

    public static void main(String[] args) throws Exception{
        //java reflect
        new PeopleProxy("Teacher").doSomething("home work");
        //java proxy
        People proxy = (People)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                new Class[]{People.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("before"+proxy.getClass().getName()+"doing "+args[0]);
                        Object result = method.invoke(new Student(),args);
                        System.out.println("after"+proxy.getClass().getName()+"doing "+args[0]);
                        return result;
                    }
                });
        proxy.doSomething("home work");
        byte[] bytes = ProxyGenerator.generateProxyClass("People$proxy",new Class[]{People.class});
        String fileName = System.getProperty("user.dir")+"/target/People$proxy.class";
        File file = new File(fileName);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
}
