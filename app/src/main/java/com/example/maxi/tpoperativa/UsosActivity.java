package com.example.maxi.tpoperativa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import Funcionalidad.Persona;

public class UsosActivity extends AppCompatActivity {

    private Persona persona;
    private String operacion;
    private String route;
    CheckBox enUsoBaja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usos);
        persona = (Persona) getIntent().getSerializableExtra("Usuario");
        enUsoBaja = (CheckBox)findViewById(R.id.enUso_check);
        enUsoBaja.setVisibility(View.INVISIBLE);
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        String textViewMsg = "";
        switch (view.getId()) {
            case R.id.donar_radio:
                if (checked)
                    operacion = "actualizar";
                route = "updateamountpackage";
                textViewMsg = "Cantidad a donar";
                enUsoBaja.setVisibility(View.INVISIBLE);
                break;
            case R.id.usar_radio:
                if (checked)
                    operacion = "actualizar";
                route = "updateamountpackage";
                textViewMsg = "Cantidad a usar";
                enUsoBaja.setVisibility(View.INVISIBLE);
                break;
            case R.id.eliminar_radio:
                if (checked)
                    operacion = "brokenobject";
                route = "brokenobject";
                textViewMsg = "Cantidad a dar de baja";
                enUsoBaja.setVisibility(View.VISIBLE);
                break;
        }
        TextView tv = (TextView) findViewById(R.id.cantidad_textview);
        tv.setText(textViewMsg);

    }
}
