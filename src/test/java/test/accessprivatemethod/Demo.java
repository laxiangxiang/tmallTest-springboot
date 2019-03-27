package test.accessprivatemethod;

/**
 * Created by LXX on 2019/2/26.
 */
public class Demo {
    public void demoA(){
        System.out.println("access public method demo A");
    }

    private void demoB(int a,int b){
        System.out.println("access private method demo B with args,a:"+a+",b:"+b);
    }

    private static void demoC(int a,int b){
        System.out.println("access private method demo c with args,a:"+a+",b:"+b);
    }

    public class DemoInnerClass{
        private int a;
        private int b;
        private DemoInnerClass(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public void accessOutMethod(){
            demoA();
            demoB(a,b);
            demoC(a,b);
        }
    }
    public DemoInnerClass createInnerClass(int a, int b){
        return new DemoInnerClass(a,b);
    }
}
