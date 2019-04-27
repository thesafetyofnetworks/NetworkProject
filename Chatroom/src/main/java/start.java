import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class start {
    private JPanel Panel_Root;
    private JTextField TextField_Account;
    private JPasswordField TextField_Password;
    private JButton Button_Sign_In;
    private JButton Button_Register;
    private JLabel passwordLabel;
    private JLabel accountLabel;

    //显示注册界面
    private JFrame newRegister()
    {
        JFrame register = new JFrame("register");
        register.setContentPane(new register().Panel_Root);
        register.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        register.pack();
        //register.setBounds(300,200,400,100);
        register.setVisible(true);
        return register;
    }


    //显示聊天室界面
    private JFrame newChatroom()
    {
        JFrame chatroom = new JFrame("chatroom");
        chatroom.setContentPane(new chatroom().panel1);
        chatroom.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatroom.pack();
        //chatroom.setBounds(300,200,500,200);
        chatroom.setVisible(true);
        return chatroom;
    }

    //点击注册sign in之后的操作
    public start() {
        Button_Sign_In.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //认证AS证书

                //连接数据库，判断账号密码

                //显示聊天室界面
                JFrame chatroom = newChatroom();
            }
        });

        //点击Regiter按钮之后的操作
        Button_Register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //显示注册界面
                JFrame register = newRegister();
                String account = TextField_Account.getText();
                String passwd = TextField_Password.getText();
                //认证AS证书

                //传输注册信息

                //注册结束，返回登陆界面
            }
        });
    }

    public static void main(String[] args) {
        JFrame start = new JFrame("start");
        start.setContentPane(new start().Panel_Root);
        start.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        start.pack();
        start.setVisible(true);
    }


}
