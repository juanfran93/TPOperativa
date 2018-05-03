package com.example.maxi.tpoperativa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Funcionalidad.Persona;
import Funcionalidad.PointInfo;

/**
 * Created by Flia. Ferreira on 07/02/2018.
 */

public class LocalRecieverMaps extends BroadcastReceiver{
    private ResourcesMaps mapsActivity;

    static final String TAG = LocalRecieverMaps.class.getCanonicalName();

    public LocalRecieverMaps(ResourcesMaps mapsActivity) {

        this.mapsActivity = mapsActivity;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        String operation = intent.getStringExtra(ServiceCaller.OPERACION);
        switch (operation) {
            case "getownresources":
                try {
                    JSONObject jsona = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));

                    HashMap<String,Integer> recursos = new HashMap<String,Integer>();

                    JSONArray json = new JSONArray(jsona.getString("resources"));

                    for(int i = 0; i < json.length(); i++) {
                        JSONObject jsonRec = json.getJSONObject(i);
                        recursos.put(jsonRec.getString("nombre"),jsonRec.getInt("id"));
                    }
                            mapsActivity.setRecursos(recursos);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "getownpackagebyresource":
                try {
                    JSONObject jsona = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));

                    ArrayList<Integer> packages = new ArrayList<>();

                    JSONArray json = new JSONArray(jsona.getString("packages"));

                    for(int i = 0; i < json.length(); i++) {
                        JSONObject jsonRec = json.getJSONObject(i);
                        packages.add(jsonRec.getInt("id"));
                    }
                    mapsActivity.setPackages(packages);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "getpackageroute" :
                try {
                    JSONObject jsona = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));

                    ArrayList<PointInfo> nodos = new ArrayList<>();

                    JSONArray json = new JSONArray(jsona.getString("nodos"));

                    for(int i = 0; i < json.length(); i++) {
                        JSONObject jsonRec = json.getJSONObject(i);

                        PointInfo nodo = new PointInfo(jsonRec.getInt("id"),jsonRec.getInt("id_origen"),jsonRec.getString("nombre_origen"),jsonRec.getInt("id_destino"),jsonRec.getString("nombre_destino"),1,jsonRec.getString("address"),jsonRec.getDouble("latitude"),jsonRec.getDouble("longitude"),jsonRec.getString("fecha"),jsonRec.getInt("id_package_padre"),jsonRec.getInt("id_package"));

                        nodos.add(nodo);
                    }
                    Log.d("LOCALRECIVER",nodos.toString());
                    mapsActivity.setMaps(nodos);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
