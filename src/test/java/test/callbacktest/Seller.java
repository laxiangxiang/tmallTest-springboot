package test.callbacktest;

/**
 * Created by LXX on 2019/2/21.
 */
public class Seller implements dojob{

    private String name = null;

    public Seller(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void fillBlank(int a, int b, int result) {
        System.out.println(name+"使用计算器算账："+a+"+"+b+"="+result);
    }

    public void calc(int a,int b){
        new Calculator().add(a,b,this);
    }
}
