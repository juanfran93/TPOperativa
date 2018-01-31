package com.example.maxi.tpoperativa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
        mServiceIntent.putExtra(ServiceCaller.OPERACION, "getresources");
        mServiceIntent.putExtra(ServiceCaller.RUTA, "getresources");
        startService(mServiceIntent);

        final EditText cantidadEditText = (EditText) findViewById(R.id.editText_cantidad);

        Button btnConfirmar = (Button) findViewById(R.id.button_confirmarDonation);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mServiceIntent.putExtra(ServiceCaller.OPERACION, "addPackage");
                mServiceIntent.putExtra(ServiceCaller.RUTA, "addpackage");
                mServiceIntent.putExtra("resource",recursos.get(recursosEditText.getText().toString()).toString());
                mServiceIntent.putExtra("cantidad",cantidadEditText.getText().toString());
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void notifySuccess(String msj){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Registro de Donación");
        dialog.setCancelable(true);
        dialog.setMessage(msj);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });
        backToMenu(dialog);

    }

    private void backToMenu(final AlertDialog.Builder dialog) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Start the next activity
                dialog.setCancelable(true);
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 2500);
    }
}

