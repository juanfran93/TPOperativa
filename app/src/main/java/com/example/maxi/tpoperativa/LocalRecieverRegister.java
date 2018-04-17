package com.example.maxi.tpoperativa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by lucho on 31/01/2018.
 */

public class LocalRecieverRegister extends BroadcastReceiver {

    private RegisterActivity registerActivity;

    public LocalRecieverRegister(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onReceive(Context context, Intent intent) {
        String operation = intent.getStringExtra(ServiceCaller.OPERACION);
        switch (operation) {
            case "getinstituciones" :
                try {
                    JSONObject json = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));
                    JSONArray jsonArray = new JSONArray(json.getString("instituciones"));
                    HashMap<String, Integer> institutions = new HashMap<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonInstitution = jsonArray.getJSONObject(i);
                        Integer id = jsonInstitution.getInt("id");
                        String insti = jsonInstitution.getString("nombre");
                        institutions.put(insti, id);
                    }
                    registerActivity.setAutoTextInstitutions(institutions);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "registeruser" :
                try {
                    JSONObject jsona = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));
                    int status = jsona.getInt("status");
                    switch (status){
                        case 200 :
                            registerActivity.notifySuccess(jsona.getString("mensaje"));
                            break;
                        case 400 :
                            registerActivity.notifyUserExist(jsona.getString("mensaje"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }

    }
}
