package test.serializable;

import java.io.*;

/**
 * Created by LXX on 2019/2/22.
 */
public class SerializableTest {

    public static void main(String[] args){
        try {
            out();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            in();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void out() throws FileNotFoundException,IOException{
        OutputStream outputStream = new FileOutputStream(new File("C:\\Users\\LXX\\Desktop\\person.txt"));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(new Person("123",12,"male","3333"));
    }

    private static void in() throws FileNotFoundException,IOException,ClassNotFoundException{
        InputStream inputStream = new FileInputStream("C:\\Users\\LXX\\Desktop\\person.txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Person person = (Person) objectInputStream.readObject();
        System.out.println(person);
    }
}
