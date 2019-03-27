package test.AIO.server;

/**
 * Created by LXX on 2019/2/27.
 */
public class Server {

    private static int DEFAULT_PORT = 8090;

    private static AsynchronousServerHandle serverHandle;

    public volatile static long clientCount = 0;

    public static void start(){
        if (serverHandle != null){
            return;
        }
        start(DEFAULT_PORT);
    }

    public static void start(int port){
        serverHandle = new AsynchronousServerHandle(port);
        new Thread(serverHandle,"server").start();
    }
}
