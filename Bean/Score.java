package Bean;

import java.lang.reflect.Array;
import java.sql.*;
import java.util.ArrayList;

public class Score {
    private String stu_id;
    private String test_id;
    private String test_name;
    private int grade;
    private String stu_class;
    private String stu_name;


    public boolean LoadScore(int test_name){
        try {
    DBConn con = new DBConn();
    Connection conn = con.getConn();
    Statement st = conn.createStatement();
    ResultSet rs = null;
    String sql = "UPDATE zz SET ma=? WHERE ma='"+test_name+"'";// AND Student_id='"+stu_id+"';
            PreparedStatement ps =conn.prepareStatement(sql);
           ps.setInt(1,77);
           ps.executeUpdate();
           ps.close();
           return true;
}       catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) {
        Score emp = new Score();
        boolean res =emp.LoadScore(333);;
        if (res == true) {
            System.out.println("hahhah");
        } else {
            System.out.println("uwuwuwu");
        }
    }
}
