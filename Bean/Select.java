package Bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Select {
    public static ArrayList<String> LoadMessage(){
        ArrayList<String> m=new ArrayList<>();
        try{
            DBConn con = new DBConn();
            Connection conn = con.getConn();
            Statement st = conn.createStatement();
            ResultSet rs = null;
            String sql = "SELECT user_id  FROM online WHERE sign='"+1+"'";
            rs = st.executeQuery(sql);
            //Security md=new Security();
            while(rs.next()){

                String pd = rs.getString("user_id");
                m.add(pd);
        /*  String  y=md.md5Password("woma");
         System.out.print( pd+"\n"  );
         System.out.print(y+"\n");
         if(pd.equals(y))
        	 System.out.print("ok"+"\n");
     */
            }
            rs.close();
            st.close();
            conn.close();
        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        return m;
    }
}
