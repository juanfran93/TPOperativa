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

public class LocalReciverPeticion extends BroadcastReceiver {

    private PeticionActivity peticionAct;

    public LocalReciverPeticion(PeticionActivity peticionActivity){
        this.peticionAct = peticionActivity;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String operation = intent.getStringExtra(ServiceCaller.OPERACION);
        switch (operation){
            case PeticionActivity.GETRESOURCES_OP :
                //--
                HashMap<String,Integer> Recursos = new HashMap<>();
                try {
                    JSONObject json = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));
                    JSONArray jsonArray = new JSONArray(json.getString("resources"));

                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonRec = jsonArray.getJSONObject(i);
                        Recursos.put(jsonRec.getString("nombre"),jsonRec.getInt("id"));
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }

                peticionAct.setAutoTextResources(Recursos);
                break;
                //--------------------------



        }

    }

}
