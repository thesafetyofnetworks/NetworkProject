package Server;

import DES.DES_Method;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * 发送数据线程
 * @author Administrator
 *
 */
public class ThreadWriter implements Runnable{

    private String key="11111111111111111111111111111111111111111111111111111000";
    private OutputStream os;
    public ThreadWriter(OutputStream os) {
        this.os = os;
    }
    @Override
    public void run() {
        String keys[]=DES_Method.Produce_keys("11111111111111111111111111111111111111111111111111111000");
        // int coode=M.hashCode();
        try {
            Scanner sc = new Scanner(System.in);
            while(true){
                System.out.println("server->client"+"：");
                String message = sc.next();
                String C=DES_Method.DES_encry(message,keys);
                System.out.println("信息加密为："+C);
                os.write(C.getBytes());
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}