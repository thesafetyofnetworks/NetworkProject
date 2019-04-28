package Client;

import java.io.IOException;
import java.io.InputStream;
import DES.DES_Method;

/**
 * 读取输入流线程
 * @author Administrator
 *
 */
public class ThreadReader implements Runnable{

    private String key="11111111111111111111111111111111111111111111111111111000";
    //private static int HEAD_SIZE=5;//传输最大字节长度
    //private static int BUFFER_SIZE=10;//每次读取10个字节
    private InputStream is;
    public ThreadReader(InputStream is) {
        this.is = is;
    }

    @Override
    public void run() {
        String keys[]= DES_Method.Produce_keys(key);
        String dekeys[]=new String[16];
        for(int i=0;i<16;i++){
            dekeys[i]=keys[15-i];
        }
        try {
            String message="";
           // while(true){
                byte[] b = new byte[1024];
                int length = is.read(b);
                message+= new String(b,0,length);
                System.out.println("获取到加密信息："+message);
                String M=DES_Method.DES_dencry(message,dekeys);
                System.out.println("信息解密为"+M);

         //   }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}