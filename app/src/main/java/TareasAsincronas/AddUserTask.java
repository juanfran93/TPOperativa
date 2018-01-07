package TareasAsincronas;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.maxi.tpoperativa.LoginActivity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Maxi on 4/4/2017.
 */

public class AddUserTask extends AsyncTask<Void, Void, Boolean> {
    private String Usuario;
    private String Password;
    private String Nombre;
    private String Email;
    private String Direccion;
    private String Telefono;
    private String Website;
    private int itemSpinner;
    private double latitud;
    private double longitud;
    private Boolean result;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }


    public AddUserTask(CharSequence Usuario, String Password, CharSequence Nombre, CharSequence Email,
                       CharSequence Direccion, CharSequence Telefono, CharSequence Website,
                       int item, double lat, double lon){
        this.Usuario     = String.valueOf(Usuario);
        this.Password    = Password;
        this.Nombre      = String.valueOf(Nombre);
        this.Email       = String.valueOf(Email);
        this.Direccion   =String.valueOf(Direccion);
        this.Telefono    = String.valueOf(Telefono);
        this.Website     = String.valueOf(Website);
        this.itemSpinner = item;
        this.latitud     = lat;
        this.longitud    = lon;
    };
    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String query = "SELECT * FROM USERS " +
                    "WHERE email='"+Email+"'";
            ResultSet resultSet = LoginActivity.Sql.getResultset(query);

            if(resultSet.next()) {
                return false;
            }
            query = "INSERT INTO USERS(id,role_id,username,password,name,email,"+
                    "address,phone,location_id,website,latitude, longitude)" +
                    "          VALUES(`id`,2,'"+this.Usuario+"','"+this.Password+"'," +
                    "'"+this.Nombre+"','"+this.Email+"'," +
                    "'"+this.Direccion+"','"+this.Telefono+"'," +
                    "'"+String.valueOf(itemSpinner)+"','"+this.Website+"','"+
                    "'"+String.valueOf(latitud)+"','"+"'"+String.valueOf(longitud)+"')";

            Log.d("Consulta",query);
            LoginActivity.Sql.executeQuery(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }
    protected void onPostExecute(final Boolean sucess) {
        this.result = sucess;
        Log.d("Resultado dentro",String.valueOf(result));
        if (sucess) {
            try {
                AddUserTask.this.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            //GENERAR LAS COORDENADAS
        } else {


        }


    }
}