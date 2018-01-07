package TareasAsincronas;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.maxi.tpoperativa.LoginActivity;
import com.example.maxi.tpoperativa.RegisterActivity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxi on 3/4/2017.
 */

public class SpinnerTask extends AsyncTask<String, Void, ResultSet> {
    private ResultSet result;
    private List list = new ArrayList<>();
    private Spinner spinnerCities;
    private AppCompatActivity register;
    private String search_column;
    private  Toast text;

    public SpinnerTask(Spinner spinner, AppCompatActivity activity, String column){
        this.register = activity;
        this.spinnerCities = spinner;
        this.search_column = column;
    }

    @Override
    protected ResultSet doInBackground(String... params) {
        Log.d("PARAMETRO",params[0]);

        this.result = LoginActivity.Sql.getResultset(params[0]);
        return result;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("cargando","Cargando");
        this.text = Toast.makeText(register,"Cargando",Toast.LENGTH_SHORT);
        this.text.setGravity(Gravity.CENTER, 0, 0);
        this.text.show();
    }

    protected void onPostExecute(final ResultSet cities) {
        try {
            int index = 0;
            Log.d("COLUMNA",this.search_column);
            while (cities.next()){
                list.add(cities.getString(this.search_column));
                Log.d("Prueba",list.get(index).toString());
                index+=1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayAdapter spinner_adapter;
        spinner_adapter = new ArrayAdapter(this.register, android.R.layout.simple_spinner_item, list);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCities.setAdapter(spinner_adapter);
    }

    public Spinner getSpinnerCities() {
        return spinnerCities;
    }

}