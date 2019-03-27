package test.NIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by LXX on 2019/2/25.
 */
public class TestFileChannel {
    public static void main(String[] args){
        TestFileChannel testFileChannel = new TestFileChannel();
//        testFileChannel.writeFile();
        testFileChannel.readFile();

    }

    public void writeFile(){
        ByteBuffer byteBuffer;
        FileChannel fileChannel = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File("C:\\Users\\LXX\\Desktop\\a.txt"),false);
            fileChannel = fileOutputStream.getChannel();
            byteBuffer = ByteBuffer.wrap("hello world,你好".getBytes("utf-8"));
            System.out.println("position:"+byteBuffer.position()+",limit:"+byteBuffer.limit()+",capacity:"+byteBuffer.capacity()+",mark:"+byteBuffer.mark());
            //byteBuffer.flip();//读写转换,这里不需要
            System.out.println("position:"+byteBuffer.position()+",limit:"+byteBuffer.limit()+",capacity:"+byteBuffer.capacity()+",mark:"+byteBuffer.mark());
            fileChannel.write(byteBuffer);
            byteBuffer.clear();
            System.out.println("position:"+byteBuffer.position()+",limit:"+byteBuffer.limit()+",capacity:"+byteBuffer.capacity()+",mark:"+byteBuffer.mark());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                fileChannel.close();
                fileOutputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void readFile(){
        FileInputStream fileInputStream = null;
        FileChannel fileChannel = null;
        ByteBuffer byteBuffer;
        try {
            fileInputStream = new FileInputStream(new File("C:\\Users\\LXX\\Desktop\\a.txt"));
            fileChannel = fileInputStream.getChannel();
            byteBuffer = ByteBuffer.allocate(5);
            System.out.println("position:"+byteBuffer.position()+",limit:"+byteBuffer.limit()+",capacity:"+byteBuffer.capacity()+",mark:"+byteBuffer.mark());
            //方法一
//            int result = fileChannel.read(byteBuffer);
//            System.out.println("position:"+byteBuffer.position()+",limit:"+byteBuffer.limit()+",capacity:"+byteBuffer.capacity()+",mark:"+byteBuffer.mark());
//            byteBuffer.flip();
//            while (byteBuffer.hasRemaining()){
//                System.out.println(new String(byteBuffer.array()));
//                System.out.println("position:"+byteBuffer.position()+",limit:"+byteBuffer.limit()+",capacity:"+byteBuffer.capacity()+",mark:"+byteBuffer.mark());
//                fileChannel.read(byteBuffer);
//                System.out.println("position:"+byteBuffer.position()+",limit:"+byteBuffer.limit()+",capacity:"+byteBuffer.capacity()+",mark:"+byteBuffer.mark());
//                byteBuffer.flip();
//                System.out.println("position:"+byteBuffer.position()+",limit:"+byteBuffer.limit()+",capacity:"+byteBuffer.capacity()+",mark:"+byteBuffer.mark());
//            }
            //方法二
            while (fileChannel.read(byteBuffer) != -1){
                byteBuffer.flip();
                System.out.println(new String(byteBuffer.array()));
                while (byteBuffer.hasRemaining()){
                    System.out.println((char)byteBuffer.get());
                }
                byteBuffer.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
