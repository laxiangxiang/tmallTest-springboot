package test.springlifecycle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by LXX on 2019/3/6.
 */
public class BeanLifeCycle {

    public static void main(String[] args){
        System.out.println("现在开始初始化容器");
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        System.out.println("容器初始化成功");
        Person person = context.getBean("person",Person.class);
        System.out.println(person);
        System.out.println("现在开始关闭容器");
        ((ClassPathXmlApplicationContext)context).registerShutdownHook();
    }
}
