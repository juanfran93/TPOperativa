package com.example.maxi.tpoperativa;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.HashMap;

public class PeticionActivity extends AppCompatActivity {

    private HashMap<String,Integer> recursosMap;

    static final String TAG = PeticionActivity.class.getCanonicalName();
    public static final String GETRESOURCES_OP = "getresources";
    public static final String GETRESOURCES_PATH = "getresources";

    private HashMap<String, Integer> recursos;
    private AutoCompleteTextView recursosEditText;

    private LocalReciverPeticion reciever = new LocalReciverPeticion(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peticion);

        recursosEditText = (AutoCompleteTextView) findViewById(R.id.resources_autocom);

        Intent intent = getIntent();

        LocalBroadcastManager.getInstance(this).registerReceiver(reciever, new IntentFilter(ServiceCaller.RESPONSE_ACTION));
        final Intent mServiceIntent = new Intent(PeticionActivity.this, ServiceCaller.class);   //

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(PeticionActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            //recursosEditText.
        }





    }

    public void setAutoTextResources(HashMap<String,Integer> recursos){
        this.recursos = recursos;
        String[] recursosList = recursos.keySet().toArray(new String[recursos.keySet().size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,recursosList);
        recursosEditText.setAdapter(adapter);
    }

    public HashMap<String, Integer> getRecursos() {
        return recursos;
    }

    public void setRecursos(HashMap<String, Integer> recursos) {
        this.recursos = recursos;
    }
}
