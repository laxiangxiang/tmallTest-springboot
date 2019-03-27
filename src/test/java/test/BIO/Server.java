package test.BIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by LXX on 2019/2/26.
 */
public class Server {

    private static int DEFAULT_PORT = 8090;

    private static ServerSocket serverSocket;

    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    public synchronized static void start() throws IOException{
        if (serverSocket != null){
            return;
        }
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("服务器已启动，端口号："+DEFAULT_PORT);
            int i = 1;
            while (true){
            Socket socket = serverSocket.accept();//等待连接，没有可以接受的连接会阻塞等待
            BufferedReader bufferedReade = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
            String expression;
            String result;
                if ((expression = bufferedReade.readLine()) == null){
                    break;
                }
                System.out.println("服务器收到消息："+expression);
                printWriter.println(expression+"<>");
//                new Thread(new ServerHandler(socket),String.valueOf(i)).start();//对每一个连接都创建一个线程处理（一个线程对应一个连接）
//                executorService.execute(new ServerHandler(socket));//使用线程池，FixedThreadPool我们就有效的控制了线程的最大数量，保证了系统有限的资源的控制，实现了N:M的伪异步I/O模型
                i++;
            }
        }finally {

        }
    }
}
