package com.example.maxi.tpoperativa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class DonationActivity extends AppCompatActivity {

    public static final String OPERACION = "OPERATION_SERVICE";
    private AutoCompleteTextView recursosEditText;
    private HashMap<String,Integer> recursos;
    private LocalRecieverDonacion reciever = new LocalRecieverDonacion(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        recursosEditText = (AutoCompleteTextView) findViewById(R.id.autoText_resource);
        LocalBroadcastManager.getInstance(this).registerReceiver(reciever, new IntentFilter(ServiceCaller.RESPONSE_ACTION));
        final Intent mServiceIntent = new Intent(DonationActivity.this, ServiceCaller.class);
        mServiceIntent.putExtra(OPERACION, "getresources");
        mServiceIntent.putExtra("ruta", "getresources");
        startService(mServiceIntent);

        final EditText cantidadEditText = (EditText) findViewById(R.id.editText_cantidad);

        Button btnConfirmar = (Button) findViewById(R.id.idbtn_confirmar);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mServiceIntent.replaceExtras(mServiceIntent.getExtras());
                mServiceIntent.putExtra(OPERACION, "addPackage");
                mServiceIntent.putExtra("ruta", "addPackage");
                mServiceIntent.putExtra("resource",recursos.get(recursosEditText.getText().toString()));
                mServiceIntent.putExtra("cantidad",cantidadEditText.getText());
                startService(mServiceIntent);

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

    public void notifySuccess(String msj){
        AlertDialog.Builder chequeo = new AlertDialog.Builder(this);
        chequeo.setTitle("Registro de Donación");
        chequeo.setCancelable(true);
        chequeo.setMessage(msj);
        chequeo.show();
        chequeo.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });
        backToMenu();

    }

    private void backToMenu() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Start the next activity
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 2500);
    }
}

