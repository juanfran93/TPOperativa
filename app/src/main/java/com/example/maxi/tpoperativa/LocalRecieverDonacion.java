package com.example.maxi.tpoperativa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by Flia. Ferreira on 26/01/2018.
 */

class LocalRecieverDonacion extends BroadcastReceiver {

    private DonationActivity activityDonation;
    private static final String TAG = LocalBroadcastManager.class.getCanonicalName();

    public LocalRecieverDonacion(DonationActivity activityDonation) {
        this.activityDonation = activityDonation;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onReceive(Context context, Intent intent) {
        String operation = intent.getStringExtra(ServiceCaller.OPERACION);
        switch (operation) {
            case "getresources":

                try {

                    JSONObject json = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));

                    JSONArray jsonArray = new JSONArray(json.getString("resources"));

                    HashMap<String,Integer> recursos = new HashMap<>();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonRecurso = jsonArray.getJSONObject(i);

                        Integer id = jsonRecurso.getInt("id");
                        String recurso = jsonRecurso.getString("nombre");

                        recursos.put(recurso,id);
                    }
                    activityDonation.setAutoTextResources(recursos);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "addPackage":
                Log.e(TAG, intent.getStringExtra(ServiceCaller.RESPONSE));
                JSONObject json = null;
                try {
                    json = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));
                    activityDonation.notifySuccess(json.getString("mensaje"));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
        }
    }
}
