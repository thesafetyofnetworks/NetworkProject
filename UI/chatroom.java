package UI;
import Bean.Select;
import DES.DES_Method;
import RSA.Client;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class chatroom {
    public JPanel panel1;
    private JList list1;
    private JTextArea textArea3;
    private JButton exitButton;
    private JButton privateMessageButton;
    private JButton sendButton;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private  String receiver="ALL";
    private String send="";
    private JPanel listPanel;
    private JFrame chatroom;
    DefaultListModel<String> names;
    private Socket socket;
    ClientThread cliendThread;
    BufferedReader input;//input为服务器传来的数据
    PrintStream output;//output为向服务器输出的数据
    public chatroom() {
        chatroom = new JFrame("chatroom");
        chatroom.setContentPane(panel1);
        chatroom.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatroom.pack();
        names = new DefaultListModel<String>();
        //chatroom.setBounds(300,200,500,200);
        list1.setModel(names);
        ConnectServer();
        chatroom.setVisible(true);
        AddActionListener();
    }
    private void AddActionListener() {

        //4.2.3 点击发送
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = textArea1.getText().trim();
                try {
                    SendMessage("MESSAGE@" + receiver + "@" + send + "@" + message);
                    //   SendMessage("MESSAGE@"+ToTargetName+"@"+NickNameText.getText()+"@"+message);
                }catch (Exception L){

                }
            }
        });

        //4.2.4 检验目标发送者是谁
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = list1.getSelectedIndex();
                if(index<0) {
                    try {
                        Log("Client：检测到目标发送者下标为负数");
                    }catch (Exception L){

                    }
                    return;
                }
                if(index == 0) {
                    receiver = "ALL";
                }else {
                    String ToClientNickName = (String)names.getElementAt(index);
                    receiver = ToClientNickName;
                }
            }
        });
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = list1.getSelectedIndex();
                if(index<0) {
                    Log("Client：检测到目标发送者下标为负数");
                    return;
                }
                if(index == 0) {
                    receiver = "ALL";
                }else {
                    String ToClientNickName = (String)names.getElementAt(index);
                    receiver = ToClientNickName;
                }
            }
        });
    }
    private void loadMember(){
        ArrayList<String> m= Select.LoadMessage();
        names.addElement("ALL");
        for (String temp:m
        ) {
            names.addElement(temp);
        }
    }
    //建立连接
    public void ConnectServer() {

        //3.1.1 获取基本信息
        String ServerIPAddress = "192.168.43.77";
        int ServerPort = 8288;//Integer.parseInt(ServerPortText.getText().trim());
        String ID = "mzz";
        send=ID;
        try {
            //3.1.2 socket相关
            socket = new Socket(ServerIPAddress, ServerPort);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintStream(socket.getOutputStream());
            //3.1.2 在线列表添加所有人标签
            loadMember();
            //3.1.3 向服务器发送本帐号登陆消息
            SendMessage("LOGIN@"+ID);
            //3.1.4 为客户端建立线程
            cliendThread = new ClientThread();

        } catch (UnknownHostException e) {
            Log("Client：主机地址异常"+e.getMessage());
            return;
        } catch (IOException e) {
            Log("Client：连接服务器异常"+e.getMessage());
            return;
        }catch (Exception e){

        }
    }

    public class ClientThread implements Runnable {
        //与服务器建立连接时，新建客户端线程，否则无法接收信息
        //与服务器断开连接时，向服务器告知，杀掉客户端进程
        //客户端调用readline时会产生死锁，故需要新建一个线程
        boolean isRuning = true;

        //5.1 构造函数
        public ClientThread() {
            //5.1.1 开始本线程
            new Thread(this).start();
        }

        @Override
        //5.2 run函数会在线程开始时自动调用
        public void run() {
            while (isRuning) {//循环用于重复接收消息，客户端断开连接之前不停止
                // TODO Auto-generated method stub
                String message;
                try {
                    //5.2.1 在服务器传来的消息中读取下一行
                    //readline会产生死锁，如果没有下一条消息则继续等待
                    //正是因为死锁，才要新建一个客户端线程
                    message = input.readLine();
                    try {                               //信息解密过程 在这步前，可根据信息进行拆分
                        message = Message(message);
                    }catch (Exception L){
                        Log("解密错误");
                    }
                    Tokenizer tokens = new Tokenizer(message, "@");//对原有消息进行分割
                    String MessageType = tokens.nextToken();
                    try{
                        //5.2.2根据人为定义的传输协议对消息进行显示
                        switch (MessageType) {
                            case "LOGIN": {//其他用户上线
                                String LoginClientNickName = tokens.nextToken();
                                Log("上线通知：用户" + LoginClientNickName + "已上线");
                                names.addElement(LoginClientNickName);
                                break;
                            }

                            case "MESSAGE": {//聊天消息
                                String ToClientNickName = tokens.nextToken();
                                String FromClientNickName = tokens.nextToken();
                                String content = tokens.nextToken();
                                if ("ALL".equals(ToClientNickName)) {
                                    Log("来自" + FromClientNickName + "对全体的消息：" + content);
                                } else {
                                    Log("来自" + FromClientNickName + "对您的私聊消息：" + content);
                                }
                                break;
                            }

                            case "LOGOUT": {//其他用户下线的消息

                                break;
                            }
                            default: {
                                Log("客户端接收消息格式错误");
                                break;
                            }
                        }
                    }catch (Exception e){

                    }
                    try {
                        DataCry(message);
                    }catch (Exception e){

                    }
                    System.out.println("客户端接收到" + message);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log("Client：客户端接收消息失败" + e.getMessage());
                    break;
                }
            }
        }
    }
    public class Tokenizer{
        String Tokens[];
        int TokenIndex = 0;
        //6.2.1 构造方法，把Message，按Delimiter进行分割
        public Tokenizer(String Message, String Delimiter) {
            Tokens = Message.split(Delimiter);
        }
        //6.2.2 获取下一项
        public String nextToken() {
            TokenIndex++;
            return Tokens[TokenIndex-1];
        }
    }
    //发送消息
    public void SendMessage(String message) throws Exception {
        message=DataCry(message);
        output.println(message);
        output.flush();
    }
    private void Log(String message){
        //JLabel不支持\n换行，故添加html标签进行换行，没有</html>结束标签不影响显示
        textArea2.setText(textArea2.getText()+message+"\n");
    }
    private String DataCry(String message) throws Exception{
        String keys[]=DES_Method.Produce_keys("11111111111111111111111111111111111111111111111111111000");
        String C= DES_Method.DES_encry(message,keys);
        message=("发送消息"+message+"加密为："+C);
        textArea3.setText(textArea3.getText()+message+"\n");
        return C;
    }
    private String  Message(String message) throws Exception{           //接收消息进行解密
        String keys[]= DES_Method.Produce_keys("11111111111111111111111111111111111111111111111111111000");
        textArea3.setText(textArea3.getText()+"\n"+"收到加密信息为"+message);
        String dekeys[]=new String[16];
        for(int i=0;i<16;i++){
            dekeys[i]=keys[15-i];
        }
        message=DES_Method.DES_dencry(message,dekeys);
        textArea3.setText(textArea3.getText()+"\n"+"信息解密为"+message);
        textArea2.setText(textArea2.getText()+"\n"+message);
        return message;
    }
}
