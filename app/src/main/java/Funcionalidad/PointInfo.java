package Funcionalidad;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Maxi on 1/5/2017.
 */

public class PointInfo {
    private int id; //Numero de movimiento
    private int id_resource; //Id del recurso
    private int id_origen; //Id del usuario de origen
    private String origen_name;
    private int id_destino;//Id persona destno
    private String destino_name;//Nombre persona destino
    private int destino_role;//Rol del destino, usuario comun o admin
    private String destino_address;//Direccion del destino
    private double latitude;
    private double longitude;
    private String date;


    public PointInfo(int id,int id_origen, String origen_name, int id_destino,
                     String destino_name, int destino_role, String dest_address, double latitude, double longitude, String date) {

        this.id              = id;
        this.id_resource     = 0;
        this.id_origen       = id_origen;
        this.origen_name     = origen_name;
        this.id_destino      = id_destino;
        this.destino_name    = destino_name;
        this.destino_role    = destino_role;
        this.destino_address = dest_address;
        this.latitude        = latitude;
        this.longitude       = longitude;
        this.date            = date;
    }

    //GETTERS AND SETTERS
    public String getDestino_address() {
        return destino_address;
    }

    public void setDestino_address(String destino_address) {
        this.destino_address = destino_address;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_resource() {
        return id_resource;
    }

    public void setId_resource(int id_resource) {
        this.id_resource = id_resource;
    }

    public int getId_origen() {
        return id_origen;
    }

    public void setId_origen(int id_origen) {
        this.id_origen = id_origen;
    }

    public String getOrigen_name() {
        return origen_name;
    }

    public void setOrigen_name(String origen_name) {
        this.origen_name = origen_name;
    }

    public int getId_destino() {
        return id_destino;
    }

    public void setId_destino(int id_destino) {
        this.id_destino = id_destino;
    }

    public String getDestino_name() {
        return destino_name;
    }

    public void setDestino_name(String destino_name) {
        this.destino_name = destino_name;
    }

    public int getDestino_role() {
        return destino_role;
    }

    public void setDestino_role(int destino_role) {
        this.destino_role = destino_role;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LatLng getLatLng(){
        LatLng latlng = new LatLng(this.getLatitude(),this.getLongitude());
        return latlng;
    }

}
