package com.example.maxi.tpoperativa;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

import Funcionalidad.Persona;

public class RecibirActivity extends AppCompatActivity {

    private LocalReciever_Recibir reciever = new LocalReciever_Recibir(this);

    public static final String GET_OWN_PETITIONS_OP = "getownpetitions";
    public static final String GET_OWN_PETITIONS_PATH = "getownpetitions";

    private HashMap<Integer, String> recursos;
    private Spinner spinnerResource;

    private Persona persona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibir);

        spinnerResource = (Spinner) findViewById(R.id.peticiones);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(PeticionActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            this.persona = (Persona)getIntent().getExtras().getSerializable("Usuario");
            Intent intent = getIntent();

            LocalBroadcastManager.getInstance(this).registerReceiver(reciever, new IntentFilter(ServiceCaller.RESPONSE_ACTION));
            final Intent mServiceIntent = new Intent(RecibirActivity.this, ServiceCaller.class);   //

            mServiceIntent.putExtra(ServiceCaller.OPERACION, GET_OWN_PETITIONS_OP);
            mServiceIntent.putExtra(ServiceCaller.RUTA, GET_OWN_PETITIONS_PATH + "/"+persona.getId());
            startService(mServiceIntent);

        } else {
            Toast.makeText(RecibirActivity.this, "Conexion no disponible", Toast.LENGTH_SHORT);
        }

    }

    public void setSpinnerResource(HashMap<Integer,String> recursos){
        this.recursos = recursos;
        Integer[] recursosList = recursos.keySet().toArray(new Integer[recursos.keySet().size()]);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>
                (this,android.R.layout.simple_list_item_1,recursosList);
        spinnerResource.setAdapter(adapter);
    }

    public Spinner getSpinnerResource() {
        return spinnerResource;
    }


    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

}
