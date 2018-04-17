package com.example.maxi.tpoperativa;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.maps.model.SquareCap;


import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import Funcionalidad.Persona;
import Funcionalidad.PointInfo;
import Funcionalidad.PointInfoList;
import TareasAsincronas.ResultSetTask;

public class ResourcesMaps extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private Persona persona;
    //Agregar actualizar en la tabla que persona tiene ese recurso
    private Spinner spinnerResources;
    private Spinner spinnerPackages;
    private String textSpinner;
    private String track;
    private ArrayList<PointInfo> pointInfo = new ArrayList<PointInfo>();
    private boolean selected;
    private HashMap<String,Integer> recursos;

    private LocalRecieverMaps reciever = new LocalRecieverMaps(this);

    private HashMap<Integer,Integer[]> padrehijos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.selected = false;
        setContentView(R.layout.activity_prueba_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.persona = (Persona) getIntent().getExtras().getSerializable("Usuario");

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(PeticionActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        this.spinnerResources = (Spinner) findViewById(R.id.spinner_resource);
        this.spinnerPackages = (Spinner) findViewById(R.id.spinner_track);

        if (networkInfo != null && networkInfo.isConnected()) {
            Intent intent = getIntent();

            LocalBroadcastManager.getInstance(this).registerReceiver(reciever, new IntentFilter(ServiceCaller.RESPONSE_ACTION));
            final Intent mServiceIntent = new Intent(ResourcesMaps.this, ServiceCaller.class);   //

            mServiceIntent.putExtra(ServiceCaller.OPERACION, "getownresources");
            mServiceIntent.putExtra(ServiceCaller.RUTA, "getownresources" + "/" + persona.getId());
            startService(mServiceIntent);

            this.spinnerResources.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    selected = false;

                    String resource = (String) spinnerResources.getSelectedItem();
                    mServiceIntent.putExtra(ServiceCaller.OPERACION, "getownpackagebyresource");
                    mServiceIntent.putExtra(ServiceCaller.RUTA, "getownpackagebyresource");
                    mServiceIntent.putExtra("user", persona.getId());
                    mServiceIntent.putExtra("resource",recursos.get(resource));
                    startService(mServiceIntent);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    textSpinner = "";
                }
            });

            this.spinnerPackages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        Integer pack = (Integer) spinnerPackages.getSelectedItem();
                        mServiceIntent.putExtra(ServiceCaller.OPERACION, "getpackageroute");
                        mServiceIntent.putExtra(ServiceCaller.RUTA, "getpackageroute/" + pack);
                        startService(mServiceIntent);
                        onMapReady(mMap);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    track = "";
                }

                ;
            });

        }

    }

    //Si el mapa esta listo agrego los puntos
    public void onMapReady(GoogleMap googleMap) {
        Log.d("MapReady", "Ingresando");
        mMap = googleMap;
        mMap.clear();

        int count = 1;
        ArrayList<ArrayList<LatLng>> listLatLng = new ArrayList<ArrayList<LatLng>>();  //

        PointInfoList pil = new PointInfoList(pointInfo);
        for (ArrayList<PointInfo> al : pil.ordenarNodos()) {
            ArrayList<LatLng> lll = new ArrayList<LatLng>();
            for(PointInfo p : al) {
                changeLocationIdem(p);
                addPointIntoMap(p, count);
                lll.add(p.getLatLng());
                count++;
            }
            listLatLng.add(lll);
        }

        for (ArrayList<LatLng> al : listLatLng ) {
            Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .addAll(al));
            polyline1.setColor(Color.BLUE);
            polyline1.setGeodesic(true);
            polyline1.setStartCap(new RoundCap());
            polyline1.setEndCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_flecha), 10));
            polyline1.setJointType(1);
            polyline1.setTag("A");

        }
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
                PointInfo point = pointInfo.get(0);
                int count = 0;
                while (!exito){
                    if(String.valueOf(point.getId()).equals(name)){
                        exito = true;
                    }else{
                        count++;
                        point = pointInfo.get(count);
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
        double latitude_add = 00.00025;
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

    public void setRecursos(HashMap<String, Integer> recursos) {
        this.recursos = recursos;
        String[] recursosList = recursos.keySet().toArray(new String[recursos.keySet().size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,recursosList);
        spinnerResources.setAdapter(adapter);
    }

    public void setPackages(ArrayList<Integer> packages) {
        Integer[] packagesList = packages.toArray(new Integer[packages.size()]);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>
                (this,android.R.layout.simple_list_item_1,packagesList);
        spinnerPackages.setAdapter(adapter);
    }

    public void setMaps(ArrayList<PointInfo> nodos) {
        pointInfo = nodos;
        onMapReady(mMap);
    }

    private void dibujarPuntos(ArrayList<PointInfo> nodos){
        Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                .clickable(false)
                .add(
                        new LatLng(-35.016, 143.321),
                        new LatLng(-34.747, 145.592),
                        new LatLng(-34.364, 147.891),
                        new LatLng(-33.501, 150.217),
                        new LatLng(-32.306, 149.248),
                        new LatLng(-32.491, 147.309)));
    }
}

