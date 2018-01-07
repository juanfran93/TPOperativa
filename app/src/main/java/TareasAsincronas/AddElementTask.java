package TareasAsincronas;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.maxi.tpoperativa.LoginActivity;
import com.example.maxi.tpoperativa.RegisterActivity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;

import Funcionalidad.Elemento;
import Funcionalidad.Persona;

/**
 * Created by Maxi on 2/5/2017.
 */

public class AddElementTask extends AsyncTask<Void, Void, Boolean> implements LocationListener {
    private Boolean result;
    //nuevo
   // private Persona user;
    private Elemento element;
    private LocationManager lm;
    private AppCompatActivity activity;

    public AddElementTask(Elemento element, AppCompatActivity activity) {
        this.element = element;
        this.activity = activity;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }



    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String query = "SELECT * FROM USERS " +
                           "WHERE email='"+this.element.getIdentifier()+"'";
            ResultSet resultSet = LoginActivity.Sql.getResultset(query);
            if(resultSet.next()) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }
    protected void onPostExecute(final Boolean sucess) {
        super.onPostExecute(sucess);
        Log.d("Resultado dentro",String.valueOf(result));
        if (sucess) {
            try {
                if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    lm = (LocationManager) activity.getSystemService(RegisterActivity.LOCATION_SERVICE);

                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            //GENERAR LAS COORDENADAS
        } else {
            //Toast del email ya existe

        }


    }

    @Override
    public void onLocationChanged(Location location) {
        lm.removeUpdates(this);

        double longitud = 0;
        double latitud = 0;

        if (location != null) {
            longitud = location.getLongitude();
            latitud = location.getLatitude();
            this.element.setLatitude(latitud);
            this.element.setLongitude(longitud);
        }
        Log.d("Persona",this.element.toString());
        this.element.getQuery().executeQuery(this.element,activity);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
