package test.BIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by LXX on 2019/2/26.
 */
public class Client {

    private static int DEFAULT_SERVER_PORT = 8090;
    private static String DEFAULT_SERVER_IP = "127.0.0.1";

    public static void send(String expression) throws IOException{
        System.out.println("client generate expression:"+expression);
        try (Socket socket = new Socket(DEFAULT_SERVER_IP,DEFAULT_SERVER_PORT);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true)){
            printWriter.println(expression);
            System.out.println("收到服务器消息："+bufferedReader.readLine());
        }
    }
}
