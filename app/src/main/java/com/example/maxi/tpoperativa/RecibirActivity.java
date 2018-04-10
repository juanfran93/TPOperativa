package com.example.maxi.tpoperativa;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.StringTokenizer;

import Funcionalidad.Persona;
import Funcionalidad.Peticion;

public class RecibirActivity extends AppCompatActivity {

    private LocalReciever_Recibir reciever = new LocalReciever_Recibir(this);

    public static final String GET_OWN_PETITIONS_OP = "getownpetitions";
    public static final String GET_OWN_PETITIONS_PATH = "getownpetitions";
    public static final String GET_USERINFO_OP = "getuserinfo";
    public static final String GET_USERINFO_PATH = "getuserinfo";
    public static final String CONFIRM_OP = "sendpackage";
    public static final String CONFIRM_PATH = "sendpackage";


    private HashMap<Integer, String> recursos;

    private HashMap<Integer,Integer> petitCant;

    private Spinner spinnerResource;
 
    private TextView recurso_tv;
    private TextView cantidad_tv;
    private TextView nombre_tv;
    private TextView direccion_tv;
    private TextView telefono_tv;
    private TextView website_tv;
    private TextView ciudad_tv;

    private Button escanearButton;
    private  Button confirmButton;

    private IntentIntegrator integrator;

    private Persona persona;

    private Persona donante = new Persona();
    private Integer cantidad;
    private Integer idPackageActual;

    private ImageView scannedImage;

    private HashMap<Integer, Peticion> peticiones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibir);

        scannedImage = (ImageView) findViewById(R.id.scannedImage);

        spinnerResource = (Spinner) findViewById(R.id.peticiones);

        escanearButton = (Button) findViewById(R.id.escanear_button2);
        confirmButton = (Button) findViewById(R.id.confirm_button3);
        confirmButton.setEnabled(false);

        recurso_tv = (TextView) findViewById(R.id.recursotextView11);
        cantidad_tv = (TextView) findViewById(R.id.cantidad_textView12);
        nombre_tv = (TextView) findViewById(R.id.nombre_textView6);
        direccion_tv = (TextView) findViewById(R.id.direccion_textView7);
        telefono_tv = (TextView) findViewById(R.id.telefono_textView8);
        website_tv = (TextView) findViewById(R.id.website_textView9);
        ciudad_tv = (TextView) findViewById(R.id.ciudad_textView10);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(PeticionActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        /* permiso para utilizacion de la camara para escanear */
        if(ContextCompat.checkSelfPermission(RecibirActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(RecibirActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }


        if (networkInfo != null && networkInfo.isConnected()) {

            this.persona = (Persona)getIntent().getExtras().getSerializable("Usuario");

            LocalBroadcastManager.getInstance(this).registerReceiver(reciever, new IntentFilter(ServiceCaller.RESPONSE_ACTION));
            final Intent mServiceIntent = new Intent(RecibirActivity.this, ServiceCaller.class);   //

            /* seteo del spinner de los recursos */

            mServiceIntent.putExtra(ServiceCaller.OPERACION, GET_OWN_PETITIONS_OP);
            mServiceIntent.putExtra(ServiceCaller.RUTA, GET_OWN_PETITIONS_PATH + "/"+persona.getId());
            startService(mServiceIntent);

            spinnerResource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    final Intent mServiceIntentSpinner = new Intent(RecibirActivity.this, ServiceCaller.class);
                    mServiceIntentSpinner.putExtra(ServiceCaller.OPERACION, GET_USERINFO_OP);
                    StringTokenizer tokenizer = new StringTokenizer((String)spinnerResource.getSelectedItem());
                    mServiceIntentSpinner.putExtra(ServiceCaller.RUTA, GET_USERINFO_PATH+"/"+(getPeticiones().get((Integer.valueOf(tokenizer.nextToken())))).getId_origen());
                    startService(mServiceIntentSpinner);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            /* seteo del boton de escaneo */

            this.integrator = new IntentIntegrator(this);
            this.integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt(" Scan a QR Code");

            integrator.setScanningRectangle(450, 450);

            // TODO escanear Buton
            escanearButton.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {

                    integrator.initiateScan();
                }
            });

            /* seteo de confirmacion del envio */

            confirmButton.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent mServiceIntentCONFIRM = new Intent(RecibirActivity.this, ServiceCaller.class);   //

                    mServiceIntentCONFIRM.putExtra(ServiceCaller.OPERACION, CONFIRM_OP);
                    //todo SACAR HARDCODE

                    Peticion peti= getPeticiones().get(1);
                    mServiceIntentCONFIRM.putExtra("id_package",(peti.getId_package()));
                    mServiceIntentCONFIRM.putExtra("id_resource",(peti.getId_recurso()));
                    mServiceIntentCONFIRM.putExtra("cantidad",(peti.getCantidad()));
                    mServiceIntentCONFIRM.putExtra("id_origen",(peti.getId_origen()));
                    mServiceIntentCONFIRM.putExtra("id_destino",persona.getId());
                    mServiceIntentCONFIRM.putExtra(ServiceCaller.RUTA, CONFIRM_PATH);
                    startService(mServiceIntentCONFIRM);
                }
            });

        } else {
            Toast.makeText(RecibirActivity.this, "Conexion no disponible", Toast.LENGTH_SHORT);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("ACTIVITIRESULT", "");
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult.getFormatName() != null) {
            String scanContent = scanningResult.getContents();
            Log.e("<--", scanContent);
            StringTokenizer tokenizer = new StringTokenizer((String)spinnerResource.getSelectedItem());
            if((!scanContent.equals(""))&&(Integer.valueOf(scanContent).equals(peticiones.get(Integer.valueOf(tokenizer.nextToken())).getId_package()))){
                scannedImage.setImageResource(R.drawable.green_check);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "El paquete asignado coincide", Toast.LENGTH_LONG);
                toast.show();
                escanearButton.setEnabled(false);
                confirmButton.setEnabled(true);
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "El paquete escaneado no es el asignado", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }


    public void setSpinnerResource(HashMap<Integer,String> recursos){
        this.recursos = recursos;
        Integer[] recursosIdList = recursos.keySet().toArray(new Integer[recursos.keySet().size()]);

        String[] recursoList = new String[recursos.keySet().size()];

        for (int i = 0 ; i<recursos.keySet().size() ; i++){
            recursoList[i] = recursosIdList[i].toString() + " - " + recursos.get(recursosIdList[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,recursoList);
        spinnerResource.setAdapter(adapter);
    }

    public void setTextViews(){
        nombre_tv.setText("Nombre: "+getDonante().getNombre());
        direccion_tv.setText("Direccion: "+getDonante().getDireccion());
        telefono_tv.setText("Telefono: "+getDonante().getTelefono());
        if(!getDonante().getWeb().equals("null")) website_tv.setText("Website: "+getDonante().getWeb()); else website_tv.setText("Website: ");
        ciudad_tv.setText("Ciudad: "+getDonante().getCiudad());
    }

    public Spinner getSpinnerResource() {
        return spinnerResource;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Persona getDonante() {
        return donante;
    }

    public void setDonante(Persona donante) {
        this.donante = donante;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }


    public void setPetitCant(HashMap<Integer, Integer> petitCant) {
        this.petitCant = petitCant;
    }


    public HashMap<Integer, Integer> getPetitCant() {
        return petitCant;
    }


    public HashMap<Integer, Peticion> getPeticiones() {
        return peticiones;
    }

    public void setPeticiones(HashMap<Integer, Peticion> peticiones) {
        this.peticiones = peticiones;
    }

    public void notifySuccess(String mensaje) {
        Toast toast = Toast.makeText(this,
                mensaje, Toast.LENGTH_SHORT);
        LinearLayout layout = (LinearLayout) toast.getView();
        if (layout.getChildCount() > 0) {
            TextView tv = (TextView) layout.getChildAt(0);
            tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }
        toast.show();
        finish();
    }
}
