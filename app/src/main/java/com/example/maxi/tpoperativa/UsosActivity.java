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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Socket;
import java.util.HashMap;
import java.util.Hashtable;

import Funcionalidad.Paquete;
import Funcionalidad.Persona;

public class UsosActivity extends AppCompatActivity {

    private Persona persona;
    private String operacion;
    private String route;
    CheckBox enUsoBaja;

    private LocalRecieverUsos reciever = new LocalRecieverUsos(this);
    private Button confirmButton;
    private Spinner spinnerResources;
    private Spinner spinnerPackages;
    private Spinner spinnerCantidad;
    private EditText cantidadFloat;
    private HashMap<String,Integer> recursos;
    private HashMap<String,Boolean> fraccionarios;
    private HashMap<Integer, Paquete> paquetes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usos);
        persona = (Persona) getIntent().getSerializableExtra("Usuario");


        this.spinnerResources = (Spinner) findViewById(R.id.resource_type);
        this.spinnerPackages = (Spinner) findViewById(R.id.package_id);
        this.spinnerCantidad = (Spinner) findViewById(R.id.cantidad_spinner);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(PeticionActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Intent intent = getIntent();

            LocalBroadcastManager.getInstance(this).registerReceiver(reciever, new IntentFilter(ServiceCaller.RESPONSE_ACTION));
            final Intent mServiceIntent = new Intent(UsosActivity.this, ServiceCaller.class);   //

            mServiceIntent.putExtra(ServiceCaller.OPERACION, "getcurrentownresources");
            mServiceIntent.putExtra(ServiceCaller.RUTA, "getcurrentownresources" + "/" + persona.getId());
            startService(mServiceIntent);

            final TextView resourceTV = (TextView) findViewById(R.id.resourceType_textView);
            final TextView cantidadTV = (TextView) findViewById(R.id.cantidad_total);
            final TextView enUsoTV = (TextView) findViewById(R.id.cantidad_enUso);

            cantidadFloat = (EditText) findViewById(R.id.cantidad_eText);
            cantidadFloat.setVisibility(View.GONE);


            this.spinnerResources.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if(fraccionarios.get(spinnerResources.getSelectedItem().toString())){
                        cantidadFloat.setVisibility(View.VISIBLE);
                        spinnerCantidad.setVisibility(View.GONE);
                    }
                    else{
                        cantidadFloat.setVisibility(View.GONE);
                        spinnerCantidad.setVisibility(View.VISIBLE);
                    }
                    String resource = (String) spinnerResources.getSelectedItem();
                    mServiceIntent.putExtra(ServiceCaller.OPERACION, "getcurrentownpackagebyresource");
                    mServiceIntent.putExtra(ServiceCaller.RUTA, "getcurrentownpackagebyresource");
                    mServiceIntent.putExtra("user", persona.getId());
                    mServiceIntent.putExtra("resource", recursos.get(resource));
                    startService(mServiceIntent);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.opcionesRadio);

            this.spinnerPackages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    Paquete paquete = paquetes.get(Integer.valueOf(spinnerPackages.getSelectedItem().toString()));
                    resourceTV.setText("Recurso: "+ spinnerResources.getSelectedItem().toString());
                    if(!fraccionarios.get(spinnerResources.getSelectedItem().toString())) {
                        Integer[] packageAmount = new Integer[(int) paquete.getEnUso()];
                        for (int i = 1; i <= paquete.getEnUso(); i++) {
                            packageAmount[i - 1] = i;
                        }
                        cantidadTV.setText("Cantidad total: "+ (int) paquete.getCantidad());
                        enUsoTV.setText("Cantidad en uso: "+ (int) paquete.getEnUso());
                        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(UsosActivity.this, android.R.layout.simple_list_item_1, packageAmount);
                        spinnerCantidad.setAdapter(adapter);
                    }
                    else {
                        cantidadTV.setText("Cantidad total: " + paquete.getCantidad());
                        enUsoTV.setText("Cantidad en uso: " + paquete.getEnUso());
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            confirmButton = (Button)findViewById(R.id.confirmUpdate_button);

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int opcion = radioGroup.getCheckedRadioButtonId();
                    Paquete paquete = paquetes.get(Integer.valueOf(spinnerPackages.getSelectedItem().toString()));
                    double enUsoNuevo = 0;
                    if(validatedFields()) {
                        switch (opcion) {
                            case R.id.donar_radio:
                                operacion = "actualizarpackage";
                                route = "updateamountpackage";
                                if (fraccionarios.get(spinnerResources.getSelectedItem().toString())) {
                                    if(Double.valueOf(cantidadFloat.getText().toString())>paquete.getEnUso()|| Double.valueOf(cantidadFloat.getText().toString())<=0){
                                        notifyError();
                                        return;
                                    }
                                    enUsoNuevo = paquetes.get(Integer.valueOf(spinnerPackages.getSelectedItem().toString())).getEnUso() - Double.valueOf(cantidadFloat.getText().toString());
                                } else {
                                    enUsoNuevo = paquetes.get(Integer.valueOf(spinnerPackages.getSelectedItem().toString())).getEnUso() - Double.valueOf(spinnerCantidad.getSelectedItem().toString());
                                }
                                mServiceIntent.putExtra(ServiceCaller.OPERACION, operacion);
                                mServiceIntent.putExtra(ServiceCaller.RUTA, route);
                                mServiceIntent.putExtra("id_package", Integer.valueOf(spinnerPackages.getSelectedItem().toString()));
                                mServiceIntent.putExtra("enUso", enUsoNuevo);
                                startService(mServiceIntent);
                                break;
                            case R.id.usar_radio:
                                operacion = "actualizarpackage";
                                route = "updateamountpackage";
                                if (fraccionarios.get(spinnerResources.getSelectedItem().toString())) {
                                    if(Double.valueOf(cantidadFloat.getText().toString())>paquete.getCantidad()-paquete.getEnUso()|| Double.valueOf(cantidadFloat.getText().toString())<=0){
                                        notifyError();
                                        return;
                                    }
                                    enUsoNuevo = paquetes.get(Integer.valueOf(spinnerPackages.getSelectedItem().toString())).getEnUso() + Double.valueOf(cantidadFloat.getText().toString());
                                } else {
                                    enUsoNuevo = paquetes.get(Integer.valueOf(spinnerPackages.getSelectedItem().toString())).getEnUso() + Double.valueOf(spinnerCantidad.getSelectedItem().toString());
                                }
                                mServiceIntent.putExtra(ServiceCaller.OPERACION, operacion);
                                mServiceIntent.putExtra(ServiceCaller.RUTA, route);
                                mServiceIntent.putExtra("id_package", Integer.valueOf(spinnerPackages.getSelectedItem().toString()));
                                mServiceIntent.putExtra("enUso", enUsoNuevo);
                                startService(mServiceIntent);
                                break;
                            case R.id.eliminar_radio:
                                operacion = "brokenobject";
                                route = "unsubscribeobject";
                                double cantidadNueva = 0;
                                if (fraccionarios.get(spinnerResources.getSelectedItem().toString())) {
                                    if(Double.valueOf(cantidadFloat.getText().toString())>paquete.getEnUso()|| Double.valueOf(cantidadFloat.getText().toString())<=0){
                                        notifyError();
                                        return;
                                    }
                                    cantidadNueva = paquete.getCantidad() - Double.valueOf(cantidadFloat.getText().toString());
                                    enUsoNuevo = paquete.getEnUso() - Double.valueOf(cantidadFloat.getText().toString());
                                } else {
                                    cantidadNueva = paquete.getCantidad() - Double.valueOf(spinnerCantidad.getSelectedItem().toString());
                                    enUsoNuevo = paquete.getEnUso() - Double.valueOf(spinnerCantidad.getSelectedItem().toString());
                                }
                                mServiceIntent.putExtra(ServiceCaller.OPERACION, operacion);
                                mServiceIntent.putExtra(ServiceCaller.RUTA, route);
                                mServiceIntent.putExtra("id_package", Integer.valueOf(spinnerPackages.getSelectedItem().toString()));
                                mServiceIntent.putExtra("cantidad", cantidadNueva);
                                mServiceIntent.putExtra("enUso", enUsoNuevo);
                                mServiceIntent.putExtra("resource", paquete.getIdResource());
                                mServiceIntent.putExtra("user", persona.getId());
                                startService(mServiceIntent);
                                break;
                        }
                    }else{
                        notifyError();
                    }

                }
            });

        }
    }

    private void notifyError() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Actualizacion de Paquetes");
            dialog.setCancelable(true);
            dialog.setMessage("Alguno de los campos requeridos no fue completado o es incorrecto ");
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
    }

    private boolean validatedFields() {
        if(cantidadFloat.getVisibility()!=View.GONE && cantidadFloat.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        spinnerCantidad.setAdapter(null);
        Paquete paquete = paquetes.get(Integer.valueOf(spinnerPackages.getSelectedItem().toString()));
        Integer[] packageAmount = new Integer[0];
        String textViewMsg = "";
        switch (view.getId()) {
            case R.id.donar_radio:
                if (checked) {

                    textViewMsg = "Cantidad a donar";
                    if(!fraccionarios.get(spinnerResources.getSelectedItem().toString())) {
                        packageAmount = new Integer[(int) paquete.getEnUso()];
                        for (int i = 1; i <= paquete.getEnUso(); i++) {
                            packageAmount[i - 1] = i;
                        }
                    }


                }
                break;
            case R.id.usar_radio:

                if (checked) {
                    textViewMsg = "Cantidad a usar";
                    if (!fraccionarios.get(spinnerResources.getSelectedItem().toString())) {
                        packageAmount = new Integer[(int) (paquete.getCantidad() - paquete.getEnUso())];
                        if (paquete.getCantidad() - paquete.getEnUso() != 0) {
                            for (int i = 1; i <= paquete.getCantidad() - paquete.getEnUso(); i++) {
                                packageAmount[i - 1] = i;
                            }
                        }
                    }
                }
                break;
            case R.id.eliminar_radio:
                if (checked) {
                    textViewMsg = "Cantidad a dar de baja";
                    if (!fraccionarios.get(spinnerResources.getSelectedItem().toString())) {
                        packageAmount = new Integer[(int) paquete.getEnUso()];
                        for (int i = 1; i <= paquete.getEnUso(); i++) {
                            packageAmount[i - 1] = i;
                        }
                    }
                }
                break;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(UsosActivity.this,android.R.layout.simple_list_item_1,packageAmount);
        spinnerCantidad.setAdapter(adapter);

        TextView tv = (TextView) findViewById(R.id.cantidad_textview);
        tv.setText(textViewMsg);

    }

    public void setRecursos(HashMap<String, Integer> recursos, HashMap<String,Boolean> fraccionarios) {
        this.recursos = recursos;
        this.fraccionarios = fraccionarios;
        String[] recursosList = recursos.keySet().toArray(new String[recursos.keySet().size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,recursosList);
        spinnerResources.setAdapter(adapter);
    }


    public void setPackages(HashMap<Integer, Paquete> packages) {
        this.paquetes = packages;
        Integer[] packagesList = packages.keySet().toArray(new Integer[packages.keySet().size()]);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>
                (this,android.R.layout.simple_list_item_1,packagesList);
        spinnerPackages.setAdapter(adapter);
    }


    public void notifySuccess(String mensaje) {
        Toast toast = Toast.makeText(getApplicationContext(),
                mensaje, Toast.LENGTH_LONG);
        toast.show();
        finish();
    }
}
