package test.queue;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by LXX on 2019/3/15.
 */
@Data
@AllArgsConstructor
public class Student implements Comparable<Student> {

    private String name;

    private int score;

    @Override
    public int compareTo(Student o) {
//        return this.score-o.getScore();//从大到小
        return o.getScore()-this.score;//从小到大
    }
}
