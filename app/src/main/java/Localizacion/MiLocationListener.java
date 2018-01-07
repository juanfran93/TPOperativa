package Localizacion;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import Funcionalidad.Coordenada;

/**
 * Created by Maxi on 28/4/2017.
 */

public class MiLocationListener implements LocationListener {

    private Location location;


    public Coordenada getCoordenadas(){
        return new Coordenada(this.location.getLatitude(),this.location.getLongitude());
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void onLocationChanged(Location loc)
    {
        loc.getLatitude();
        loc.getLongitude();
        this.location = loc;
        String coordenadas = "Mis coordenadas son: " + "Latitud = " + loc.getLatitude() + "Longitud = " + loc.getLongitude();
        Log.d("Coordenadas:",coordenadas);
        //Toast.makeText( getApplicationContext(),coordenadas,Toast.LENGTH_LONG).show();
    }
    public void onProviderDisabled(String provider)
    {
        Log.d("GPS","Gps Desactivado");
    }
    public void onProviderEnabled(String provider)
    {
        Log.d("GPS","Gps Activo");
    }
    public void onStatusChanged(String provider, int status, Bundle extras){}
}
