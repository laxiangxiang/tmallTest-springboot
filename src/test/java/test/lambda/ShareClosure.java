package test.lambda;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LXX on 2019/2/25.
 */
public class ShareClosure {

    List<Action> actions = new ArrayList<>();

    public void input(){
        for (int i = 0;i < 10;i++){
            /**
             * 因为 i 变量在各个匿名内部类中使用(lambda表达式同理)，这里产生了闭包共享，java编译器会强制要求传入匿名内部类中的变量添加final
             * 这里java会隐式地将copy定义为final类型
             * 这里的lambda表达式就体现了闭包。
             */
            int copy = i;
            actions.add(new Action() {
                @Override
                public void run() {
                    System.out.println(copy);
                }
            });

            actions.add(()-> System.out.println(copy));
        }
    }

    public void ouPut(){
        for (Action action : actions){
            action.run();
        }
    }

    public static void main(String[] args){
        ShareClosure shareClosure = new ShareClosure();
        shareClosure.input();
        shareClosure.ouPut();
    }
}
