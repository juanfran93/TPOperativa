package com.example.maxi.tpoperativa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import Funcionalidad.Elemento;
import Funcionalidad.Persona;
import Funcionalidad.PointInfo;
import TareasAsincronas.ResultSetTask;
import TareasAsincronas.SpinnerTask;

public class ResourcesMaps extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private Elemento element;
    //Agregar actualizar en la tabla que persona tiene ese recurso
    private Spinner spinnerCities;
    private Spinner spinnerTrack;
    private String textSpinner;
    private String track;
    private Vector<PointInfo> pointInfo = new Vector<PointInfo>();
    private ResourcesMaps resources;
    private boolean selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.resources = this;
        this.selected = false;
        setContentView(R.layout.activity_prueba_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.element = (Elemento)getIntent().getExtras().getSerializable("Persona");

        this.spinnerCities = (Spinner) findViewById(R.id.spinner_resource);
        this.spinnerTrack  = (Spinner) findViewById(R.id.spinner_track);
        //Genero la query para cargar el spinner

        String query = "SELECT * " +
                "FROM resources " +
                "ORDER BY name";
        updateSpinner(this.spinnerCities,query,"name");

        this.textSpinner = "";
        this.spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,View selectedItemView, int position, long id) {
                selected = false;

                String text = (String) spinnerCities.getSelectedItem();
                String query = " SELECT track_code "+
                        " FROM RESOURCES AS R, RESOURCES_TRACK AS RT "+
                        " WHERE R.ID = RT.ID_RESOURCE" +
                        "   AND R.name = '"+text+"'" +
                        " ORDER BY RT.TRACK_CODE ";
                updateSpinner(spinnerTrack,query,"track_code");
                selected = true;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textSpinner = "";
            };
        });
        this.spinnerTrack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,View selectedItemView, int position, long id) {
                if(selected){
                    String text = (String) spinnerTrack.getSelectedItem();
                    track = text;
                    mMap.clear();
                    onMapReady(mMap);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                track = "";
            };
        });


    }


    //Spinner de los nombres de recursos
    private void updateSpinner(Spinner spinner, String query,String column){


        SpinnerTask citiesTask = new SpinnerTask(spinner,ResourcesMaps.this,column);
        citiesTask.execute(query);
        spinner = citiesTask.getSpinnerCities();

    }

    //Si el mapa esta listo agrego los puntos
    public void onMapReady(GoogleMap googleMap) {
        Log.d("MapReady", "Ingresando");
        mMap = googleMap;
        String query = "SELECT RR.ID, RR.ID_RESOURCE, RR.ID_ORIGEN, O.NAME as ORI_NAME, RR.ID_DESTINO, D.NAME AS DES_NAME, D.ROLE_ID AS DES_ROLE,\n" +
                "       D.ADDRESS AS DES_ADDRESS, RR.LATITUDE, RR.LONGITUDE, DATE_FORMAT(RR.DATE,'%d/%m/%Y') AS date\n" +
                " FROM   RESOURCE_ROUTE AS RR, USERS AS O, USERS AS D, RESOURCES AS R \n" +
                " WHERE R.ID = RR.ID_RESOURCE " +
                "  AND  RR.TRACK_CODE ='" + track + "'\n" +
                "  AND  RR.ID_DESTINO = D.ID\n" +
                "  AND  RR.ID_ORIGEN  = O.ID ";

        String mensaje = "No hay datos para mostrar";

        ResultSetTask task = new ResultSetTask(this, mensaje);
        int count = 1;
        try {
            task.execute(query);
            ResultSet result = task.get();
            while (result.next()) {
                PointInfo point = generarPoint(result);
                changeLocationIdem(point);
                pointInfo.add(point);
                addPointIntoMap(point, count);
                count++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Genero el punto con la informacion necesaria
    private PointInfo generarPoint(ResultSet result) throws SQLException {
        int id              = result.getInt("id");
        String id_resource  = result.getString("id_resource");
        String id_origen    = result.getString("id_origen");
        String origen_name  = result.getString("ORI_NAME");
        String id_destino   = result.getString("id_destino");
        String destino_name = result.getString("DES_NAME");
        int destino_role    = result.getInt("DES_ROLE");
        double latitude     = result.getDouble("latitude");
        double longitude    = result.getDouble("longitude");
        String date         = result.getString("date");
        String dest_address      = result.getString("DES_ADDRESS");
        return new PointInfo(id,id_resource,id_origen,origen_name,id_destino,destino_name,destino_role,dest_address,latitude,longitude,date);

    }

    //Cambio la ventana del punto con la info
    private void addMensaje() {
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            // Defines the contents of the InfoWindow
            public View getInfoContents(Marker marker) {
                String name = marker.getTitle();
                boolean exito = false;
                PointInfo point = pointInfo.elementAt(0);
                int count = 0;
                while (!exito){
                    if(String.valueOf(point.getId()).equals(name)){
                        exito = true;
                    }else{
                        count++;
                        point = pointInfo.elementAt(count);
                    }
                }
                View v = getLayoutInflater().inflate(R.layout.info_windows_marker, null);
                TextView movimiento_nro = (TextView) v.findViewById(R.id.movimiento_nro);
                TextView direccion_Destino = (TextView) v.findViewById(R.id.direccion_destino);
                TextView origen = (TextView) v.findViewById(R.id.origen);
                TextView destino = (TextView) v.findViewById(R.id.destino);
                TextView fecha = (TextView) v.findViewById(R.id.fecha);
                movimiento_nro.setText(   "Movimiento Nro: "+ point.getId());
                direccion_Destino.setText("Dir Destino:    "+ point.getDestino_address());
                origen.setText(           "Origen:           "+ point.getOrigen_name());
                destino.setText(          "Destino:         "+ point.getDestino_name());
                fecha.setText(            "Fecha:            "+ point.getDate());
                return v;
            }
        });

    }

    //Crea y Agrega punto al mapa
    private void addPointIntoMap(PointInfo pointer, int count){

        LatLng point = new LatLng(pointer.getLatitude(), pointer.getLongitude());
        Log.d("PUNTO","AGREGAR PUNTO= "+point.longitude + " , "+point.latitude);
        String textNumber = String.valueOf(count);
        int numberIcon = this.getLayoutNumber(pointer.getDestino_role());
        MarkerOptions tag = new MarkerOptions().position(point).title(String.valueOf(pointer.getId()))
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(textNumber,numberIcon)));
        addMensaje();
        mMap.addMarker(tag);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
    }

    //Si hay dos puntos que compiten por la misma posicion, muevo la latitud para
    //que no se solapen
    private void changeLocationIdem(PointInfo point) {
        double latitude_add = 00.00001;
        double aux=0;
        for(PointInfo p: pointInfo){
            if((p.getLatitude()==point.getLatitude())&& (p.getLongitude()==point.getLongitude())){
                if(point.getLatitude() > 0) {
                    point.setLatitude(point.getLatitude()+latitude_add);
                }else{
                    point.setLatitude(point.getLatitude()- latitude_add);
                }
            }

        }
    }

    //Selecciono el color del marcador a utilizar
    private int getLayoutNumber(int destino_role){
        if(destino_role != 1){
            return R.layout.icon_maps;
        }else{
            return R.layout.icon_maps_admin;
        }
    }

    //Genero el mapa de bits para dibujar el icono
    private Bitmap getMarkerBitmapFromView(String text, int numberIcon) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(numberIcon, null);
        TextView markerImageView = (TextView) customMarkerView.findViewById(R.id.profile_icon);
        markerImageView.setText(text);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

}

