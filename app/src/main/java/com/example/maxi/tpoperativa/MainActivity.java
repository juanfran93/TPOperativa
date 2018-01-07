package com.example.maxi.tpoperativa;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import Funcionalidad.Persona;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Persona user;
    private TextView nameText;
    private TextView emailText;
    private TextView direccionText;
    private  NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);


        //Tomamos la persona en la actividad
        this.user = (Persona)getIntent().getExtras().getSerializable("Persona");

        hideItem();

        this.emailText = (TextView) header.findViewById(R.id.textMail);
        this.emailText.setText(this.user.getEmail());

        this.nameText = (TextView) header.findViewById(R.id.textNombre);
        this.nameText.setText(this.user.getNombre());

        this.direccionText = (TextView) header.findViewById(R.id.textDireccion);
        this.direccionText.setText(this.user.getDireccion());



    }
    /**
     * LINEAS DE GEO LOCALIZACION
     */

    /*
    * FIN GEO LOCALIZACION*/

    private void hideItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        if(!this.user.isAdmin()) {
            nav_Menu.findItem(R.id.nav_trazarRecurso).setVisible(false);
            nav_Menu.findItem(R.id.nav_nuevoRecurso).setVisible(false);
        }
        nav_Menu.findItem(R.id.nav_share).setVisible(false);
        nav_Menu.findItem(R.id.nav_nuevoRecurso).setVisible(false);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nuevoRecurso) {
            // Handle the camera action
        } else if (id == R.id.nav_trazarRecurso) {
            Intent sendActivity = new Intent(MainActivity.this, SendActivity.class);
            sendActivity.putExtra("Usuario",user);
            startActivity(sendActivity);
        } else if (id == R.id.nav_verRecurso) {
            //LLamado a elegir recurso primero y despues mostrar en mapa
            //Intent mainActivity = new Intent(MainActivity.this,MapsActivity.class);
            Intent mainActivity = new Intent(MainActivity.this,ResourcesMaps.class);
            mainActivity.putExtra("Persona",user);
            startActivity(mainActivity);

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
