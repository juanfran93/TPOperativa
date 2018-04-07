package com.example.maxi.tpoperativa;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import Funcionalidad.Persona;

public class DonationActivity extends AppCompatActivity {

    private AutoCompleteTextView recursosEditText;
    private HashMap<String, Integer> recursos;
    private LocalRecieverDonacion reciever = new LocalRecieverDonacion(this);
    private Persona persona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        persona = (Persona) getIntent().getSerializableExtra("Usuario");

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
                ActivityCompat.requestPermissions(DonationActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        2);
                if (ContextCompat.checkSelfPermission(DonationActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    mServiceIntent.putExtra(ServiceCaller.OPERACION, "addPackage");
                    mServiceIntent.putExtra(ServiceCaller.RUTA, "addpackage");
                    mServiceIntent.putExtra("id_user", persona.getId());
                    mServiceIntent.putExtra("resource", recursos.get(recursosEditText.getText().toString()).toString());
                    mServiceIntent.putExtra("cantidad", cantidadEditText.getText().toString());
                }
                //TODO AGREGEGAR EL USUARIO ACA Y EN EL SERVICE CALLER
                // mServiceIntent.putExtra("id_user",)

                startService(mServiceIntent);

            }
        });

    }

    public void setAutoTextResources(HashMap<String, Integer> recursos) {
        this.recursos = recursos;
        String[] recursosList = recursos.keySet().toArray(new String[recursos.keySet().size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, recursosList);
        recursosEditText.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void notifySuccess(String msj, Bitmap bitmap) {
        ImageView image = new ImageView(DonationActivity.this);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Registro de Donación");
        dialog.setCancelable(true);
        dialog.setMessage(msj);
        dialog.setView(image);

        addImageToGallery(bitmap);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });
        backToMenu(dialog);

    }

    public void addImageToGallery(Bitmap bitmap) {

        String ImagePath = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "demo_image",
                "demo_image"
        );
        Uri URI = Uri.parse(ImagePath);
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

