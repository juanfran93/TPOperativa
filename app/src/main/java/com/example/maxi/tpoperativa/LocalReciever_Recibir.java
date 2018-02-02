package com.example.maxi.tpoperativa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by lucho on 02/02/2018.
 */

public class LocalReciever_Recibir extends BroadcastReceiver {

    private  RecibirActivity activity;

    public LocalReciever_Recibir(RecibirActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String operation = intent.getStringExtra(ServiceCaller.OPERACION);

        switch (operation){
            case RecibirActivity.GET_OWN_PETITIONS_OP :
                HashMap<Integer,String> Recursos = new HashMap<>();
                try {
                    JSONObject json = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));
                    JSONArray jsonArray = new JSONArray(json.getString("peticiones"));

                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonRec = jsonArray.getJSONObject(i);
                        Recursos.put(jsonRec.getInt("id_peticion"),jsonRec.getString("recurso"));
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }

                activity.setSpinnerResource(Recursos);
            break;
        }


    }
}
