package test.AIO;

import test.AIO.client.Client;

import java.util.Scanner;

/**
 * Created by LXX on 2019/2/27.
 */
public class StartClient {

    public static void main(String[] args){
        Client client = new Client();
        client.start();
        System.out.println("请输入请求消息：");
        Scanner scanner = new Scanner(System.in);
        while(client.sendMsg(scanner.nextLine()));
    }
}
