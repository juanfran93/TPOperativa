package com.example.maxi.tpoperativa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

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
        try {
            JSONObject jsona = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));
            int status = jsona.getInt("status");
            switch (status){
                case 200 :
                    registerActivity.notifySuccess(jsona.getString("mensaje"));
                    break;
                default :
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
