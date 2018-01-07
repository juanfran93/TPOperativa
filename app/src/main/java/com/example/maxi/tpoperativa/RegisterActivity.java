package com.example.maxi.tpoperativa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
    private Spinner spinnerCities;
    private Button btnFinalizar;
    private int itemSpinner;
    private RegisterActivity actividad;

    private List listCities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Crear Cuenta");
        setContentView(R.layout.activity_register);
        this.actividad = this;

        this.itemSpinner = 0;

        this.Nombre = (TextView) findViewById(R.id.txt_nombre);
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

        this.spinnerCities = (Spinner) findViewById(R.id.spinner_resource);
        updateSpinner();

        this.spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,View selectedItemView, int position, long id) {
                itemSpinner = spinnerCities.getSelectedItemPosition() + 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                itemSpinner = 0;
            };
        });

        this.btnFinalizar = (Button) findViewById(R.id.btnFinalizar);
        this.btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    createUser();
            }
        });



    }
    private void createUser(){
        if(verifyDate()){
            Elemento element = new Persona(Usuario.getText(),Password.getText().toString(),
                    Nombre.getText(),Email.getText(),Direccion.getText(),Telefono.getText(),
                    Website.getText(),itemSpinner);
            AddElementTask add = new AddElementTask(element,this);
            add.execute();
            finish();

        }else {
            Toast text = Toast.makeText(actividad,"Faltan datos",Toast.LENGTH_SHORT);
            text.setGravity(Gravity.CENTER, 0, 0);
            text.show();
        }
    }
    private void updateSpinner(){
        Log.d("CIUDAD:","HAGO CLICK");
        SpinnerTask citiesTask = new SpinnerTask(this.spinnerCities,RegisterActivity.this,"name");

        String query = "SELECT * " +
                "FROM locations";

        citiesTask.execute(query);
        spinnerCities = citiesTask.getSpinnerCities();

    }
    private boolean verifyDate() {
        if(String.valueOf(this.Nombre.getText()).length() != 0 && String.valueOf(this.Email.getText()).length() != 0
                && String.valueOf(this.Usuario.getText()).length() != 0 && String.valueOf(this.Direccion.getText()).length() != 0
                &&String.valueOf(this.Telefono.getText()).length() != 0 && String.valueOf(this.Password.getText()).length() != 0
                && (itemSpinner != 0) )
            return true;
        return false;
    }





}
