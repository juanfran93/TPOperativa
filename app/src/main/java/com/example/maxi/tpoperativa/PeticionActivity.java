package com.example.maxi.tpoperativa;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import Funcionalidad.Persona;

public class PeticionActivity extends AppCompatActivity {

    private HashMap<String,Integer> recursosMap;

    static final String TAG = PeticionActivity.class.getCanonicalName();
    public static final String GETRESOURCES_OP = "getresources";
    public static final String GETRESOURCES_PATH = "getresources";

    public static final String SOLICITAR_OP = "addpeticion";
    public static final String SOLICITAR_PATH = "addpeticion";

    private HashMap<String, Integer> recursos;
    private AutoCompleteTextView  recursosEditText;
    private EditText cantidad;
    private Button solicitarButton;
    private LocalRecieverPeticion reciever = new LocalRecieverPeticion(this);

    private Persona persona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peticion);

        recursosEditText = (AutoCompleteTextView) findViewById(R.id.resources_autocom);
        solicitarButton = (Button) findViewById(R.id.solicitar_Button);
        cantidad = (EditText) findViewById(R.id.txt_Cantidad);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(PeticionActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            this.persona = (Persona)getIntent().getExtras().getSerializable("Usuario");
            Intent intent = getIntent();

            LocalBroadcastManager.getInstance(this).registerReceiver(reciever, new IntentFilter(ServiceCaller.RESPONSE_ACTION));
            final Intent mServiceIntent = new Intent(PeticionActivity.this, ServiceCaller.class);   //

            mServiceIntent.putExtra(ServiceCaller.OPERACION, "getresources");
            mServiceIntent.putExtra(ServiceCaller.RUTA, "getresources");
            startService(mServiceIntent);

        } else {
            Toast.makeText(PeticionActivity.this, "Conexion no disponible", Toast.LENGTH_SHORT);
        }

        solicitarButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validatedFields()) {
                    final Intent mServiceIntentSOLICITAR = new Intent(PeticionActivity.this, ServiceCaller.class);
                    mServiceIntentSOLICITAR.putExtra(ServiceCaller.OPERACION, "addpeticion");
                    mServiceIntentSOLICITAR.putExtra(ServiceCaller.RUTA, "addpeticion");

                    mServiceIntentSOLICITAR.putExtra("idUser", persona.getId());
                    mServiceIntentSOLICITAR.putExtra("idResource", (recursos.get(recursosEditText.getText().toString()).intValue()));
                    mServiceIntentSOLICITAR.putExtra("cantidad", Integer.valueOf(cantidad.getText().toString()));

                    startService(mServiceIntentSOLICITAR);
                }
                else
                    notifyError();
            }
        });
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

    private boolean validatedFields() {
        if(recursosEditText.getText().toString().equals(""))
            return false;
        else
        if( recursos.get(recursosEditText.getText().toString())== null  || cantidad.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    private void notifyError() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Registro de Petición");
        dialog.setCancelable(true);
        dialog.setMessage("Alguno de los campos requeridos no fue completado o es incorrecto ");
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }


    public void notifySuccess(String mensaje) {
        Toast toast = Toast.makeText(getApplicationContext(),
                mensaje, Toast.LENGTH_LONG);
        toast.show();
        finish();
    }
}
