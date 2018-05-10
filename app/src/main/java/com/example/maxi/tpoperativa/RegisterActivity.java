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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Funcionalidad.Elemento;
import Funcionalidad.Persona;
import TareasAsincronas.AddElementTask;
import TareasAsincronas.SpinnerTask;

public class RegisterActivity extends AppCompatActivity{
    private TextView Nombre;
    private TextView Email;
    private TextView Usuario;
    private TextView Direccion;
    private TextView Telefono;
    private TextView Password;
    private TextView Website;
    private AutoCompleteTextView institucionesEditText;
    private Button btnFinalizar;
    private RegisterActivity actividad;

    private LocalRecieverRegister reciever = new LocalRecieverRegister(this);

    public static final String REGISTER_OP = "registeruser";
    public static final String REGISTER_PATH = "registeruser";

    private static final String TAG = RegisterActivity.class.getCanonicalName();

    private HashMap<String,Integer> instituciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Crear Cuenta");
        setContentView(R.layout.activity_register);
        this.actividad = this;

        this.institucionesEditText = (AutoCompleteTextView) findViewById(R.id.institucionesATV);

        LocalBroadcastManager.getInstance(this).registerReceiver(reciever, new IntentFilter(ServiceCaller.RESPONSE_ACTION));
        final Intent mServiceIntent = new Intent(RegisterActivity.this, ServiceCaller.class);
        mServiceIntent.putExtra(ServiceCaller.OPERACION, "getinstituciones");
        mServiceIntent.putExtra(ServiceCaller.RUTA, "getinstituciones");
        startService(mServiceIntent);

        this.Nombre = (TextView) findViewById(R.id.txt_Cantidad);
        this.Nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(Nombre.getText()).equals("Nombre y Apellido"))
                    Nombre.setText("");
            }
        });
        this.Email = (TextView) findViewById(R.id.txt_email);
        this.Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(Email.getText()).equals("Email"))
                    Email.setText("");
            }
        });
        this.Usuario = (TextView) findViewById(R.id.txt_username);
        this.Usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(Usuario.getText()).equals("Nombre de usuario"))

                  
                  
                  
              Usuario.setText("");
            }
        });
        this.Direccion = (TextView) findViewById(R.id.txt_direccion);
        this.Direccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(Direccion.getText()).equals("Direccion"))
                    Direccion.setText("");
            }
        });
        this.Telefono = (TextView) findViewById(R.id.txt_telefono);
        this.Telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(Telefono.getText()).equals("Telefono"))
                    Telefono.setText("");
            }
        });

        this.Password = (TextView) findViewById(R.id.txt_password);
        this.Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(Password.getText()).equals("Password"))
                    Password.setText("");
            }
        });

        this.Website = (TextView) findViewById(R.id.txt_Website);
        this.Website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(Website.getText()).equals("Website"))
                    Website.setText("");
            }
        });


        this.btnFinalizar = (Button) findViewById(R.id.btnFinalizar);
        this.btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    createUser();
            }
        });
    }
    ///////////////////////////////

    public void setAutoTextInstitutions(HashMap<String, Integer> intituciones) {
        this.instituciones = intituciones;
        String[] institusList = intituciones.keySet().toArray(new String[intituciones.keySet().size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, institusList);
        institucionesEditText.setAdapter(adapter);
    }
    ///////////////////////////
    private void createUser(){
        if(verifyDate()){
            //FIXME institucion
            Elemento element = new Persona(Usuario.getText(),Password.getText().toString(),
                    Nombre.getText(),Email.getText(),Direccion.getText(),Telefono.getText(),
                    Website.getText(),1);
            //AddElementTask add = new AddElementTask(element,this);
            //add.execute();
            //finish();


            Intent intentL = new Intent(RegisterActivity.this,ServiceCaller.class);
            intentL.putExtra(ServiceCaller.OPERACION, REGISTER_OP);
            intentL.putExtra(ServiceCaller.RUTA, REGISTER_PATH);
            intentL.putExtra("nombre",Nombre.getText().toString());
            intentL.putExtra("email",Email.getText().toString());
            intentL.putExtra("usuario",Usuario.getText().toString());
            intentL.putExtra("direccion",Direccion.getText().toString());
            intentL.putExtra("telefono",Telefono.getText().toString());
            intentL.putExtra("web",Website.getText().toString());
            intentL.putExtra("password",md5(Password.getText().toString()));
            intentL.putExtra("institucion",instituciones.get(institucionesEditText.getText().toString()));
            intentL.putExtra("longitud",2.00);
            intentL.putExtra("latitud",2.00);
            startService(intentL);

        }else {
            Toast text = Toast.makeText(actividad,"Faltan datos",Toast.LENGTH_SHORT);
            text.setGravity(Gravity.CENTER, 0, 0);
            text.show();
        }
    }


    private boolean verifyDate() {
        //TODO ARREGLAR
        if(String.valueOf(this.Nombre.getText()).length() != 0 && String.valueOf(this.Email.getText()).length() != 0
                && String.valueOf(this.Usuario.getText()).length() != 0 && String.valueOf(this.Direccion.getText()).length() != 0
                &&String.valueOf(this.Telefono.getText()).length() != 0 && String.valueOf(this.Password.getText()).length() != 0
                )//&& (itemSpinner != 0) )
            return true;
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void notifySuccess(String msj){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Registro de Usuario");
        dialog.setCancelable(true);
        dialog.setMessage(msj);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });
        backToLogin(dialog);

    }

    private void backToLogin(final AlertDialog.Builder dialog) {
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

    public void notifyUserExist(String mensaje) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Registro de Usuario");
        dialog.setCancelable(true);
        dialog.setMessage(mensaje);
        dialog.show();
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
