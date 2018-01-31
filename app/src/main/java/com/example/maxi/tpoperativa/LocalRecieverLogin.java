package com.example.maxi.tpoperativa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Funcionalidad.Persona;

/**
 * Created by lucho on 30/01/2018.
 */

public class LocalRecieverLogin extends BroadcastReceiver {

    private LoginActivity loginActivity;

    public LocalRecieverLogin(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        String operation = intent.getStringExtra(ServiceCaller.SERVICE_TYPE);
        switch (operation) {
            case LoginActivity.ATTEMPTLOGIN_OP:
                //--
                Persona persona = new Persona();
                try {
                    JSONObject jsona = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));
                    String status = intent.getStringExtra("status");
                    Log.d(ServiceCaller.class.getCanonicalName(),status);

                    switch (status){
                        case "200" :
                            JSONObject json = new JSONObject(jsona.getString("usuario"));
                            persona.setAdmin(json.getBoolean("admin"));
                            persona.setCiudad(json.getInt("location_id"));
                            persona.setDireccion(json.getString("direccion"));
                            persona.setDomicilio(json.getString("address"));
                            persona.setEmail(json.getString("email"));
                            persona.setId(json.getInt("id"));
                            persona.setLatitude(json.getDouble("latitude"));
                            persona.setLongitude(json.getDouble("longitude"));
                            persona.setNombre(json.getString("name"));
                            persona.setPass(json.getString("password"));
                            persona.setWeb(json.getString("web"));
                            persona.setUser(json.getString("username"));
                            persona.setTelefono(json.getString("phone"));
                            break;
                        default :
                            Log.d(ServiceCaller.class.getCanonicalName(), status);
                            break;
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
