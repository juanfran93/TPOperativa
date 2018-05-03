package com.example.maxi.tpoperativa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Funcionalidad.Recurso;

/**
 * Created by lucho on 26/01/2018.
 */

public class LocalRecieverPeticion extends BroadcastReceiver {

    private PeticionActivity peticionAct;

    public LocalRecieverPeticion(PeticionActivity peticionActivity){
        this.peticionAct = peticionActivity;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String operation = intent.getStringExtra(ServiceCaller.OPERACION);
        switch (operation){
            case PeticionActivity.GETRESOURCES_OP :
                //--
                HashMap<String,Integer> recursos = new HashMap<>();
                HashMap<String,Boolean> fraccionarios = new HashMap<>();
                try {
                    JSONObject json = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));
                    JSONArray jsonArray = new JSONArray(json.getString("resources"));

                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonRec = jsonArray.getJSONObject(i);
                        String nombre = jsonRec.getString("nombre");
                        recursos.put(nombre,jsonRec.getInt("id"));
                        fraccionarios.put(nombre,jsonRec.getBoolean("fraccionario"));
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }

                peticionAct.setAutoTextResources(recursos);
                peticionAct.setRecursos(recursos,fraccionarios);
                break;
            default :
                try {
                    JSONObject jsona = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));
                    String mensaje = jsona.getString("mensaje");
                    //peticionAct.notifySuccess(mensaje);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;


        }

    }

}
