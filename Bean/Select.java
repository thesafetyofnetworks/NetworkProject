package Bean;
import java.sql.*;
import java.security.*;
import Bean.DBConn;
import java.sql.Connection;
import java.sql.SQLException;
public class Select {
public static void main(String []args){

    try{
        DBConn con = new DBConn();
        Connection conn = con.getConn();
        Statement st = conn.createStatement();
        ResultSet rs = null;
	 String sql = "SELECT ma FROM zz";
     rs = st.executeQuery(sql);
     //Security md=new Security();
     while(rs.next()){
         int ma  = rs.getInt("ma");
         System.out.print("ID: " + ma+"\n");
       /*   String pd = rs.getString("password");
          String  y=md.md5Password("woma");
         System.out.print( pd+"\n"  );
         System.out.print(y+"\n");
         if(pd.equals(y))
        	 System.out.print("ok"+"\n");
     */}
     rs.close();
     st.close();
     conn.close();
 }catch(SQLException se){

    se.printStackTrace();
}catch(Exception e){
    e.printStackTrace();
}
}
}
