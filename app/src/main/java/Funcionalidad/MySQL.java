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

public class MySQL{
    private String mysqldateBase;
    private String mysqluser;
    private String mysqlpass;
    private String mysqlIP;
    private String mysqlPort;
    private Connection conn;
    private Statement st;



    public MySQL(String db, String user, String pass) {
        this.mysqluser     = user;
        this.mysqlpass     = pass;
        this.mysqldateBase = db;
        this.mysqlIP       = "192.168.1.36";
        this.mysqlPort     = "3306";
    }

    public String getDateBase() {
        return mysqldateBase;
    }

    public void setDateBase(String dateBase) {
        this.mysqldateBase = dateBase;
    }

    public String getUser() {
        return mysqluser;
    }

    public void setUser(String user) {
        this.mysqluser = user;
    }

    public String getPass() {
        return mysqlpass;
    }

    public void setPass(String pass) {
        this.mysqlpass = pass;
    }

    public void CloseConecction(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Statement crearConn(){

        try {
            Class.forName("com.mysql.jdbc.Driver");
            // "jdbc:mysql://IP:PUERTO/DB", "USER", "PASSWORD");
            // Si estás utilizando el emulador de android y tenes el mysql en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2
            //TODO: Modificar la direccion de la base de datos
            //http://dbases.exa.unicen.edu.ar:8080/phpgadmin/
            //unc_248361
            //kobebryant
            conn = DriverManager.getConnection("jdbc:mysql://"+this.mysqlIP+":"+this.mysqlPort+ "/" +
                    this.mysqldateBase, this.mysqluser, this.mysqlpass);

            st = conn.createStatement();
            st.executeUpdate("USE trazaapp2");

            Log.v("MySql","Conexion con BD establecida.");
        } catch (SQLException se) {
            Log.v("MySql","oops! No se puede conectar. Error: " + se.toString());
        } catch (ClassNotFoundException e) {
            Log.v("MySql","oops! No se encuentra la clase. Error: " + e.getMessage());
        }

        return st;
    }

    public ResultSet getResultset(String s){
        try {
            crearConn();
            return st.executeQuery(s);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v("MySql","Error en executeQuery-MySql");
        }
        return null;
    }

    public int executeQuery(String s){
        try {
            crearConn();
            Log.d("RETORNO","CONSULTA = "+s);
            int rs = st.executeUpdate(s);
            Log.d("Retorno","Valor= "+rs);
            return rs;
        } catch (SQLException e) {
            Log.d("Retorno","Valor= NULO");
            e.printStackTrace();
            return -1;
        }
    }


}
