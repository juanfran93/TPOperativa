package TareasAsincronas;

/**
 * Created by Maxi on 2/5/2017.
 */

import android.icu.util.RangeValueIterator;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.maxi.tpoperativa.LoginActivity;

import Funcionalidad.Elemento;
import Funcionalidad.Persona;
public class TaskInsert extends AsyncTask<String, Void, Integer> {

    private Elemento element;
    private AppCompatActivity activity;

    public TaskInsert(Elemento user, AppCompatActivity activity){
        this.element = user;
        this.activity = activity;
    }


    protected Integer doInBackground(String... params) {
        Log.d("TASKINSERT",this.element.toString());
        Log.d("CONSULTA TASKINSERT",params[0]);

        return LoginActivity.Sql.executeQuery(params[0]);
    }
    protected void onPostExecute(Integer insert) {
        super.onPostExecute(insert);
        if (insert != -1) {
            Toast toast = Toast.makeText(activity.getApplicationContext(),
                    "Registrado Correctamente.", Toast.LENGTH_SHORT);
            toast.show();
        }
        this.activity.finish();
    }
}
