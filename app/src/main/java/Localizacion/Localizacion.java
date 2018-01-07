package Localizacion;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.maxi.tpoperativa.LoginActivity;
import com.example.maxi.tpoperativa.RegisterActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import Funcionalidad.Coordenada;
import Funcionalidad.Persona;

/**
 * Created by Maxi on 28/4/2017.
 */

public class Localizacion implements LocationListener {
    RegisterActivity mainActivity;

    private Location location;

    public Localizacion(){

    }

    public Localizacion(Location location) {
        this.location = location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public Location getLocation() {
        return location;
    }

    public RegisterActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(RegisterActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onLocationChanged(Location loc) {
        // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
        // debido a la deteccion de un cambio de ubicacion

        loc.getLatitude();
        loc.getLongitude();

        String Text = "Mi ubicacion actual es: " + "\n Latitud = "
                + loc.getLatitude() + "\n Longitud = " + loc.getLongitude();
        Log.d("CAMBIO",Text);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Este metodo se ejecuta cuando el GPS es desactivado
        Log.d("","GPS Desactivado");
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Este metodo se ejecuta cuando el GPS es activado
        this.location.getLatitude();
        this.location.getLongitude();

        String Text = "Mi ubicacion actual es: " + "\n Lat = "
                + this.location.getLatitude() + "\n Long = " + this.location.getLongitude();
        Log.d("CAMBIO",Text);

        Log.d("","GPS Activado");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }


}



