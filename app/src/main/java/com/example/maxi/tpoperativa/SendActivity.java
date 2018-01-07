package com.example.maxi.tpoperativa;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import Funcionalidad.Elemento;
import Funcionalidad.Persona;
import Funcionalidad.Recurso;
import TareasAsincronas.ResultSetTask;
import TareasAsincronas.SendPackage;
import TareasAsincronas.SpinnerTask;

public class SendActivity extends AppCompatActivity {
    private Persona user;
    private Recurso resource;
    private Spinner spinner_destino;
    private Spinner spinner_recurso;
    private Button btn_confirmar;
    private EditText track;
    private EditText descripcion;
    private String spinnerItem_destino;
    private int id_destino;
    private String spinnerItem_recurso;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        this.user = (Persona)getIntent().getExtras().getSerializable("Usuario");

        this.spinner_destino = (Spinner) findViewById(R.id.spinner_destino);
        this.spinner_recurso = (Spinner) findViewById(R.id.spinner_recurso);
        this.track           = (EditText) findViewById(R.id.edit_track);
        this.descripcion     = (EditText) findViewById(R.id.edit_descripcion);
        //agregar descripcion

        //Update Spinner Usuarios
        String query_user = "SELECT NAME " +
                            "FROM   USERS " +
                            "WHERE  id <> "+this.user.getId()+
                            " ORDER BY NAME ASC ";

        updateSpinner(this.spinner_destino,query_user,"NAME");
        setSpinners();

        String query_recurso = "SELECT NAME " +
                               "FROM RESOURCES " +
                               "ORDER BY NAME ASC";
        updateSpinner(this.spinner_recurso,query_recurso,"name");

        inicComponentes();

    }
    private void inicComponentes(){
        this.track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(track.getText()).equals("Track Numero"))
                    track.setText("");
            }
        });
        this.descripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(descripcion.getText()).equals("Descripcion"))
                    descripcion.setText("");
            }
        });
        this.btn_confirmar = (Button) findViewById(R.id.idbtn_confirmar);

        this.btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btn_confirmar.getWindowToken(), 0);
                verificarDatos();
            }
        });
    }
    private void setSpinners(){
        this.spinner_destino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,View selectedItemView, int position, long id) {
                spinnerItem_destino = (String) spinner_destino.getSelectedItem();
                String query = " SELECT ID " +
                        " FROM USERS " +
                        " WHERE NAME = '"+spinnerItem_destino+"'";
                ResultSetTask task = new ResultSetTask(SendActivity.this,"");
                task.execute(query);
                try {

                    ResultSet result = task.get();
                    if(result.next()){
                        id_destino = result.getInt("id");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerItem_destino = "";
            };
        });
        this.spinner_recurso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,View selectedItemView, int position, long id) {
                spinnerItem_recurso = (String) spinner_recurso.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerItem_recurso = "";
            };
        });
    }
    private void verificarDatos() {

        if(existeTrack()){
            if(persona_recurso()){
                Log.d("ANTES DE SENDPACKAGE",this.user.toString());
               // SendPackage send = new SendPackage(this.user,spinnerItem_recurso,spinnerItem_destino,this.track.getText(),this.id_destino,this.descripcion.getText(),this.resource,this);
                SendPackage send = new SendPackage(this.user,this.track.getText(),this.id_destino,this.descripcion.getText(),this.resource,this);

                send.execute();

            }else{
                Toast.makeText(this,"No posee dicho recurso",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"El track es invalido",Toast.LENGTH_LONG).show();
        }
    }

    private boolean persona_recurso() {
        String query = "SELECT * " +
                       "FROM   USERS_RESOURCES AS US, RESOURCES AS R " +
                       "WHERE  US.RESOURCE_ID = R.ID " +
                       "  AND  US.USER_ID = "+this.user.getId()+
                       "  AND  R.NAME = '"+this.spinnerItem_recurso+"'";
        Log.d("PERSONA RECURSO",query);
        ResultSetTask task = new ResultSetTask(this);
        task.execute(query);
        try {
            ResultSet result = task.get();
            if(result.next()){
                Log.d("PERSONA RECURSO",query);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Toast.makeText(SendActivity.this,"Problemas de conexion",Toast.LENGTH_SHORT);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean existeTrack() {
        String query = "SELECT R.ID AS ID, RT.TRACK_CODE AS TRACK_CODE, R.NAME AS NAME "+
                       "FROM   RESOURCES AS R, RESOURCES_TRACK AS RT " +
                       "WHERE  RT.ID_RESOURCE = R.ID " +
                       "  AND  R.NAME = '"+spinnerItem_recurso+"'";
        ResultSetTask task = new ResultSetTask(this);
        task.execute(query);
        try {
            ResultSet result = task.get();
            if(result.next()){
                this.resource = new Recurso(result.getInt("ID"),result.getString("TRACK_CODE"),result.getString("NAME"));
                Log.d("RECURSO CREADO",this.resource.toString());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Toast.makeText(SendActivity.this,"Problemas de conexion",Toast.LENGTH_SHORT);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;

    }

    //Spinner de los nombres de recursos
    private void updateSpinner(Spinner spinner, String query,String column){
        SpinnerTask citiesTask = new SpinnerTask(spinner,SendActivity.this,column);
        citiesTask.execute(query);
        spinner = citiesTask.getSpinnerCities();
    }
}
