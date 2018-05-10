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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import Funcionalidad.Persona;

public class DonationActivity extends AppCompatActivity {

    private AutoCompleteTextView recursosEditText;
    private HashMap<String, Integer> recursos;
    private LocalRecieverDonacion reciever = new LocalRecieverDonacion(this);
    private Persona persona;
    private EditText cantidadEditText;
    private HashMap<String, Boolean> fraccionarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        persona = (Persona) getIntent().getSerializableExtra("Usuario");

        recursosEditText = (AutoCompleteTextView) findViewById(R.id.autoText_resource);


        if (ContextCompat.checkSelfPermission(DonationActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DonationActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(reciever, new IntentFilter(ServiceCaller.RESPONSE_ACTION));
        final Intent mServiceIntent = new Intent(DonationActivity.this, ServiceCaller.class);
        mServiceIntent.putExtra(ServiceCaller.OPERACION, "getresources");
        mServiceIntent.putExtra(ServiceCaller.RUTA, "getresources");

        startService(mServiceIntent);
        recursosEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(fraccionarios.get(adapterView.getItemAtPosition(i).toString())){
                    cantidadEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); //permite cantidades con decimales
                }
                else{
                    cantidadEditText.setInputType(InputType.TYPE_CLASS_NUMBER); // permite solo cantidades enteras
                }

            }
        });
        recursosEditText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(fraccionarios.get(recursosEditText.getText().toString())){
                    cantidadEditText.setInputType(8192); //permite cantidades con decimales
                }
                else{
                    cantidadEditText.setInputType(2); // permite solo cantidades enteras
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cantidadEditText = (EditText) findViewById(R.id.editText_cantidad);

        Button btnConfirmar = (Button) findViewById(R.id.button_confirmarDonation);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validatedFields()) {
                    mServiceIntent.putExtra(ServiceCaller.OPERACION, "addPackage");
                    mServiceIntent.putExtra(ServiceCaller.RUTA, "addpackage");
                    mServiceIntent.putExtra("id_user", persona.getId());
                    mServiceIntent.putExtra("resource", recursos.get(recursosEditText.getText().toString()));
                    mServiceIntent.putExtra("cantidad", Double.valueOf(cantidadEditText.getText().toString()));
                    Log.d("<<.......",cantidadEditText.getText().toString());

                    startService(mServiceIntent);
                }
                else {
                    notifyError();
                }
            }
        });

    }

    private void notifyError() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Registro de Donación");
        dialog.setCancelable(true);
        dialog.setMessage("Alguno de los campos requeridos no fue completado o es incorrecto ");
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }

    private boolean validatedFields() {
        if(recursosEditText.getText().toString().equals(""))
            return false;
        else
            if( recursos.get(recursosEditText.getText().toString())== null  || cantidadEditText.getText().toString().equals("")){
                return false;
        }
        return true;
    }

    public void setAutoTextResources(HashMap<String, Integer> recursos, HashMap<String, Boolean> fraccionarios) {
        this.recursos = recursos;
        this.fraccionarios = fraccionarios;
        String[] recursosList = recursos.keySet().toArray(new String[recursos.keySet().size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, recursosList);
        recursosEditText.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void notifySuccess(String msj, Bitmap bitmap) {
        ImageView image = new ImageView(DonationActivity.this);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        image.setImageBitmap(bitmap);
        dialog.setTitle("Registro de Donación");
        dialog.setCancelable(true);
        dialog.setMessage(msj);
        dialog.setView(image);

        final AlertDialog.Builder builder = dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "QR code guardado en la galeria de imagenes", Toast.LENGTH_LONG);
                toast.show();

                DonationActivity.this.finish();
            }
        });

        addImageToGallery(bitmap);

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public void addImageToGallery(Bitmap bitmap)  {

        String ImagePath = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "QR - " + recursosEditText.getText().toString(),
                "QR code TrazaApp"
        );
        Uri URI = Uri.parse(ImagePath);
    }

}

