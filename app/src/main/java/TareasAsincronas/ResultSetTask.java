package TareasAsincronas;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.maxi.tpoperativa.LoginActivity;
import com.example.maxi.tpoperativa.ResourcesMaps;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Maxi on 30/4/2017.
 */

public class ResultSetTask  extends AsyncTask<String, Void, ResultSet> {

    private AppCompatActivity activity;
    private String mensaje;

    public ResultSetTask(AppCompatActivity activity, String mensaje){
        this.activity = activity;
        this.mensaje = mensaje;
    }
    public ResultSetTask(AppCompatActivity activity){
        this.activity = activity;
        this.mensaje = null;
    }

    @Override
    protected ResultSet doInBackground(String... params) {
        Log.d("Query",params[0]);
        return LoginActivity.Sql.getResultset(params[0]);
    }

    @Override
    protected void onPostExecute(ResultSet resultSet) {
        super.onPostExecute(resultSet);
        /*try {
            if(this.mensaje != null)
                if(resultSet.next()){

                    //Toast.makeText(activity,"Cargando puntos",Toast.LENGTH_SHORT).show();
                }else{
                   // Toast.makeText(activity,this.mensaje,Toast.LENGTH_SHORT).show();
                }
        } catch (SQLException e) {e.printStackTrace();}
        */
    }
}
