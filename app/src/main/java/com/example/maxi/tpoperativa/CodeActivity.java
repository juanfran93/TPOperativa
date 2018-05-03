package com.example.maxi.tpoperativa;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import Funcionalidad.Paquete;
import Funcionalidad.Persona;

public class CodeActivity extends AppCompatActivity {

    private LocalRecieverCode reciever;

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private Persona persona;
    private HashMap<Integer, Paquete> paquetes;
    private Spinner spinnerResources;
    private Spinner spinnerPackages;
    private HashMap<String, Integer> recursos;
    private Button generarButton;
    private HashMap<String, Boolean> fraccionarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        persona = (Persona) getIntent().getSerializableExtra("Usuario");


        this.spinnerResources = (Spinner) findViewById(R.id.resource_type_CODE);
        this.spinnerPackages = (Spinner) findViewById(R.id.package_id_CODE);

        this.generarButton = (Button) findViewById(R.id.generarButton_CODE);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(PeticionActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Intent intent = getIntent();

            reciever = new LocalRecieverCode(this);

            LocalBroadcastManager.getInstance(this).registerReceiver(reciever, new IntentFilter(ServiceCaller.RESPONSE_ACTION));
            final Intent mServiceIntent = new Intent(CodeActivity.this, ServiceCaller.class);   //

            mServiceIntent.putExtra(ServiceCaller.OPERACION, "getcurrentownresources");
            mServiceIntent.putExtra(ServiceCaller.RUTA, "getcurrentownresources" + "/" + persona.getId());
            startService(mServiceIntent);

            final TextView resourceTV = (TextView) findViewById(R.id.resourceType_tv_CODE);
            final TextView cantidadTV = (TextView) findViewById(R.id.cantidad_total_CODE);
            final TextView enUsoTV = (TextView) findViewById(R.id.cantidad_enUso_CODE);

            this.spinnerResources.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

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

            this.spinnerPackages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(!fraccionarios.get(spinnerResources.getSelectedItem().toString())){
                        resourceTV.setText("Recurso: " + spinnerResources.getSelectedItem().toString());
                        cantidadTV.setText("Cantidad: " + (int)paquetes.get((Integer.valueOf(spinnerPackages.getSelectedItem().toString()))).getCantidad());
                        enUsoTV.setText("Cantidad en uso: "+ (int)paquetes.get(Integer.valueOf(spinnerPackages.getSelectedItem().toString())).getEnUso());
                    }else{
                    resourceTV.setText("Recurso: " + spinnerResources.getSelectedItem().toString());
                    cantidadTV.setText("Cantidad: " + paquetes.get((Integer.valueOf(spinnerPackages.getSelectedItem().toString()))).getCantidad());
                    enUsoTV.setText("Cantidad en uso: "+ paquetes.get(Integer.valueOf(spinnerPackages.getSelectedItem().toString())).getEnUso());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            this.generarButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onClick(View view) {
                    try {
                        if (ContextCompat.checkSelfPermission(CodeActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CodeActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    2);
                        }
                        generarCodigoQR();
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    public void setRecursos(HashMap<String, Integer> recursos, HashMap<String, Boolean> fraccionarios) {
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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void notifySuccess(String msj, Bitmap bitmap) {
        ImageView image = new ImageView(CodeActivity.this);
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

                CodeActivity.this.finish();
            }
        });

        addImageToGallery(bitmap);

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }



    public void addImageToGallery(Bitmap bitmap) {

        String ImagePath = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "QR - " + spinnerResources.getSelectedItem().toString() + " - " + spinnerPackages.getSelectedItem().toString(),
                "QR code TrazaApp"
        );
        Uri URI = Uri.parse(ImagePath);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void generarCodigoQR() throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(spinnerPackages.getSelectedItem().toString(), BarcodeFormat.QR_CODE, 200, 200);
        Bitmap bitmap = createBitmap(bitMatrix);
        notifySuccess("Codigo QR recuperado exitosamente",bitmap);

    }

    private Bitmap createBitmap(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
