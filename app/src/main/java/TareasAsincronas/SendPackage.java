package TareasAsincronas;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;

import com.example.maxi.tpoperativa.RegisterActivity;
import com.example.maxi.tpoperativa.SendActivity;

import Funcionalidad.Envio;
import Funcionalidad.Persona;
import Funcionalidad.Recurso;

/**
 * Created by Maxi on 2/5/2017.
 * Inserta la transaccion en la base, el evento a mostrar en el mapa
 */

public class SendPackage extends AsyncTask<Void, Void, Boolean> implements LocationListener {
    private Persona user;
    private Envio sender;
    private LocationManager lm;
    private AppCompatActivity activity;
    private String track;

    public SendPackage(Persona user,Editable track, int id_destino, Editable descripcion,
                       Recurso resource, AppCompatActivity activity) {
        this.activity = activity;
        this.user = user;
        this.track = String.valueOf(track);
        this.sender = new Envio(this.user,id_destino,String.valueOf(descripcion),resource);
        Log.d("CLASE SENDPACKAGE","= "+this.sender.toString());
    }


    @Override
    public void onLocationChanged(Location location) {
        lm.removeUpdates(this);
        double longitud = 0;
        double latitud = 0;

        if (location != null) {
            longitud = location.getLongitude();
            latitud = location.getLatitude();
            this.sender.setLatitude(latitud);
            this.sender.setLongitude(longitud);
        }
        Log.d("CLASE SENDPACKAGE, LOCATIONCHANGE",this.sender.toString());
        this.sender.getQuery().executeQuery(this.sender,activity);

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

    @Override
    protected Boolean doInBackground(Void... params) {
        return true;
    }

    protected void onPostExecute(final Boolean sucess) {
        super.onPostExecute(sucess);
        if (sucess) {
            try {
                if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    lm = (LocationManager) activity.getSystemService(SendActivity.LOCATION_SERVICE);

                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }


    }
}
