package test.accessprivatemethod;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by LXX on 2019/2/26.
 */
public class AccessPrivateMethod {

    public static void main(String[] args){
        AccessPrivateMethod accessPrivateMethod = new AccessPrivateMethod();
        try {
            accessPrivateMethod.javaReflection();
            accessPrivateMethod.innerClass();
            accessPrivateMethod.methodHandler();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
    /**
     * 使用java反射访问private方法
     */
    public void javaReflection() throws ClassNotFoundException,NoSuchMethodException,InstantiationException,IllegalAccessException,InvocationTargetException{
        Class<?> targetClass = Class.forName("test.accessprivatemethod.Demo");
        Class<?>[] args = new Class[]{int.class,int.class};
        Method methodA = targetClass.getMethod("demoA");
        Method methodB = Demo.class.getDeclaredMethod("demoB",args);
        Method methodC = targetClass.getDeclaredMethod("demoC",args);
        methodB.setAccessible(true);//访问私有方法
        methodC.setAccessible(true);
        methodA.invoke(targetClass.newInstance());
        methodB.invoke(targetClass.newInstance(),1,2);
        methodC.invoke(targetClass.newInstance(),1,2);
    }

    /**
     * 使用内部类可以访问外部类的私有的属性和方法
     */
    public void innerClass(){
        Demo.DemoInnerClass innerClass = new Demo().createInnerClass(1,2);
        innerClass.accessOutMethod();
    }

    public void methodHandler() throws ClassNotFoundException,NoSuchMethodException,IllegalAccessException,Throwable{
        MethodType mtB = MethodType.methodType(int.class,int.class,int.class);
        MethodType mtA = MethodType.methodType(void.class);
        MethodHandles.Lookup lk = MethodHandles.lookup();
        MethodHandle mhA = lk.findVirtual(Class.forName("test.accessprivatemethod.Demo"),"demoA",mtA);
        mhA.invoke(Class.forName("test.accessprivatemethod.Demo").newInstance());
        MethodHandle mhB = lk.findVirtual(Class.forName("test.accessprivatemethod.Demo"),"demoB",mtB);
        System.out.println(mhB.invokeExact(Class.forName("test.accessprivatemethod.Demo").newInstance(),1,2));
    }
}
