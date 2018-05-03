package com.example.maxi.tpoperativa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Funcionalidad.Paquete;

/**
 * Created by Flia. Ferreira on 02/05/2018.
 */

public class LocalRecieverCode extends BroadcastReceiver {

    private CodeActivity activity;

    public LocalRecieverCode(CodeActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String operation = intent.getStringExtra(ServiceCaller.OPERACION);
        switch (operation) {
            case "getcurrentownresources":
                try {
                    JSONObject jsona = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));

                    HashMap<String, Integer> recursos = new HashMap<String, Integer>();
                    HashMap<String, Boolean> fraccionarios = new HashMap<String, Boolean>();

                    JSONArray json = new JSONArray(jsona.getString("resources"));

                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jsonRec = json.getJSONObject(i);
                        String nombre = jsonRec.getString("nombre");
                        recursos.put(nombre, jsonRec.getInt("id"));
                        fraccionarios.put(nombre, jsonRec.getBoolean("fraccionario"));
                    }
                    activity.setRecursos(recursos,fraccionarios);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "getcurrentownpackagebyresource":
                try {
                    JSONObject jsona = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));

                    HashMap<Integer, Paquete> packages = new HashMap<Integer, Paquete>();

                    JSONArray json = new JSONArray(jsona.getString("packages"));

                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jsonRec = json.getJSONObject(i);
                        Integer id = jsonRec.getInt("id");
                        Paquete p = new Paquete();
                        p.setId(id);
                        p.setCantidad(jsonRec.getDouble("cantidad"));
                        p.setEnUso(jsonRec.getDouble("enUso"));
                        p.setIdResource(jsonRec.getInt("idResource"));
                        packages.put(id, p);

                    }
                    activity.setPackages(packages);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;


        }
    }
}
