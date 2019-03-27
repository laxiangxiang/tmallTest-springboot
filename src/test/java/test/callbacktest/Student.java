package test.callbacktest;

/**
 * Created by LXX on 2019/2/21.
 */
public class Student implements dojob{

    private String name = null;

    public Student(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void calcAdd(int a,int b){
        Calculator calculator = new Calculator();
        calculator.add(a,b,this);
    }

    public void fillBlank(int a,int b,int result){
        System.out.println(name+"使用计算器计算："+a+"+"+b+"="+result);
    }
}
