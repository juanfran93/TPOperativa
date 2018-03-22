package com.example.maxi.tpoperativa;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import Funcionalidad.Persona;
import Funcionalidad.Peticion;

public class RecibirActivity extends AppCompatActivity {

    private LocalReciever_Recibir reciever = new LocalReciever_Recibir(this);

    public static final String GET_OWN_PETITIONS_OP = "getownpetitions";
    public static final String GET_OWN_PETITIONS_PATH = "getownpetitions";
    public static final String GET_USERINFO_OP = "getuserinfo";
    public static final String GET_USERINFO_PATH = "getuserinfo";
    public static final String CONFIRM_OP = "sendpackage";
    public static final String CONFIRM_PATH = "sendpackage";


    private HashMap<Integer, String> recursos;

    private HashMap<Integer,Integer> petitCant;

    private Spinner spinnerResource;

    private TextView recurso_tv;
    private TextView cantidad_tv;
    private TextView nombre_tv;
    private TextView direccion_tv;
    private TextView telefono_tv;
    private TextView website_tv;
    private TextView ciudad_tv;

    private Button escanearButton;
    private  Button confirmButton;

    private Persona persona;

    private Persona donante = new Persona();
    private Integer cantidad;

    private HashMap<Integer, Peticion> peticiones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibir);

        spinnerResource = (Spinner) findViewById(R.id.peticiones);

        escanearButton = (Button) findViewById(R.id.escanear_button2);
        confirmButton = (Button) findViewById(R.id.confirm_button3);

        recurso_tv = (TextView) findViewById(R.id.recursotextView11);
        cantidad_tv = (TextView) findViewById(R.id.cantidad_textView12);
        nombre_tv = (TextView) findViewById(R.id.nombre_textView6);
        direccion_tv = (TextView) findViewById(R.id.direccion_textView7);
        telefono_tv = (TextView) findViewById(R.id.telefono_textView8);
        website_tv = (TextView) findViewById(R.id.website_textView9);
        ciudad_tv = (TextView) findViewById(R.id.ciudad_textView10);

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

            spinnerResource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    final Intent mServiceIntentSpinner = new Intent(RecibirActivity.this, ServiceCaller.class);
                    mServiceIntentSpinner.putExtra(ServiceCaller.OPERACION, GET_USERINFO_OP);
                    mServiceIntentSpinner.putExtra(ServiceCaller.RUTA, GET_USERINFO_PATH+"/"+(getPeticiones().get(1)).getId_origen());
                    startService(mServiceIntentSpinner);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            escanearButton.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            confirmButton.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent mServiceIntentCONFIRM = new Intent(RecibirActivity.this, ServiceCaller.class);   //

                    mServiceIntentCONFIRM.putExtra(ServiceCaller.OPERACION, CONFIRM_OP);
                    //todo SACAR HARDCODE
                    mServiceIntentCONFIRM.putExtra("id_package",(getPeticiones().get(1)).getId_package());
                    mServiceIntentCONFIRM.putExtra("id_resource",(getPeticiones().get(1)).getId_recurso());
                    mServiceIntentCONFIRM.putExtra("cantidad",(getPeticiones().get(1)).getCantidad());
                    mServiceIntentCONFIRM.putExtra("id_origen",(getPeticiones().get(1)).getId_origen());
                    mServiceIntentCONFIRM.putExtra("id_destino",persona.getId());
                    mServiceIntentCONFIRM.putExtra(ServiceCaller.RUTA, CONFIRM_PATH);
                    startService(mServiceIntentCONFIRM);
                }
            });

        } else {
            Toast.makeText(RecibirActivity.this, "Conexion no disponible", Toast.LENGTH_SHORT);
        }

    }

    public void setSpinnerResource(HashMap<Integer,String> recursos){
        this.recursos = recursos;
        Integer[] recursosIdList = recursos.keySet().toArray(new Integer[recursos.keySet().size()]);

        //String[] recursoNameList = recursos.values()
        String[] recursoList = new String[recursos.keySet().size()];

        for (int i = 0 ; i<recursos.keySet().size() ; i++){
            recursoList[i] = recursosIdList[i].toString() + " - " + recursos.get(recursosIdList[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,recursoList);
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

    public void setTextViews(){
       // nombre_tv.append(" "+persona.getNombre());

        nombre_tv.setText("Nombre: "+getDonante().getNombre());
        direccion_tv.setText("Direccion: "+getDonante().getDireccion());
        telefono_tv.setText("Telefono: "+getDonante().getTelefono());
        website_tv.setText("Website: "+getDonante().getWeb());
        ciudad_tv.setText("Ciudad: "+getDonante().getCiudad());


    }

    public Persona getDonante() {
        return donante;
    }

    public void setDonante(Persona donante) {
        this.donante = donante;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }


    public void setPetitCant(HashMap<Integer, Integer> petitCant) {
        this.petitCant = petitCant;
    }


    public HashMap<Integer, Integer> getPetitCant() {
        return petitCant;
    }


    public HashMap<Integer, Peticion> getPeticiones() {
        return peticiones;
    }

    public void setPeticiones(HashMap<Integer, Peticion> peticiones) {
        this.peticiones = peticiones;
    }
}
