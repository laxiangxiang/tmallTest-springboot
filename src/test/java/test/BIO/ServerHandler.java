package test.BIO;

import test.callbacktest.Calculator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by LXX on 2019/2/26.
 */
public class ServerHandler implements Runnable {

    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader bufferedReade = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true)){
            String expression;
            String result;
            while (true){
                if ((expression = bufferedReade.readLine()) == null){
                    break;
                }
                System.out.println(Thread.currentThread().getName()+"：服务器收到消息："+expression);
                printWriter.println(expression+"<>");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
