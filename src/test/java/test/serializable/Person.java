package test.serializable;

import java.io.Serializable;

/**
 * Created by LXX on 2019/2/22.
 */
public class Person implements Serializable{

    private static final long serialVersionUID = -6152908024889886098l;

    //静态变量不会被序列化
    private static int id = 2;

    private String name;

    private int age;

    private String sex;

    //不需要序列化
    transient private String password;

    public Person(String name, int age,String sex,String password) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

