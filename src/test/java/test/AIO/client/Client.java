package test.AIO.client;

/**
 * Created by LXX on 2019/2/27.
 */
public class Client {

    private static String DEFAULT_HOST = "127.0.0.1";

    private static int DEFAULT_PORT = 8090;

    private AsyncClientHandler clientHandler;

    public void start(){
        start(DEFAULT_HOST,DEFAULT_PORT);
    }

    private void start(String ip,int port){
        if (clientHandler != null){
            return;
        }
        clientHandler = new AsyncClientHandler(ip,port);
        new Thread(clientHandler,"client").start();
}

    public boolean sendMsg(String msg){
        if (msg.equals("q"))
            return false;
        clientHandler.sendMsg(msg);
        return true;
    }


}
