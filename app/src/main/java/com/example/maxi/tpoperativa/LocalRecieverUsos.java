package com.example.maxi.tpoperativa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import Funcionalidad.Paquete;
import Funcionalidad.PointInfo;

/**
 * Created by Flia. Ferreira on 07/04/2018.
 */

public class LocalRecieverUsos extends BroadcastReceiver {

    private UsosActivity activity;

    public LocalRecieverUsos(UsosActivity activity){
        this.activity = activity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String operation = intent.getStringExtra(ServiceCaller.OPERACION);
        switch (operation) {
            case "getcurrentownresources":
                try {
                    JSONObject jsona = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));

                    HashMap<String,Integer> recursos = new HashMap<String,Integer>();
                    HashMap<String, Boolean> fraccionarios = new HashMap<String,Boolean>();

                    JSONArray json = new JSONArray(jsona.getString("resources"));

                    for(int i = 0; i < json.length(); i++) {
                        JSONObject jsonRec = json.getJSONObject(i);
                        String nombre = jsonRec.getString("nombre");
                        recursos.put(nombre,jsonRec.getInt("id"));
                        fraccionarios.put(nombre,jsonRec.getBoolean("fraccionario"));
                    }
                    activity.setRecursos(recursos,fraccionarios);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "getcurrentownpackagebyresource":
                try {
                    JSONObject jsona = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));

                    HashMap<Integer, Paquete> packages = new HashMap<Integer,Paquete>();

                    JSONArray json = new JSONArray(jsona.getString("packages"));

                    for(int i = 0; i < json.length(); i++) {
                        JSONObject jsonRec = json.getJSONObject(i);
                        Integer id = jsonRec.getInt("id");
                        Paquete p = new Paquete();
                        p.setId(id);
                        p.setCantidad(jsonRec.getDouble("cantidad"));
                        p.setEnUso(jsonRec.getDouble("enUso"));
                        p.setIdResource(jsonRec.getInt("idResource"));
                        packages.put(id,p);

                    }
                    activity.setPackages(packages);

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
                        // fixme esta bien escrito id_paquete y padre?
                        PointInfo nodo = new PointInfo(jsonRec.getInt("id"),jsonRec.getInt("id_origen"),jsonRec.getString("nombre_origen"),jsonRec.getInt("id_destino"),jsonRec.getString("nombre_destino"),1,jsonRec.getString("address"),jsonRec.getDouble("latitude"),jsonRec.getDouble("longitude"),jsonRec.getString("fecha"),jsonRec.getInt("id_paquete"),jsonRec.getInt("padre"));
                        nodos.add(nodo);
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "actualizarpackage" :
                try {
                    JSONObject jsona = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));
                    String mensaje = jsona.getString("mensaje");
                    activity.notifySuccess(mensaje);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case "brokenobject" :
                try {
                    JSONObject jsona = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));
                    String mensaje = jsona.getString("mensaje");
                    activity.notifySuccess(mensaje);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }
}
