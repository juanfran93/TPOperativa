package com.example.maxi.tpoperativa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Funcionalidad.Persona;
import Funcionalidad.Peticion;

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
                HashMap<Integer,Integer> petitCant = new HashMap<>();
                HashMap<Integer, Peticion> peticiones = new HashMap<>();
                try {
                    JSONObject json = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));
                    JSONArray jsonArray = new JSONArray(json.getString("peticiones"));

                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonRec = jsonArray.getJSONObject(i);
                        Recursos.put(jsonRec.getInt("id_peticion"),jsonRec.getString("recurso"));
                        petitCant.put(jsonRec.getInt("id_peticion"),jsonRec.getInt("cantidad"));

                        Peticion peti = new Peticion();
                        peti.setId_peticion(jsonRec.getInt("id_peticion"));
                        peti.setCantidad(jsonRec.getInt("cantidad"));
                        peti.setId_origen(jsonRec.getInt("id_origen"));
                        peti.setId_package(jsonRec.getInt("id_package"));
                        peti.setId_recurso(jsonRec.getInt("id_recurso"));
                        peti.setRecurso(jsonRec.getString("recurso"));

                        peticiones.put(jsonRec.getInt("id_peticion"),peti);
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }

                activity.setSpinnerResource(Recursos);
                activity.setPetitCant(petitCant);
                activity.setPeticiones(peticiones);

            break;
            case RecibirActivity.GET_USERINFO_OP :
                try{
                    JSONObject json = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));
                    JSONObject jsonP = new JSONObject(json.getString("usuario"));

                    Persona usuario = new Persona();
                    usuario.setId(jsonP.getInt("id"));
                    usuario.setEmail(jsonP.getString("email"));
                    usuario.setWeb(jsonP.getString("web"));
                    usuario.setNombre(jsonP.getString("name"));
                    usuario.setTelefono(jsonP.getString("phone"));
                    usuario.setDireccion(jsonP.getString("direccion"));

                    activity.setDonante(usuario);
                    activity.setTextViews();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "sendpackage":
                int status = 0;
                String mensaje = new String();
                try{
                    JSONObject json = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));
                    status = json.getInt("status");
                    mensaje = json.getString("mensaje");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(status == 200)
                activity.notifySuccess(mensaje);
                break;
        }


    }
}
