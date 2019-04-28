package Server;

import java.io.IOException;
import java.net.Socket;

/**
 * 服务器处理Socket线程
 * @author Administrator
 *
 */
public class ThreadSocket implements Runnable{

    private Socket socket;

    public ThreadSocket(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            Thread threadReader = new Thread(new ThreadReader(socket.getInputStream()));
            Thread threadWriter = new Thread(new ThreadWriter(socket.getOutputStream()));
            threadReader.start();
            threadWriter.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}