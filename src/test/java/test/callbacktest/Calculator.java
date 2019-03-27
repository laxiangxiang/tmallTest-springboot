package test.callbacktest;

/**
 * Created by LXX on 2019/2/21.
 */
public class Calculator {

    public void add(int a,int b,dojob dojob){
        int result = a + b;
        dojob.fillBlank(a,b,result);
    }
}
