package test.BIO;

import java.io.IOException;
import java.util.Random;

/**
 * Created by LXX on 2019/2/27.
 */
public class StartClient {

    public static void main(String[] args){
        for (int i = 0;i < 10; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Client.send(String.valueOf(new Random().nextInt(10)));
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
