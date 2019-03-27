package test.lambda;

/**
 * Created by LXX on 2019/2/25.
 */
public class Closure {
    public static void main(String[] args){
        Closure closure = new Closure();
        InnerClosure1 innerClosure1 = closure.createInnerClosure1(10);
        InnerClosure2 innerClosure2 = closure.createInnerClosure2(10);
        for (int i =  0;i <= 10;i++){
            System.out.println(innerClosure1.increment(i));
            System.out.println(innerClosure2.decrease(i));
        }
    }

    private void log(){
        System.out.println("in log...");
    }

    public class InnerClosure1 {
        private final int initial;
        private InnerClosure1(int initial) {

            this.initial = initial;
        }
        public int increment(int i){
            log();
            return initial+i;
        }
    }

    public class InnerClosure2{
        private final int initial;
        private InnerClosure2(int initial) {
            this.initial = initial;
        }

        public int decrease(int i){
            log();
            return initial-i;
        }
    }
    public InnerClosure2 createInnerClosure2(int initial){
        return new InnerClosure2(initial);
    }
    public InnerClosure1 createInnerClosure1(int initial){
        return new InnerClosure1(initial);
    }
}
