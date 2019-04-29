package Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 服务器
 * @author Administrator
 *
 */
public class Server {

    private int duankou = 9000;//端口号
    private ServerSocket server;//声明服务器
    private static Socket socket;//声明客户端
    private String serName;
    public Server(){
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 创建服务器，开始监听
     */
    private void init() throws IOException {
        server = new ServerSocket(duankou);
        System.out.println("------服务器已开启--------");
        System.out.println("请输入服务器名字：");
        Scanner sc = new Scanner(System.in);
        serName = sc.next();
        while(true){
            socket = server.accept();
            hands(socket);
        }
    }


    private void hands(Socket socket) {
        String key = socket.getInetAddress().getHostAddress()+":"+socket.getPort();
        System.out.println("监听到的客户端："+key);
        Thread thread = new Thread(new ThreadSocket(socket));
        thread.setName(serName);
        thread.start();
    }

    public static void main(String[] args) {
        Server server = new Server();
    }

}