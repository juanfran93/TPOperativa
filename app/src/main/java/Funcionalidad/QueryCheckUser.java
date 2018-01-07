package Funcionalidad;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Maxi on 23/3/2017.
 */

public class QueryCheckUser extends AbsQuery {
    private String email;
    private String password;

    public QueryCheckUser(String email,String password){
        this.email = email;
        this.password = password;
    }
    @Override

    public void execute(String dateBase, String user, String pass){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/"+dateBase, user, pass);
            Statement st = conn.createStatement();

            String query = "SELECT * FROM usuario " +
                           "WHERE EMAIL='"+email+"'" +
                           "  AND PASS='"+password+"'";
            st = (Statement) conn.createStatement();
            ResultSet resultSet = ((java.sql.Statement) st).executeQuery(query);
            if(resultSet.next()){
               this.existe = true;
            }else{
                this.existe = false;
            }
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
