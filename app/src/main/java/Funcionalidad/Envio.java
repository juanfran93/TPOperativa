package Funcionalidad;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import Query.AbsQuery;
import Query.QueryEnvio;
import Query.QueryPersona;

/**
 * Created by Maxi on 2/5/2017.
 * Clase para almacenar el objeto a enviar en el evento
 */

public class Envio extends Elemento{
    private String date;
    private Persona user;
    private Recurso resource;
    private int id_Destino;
    private String descripcion;

    public Envio(Persona user, int id_Destino, String descripcion, Recurso resource){
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = df.format(date);

        this.user = user;
        this.resource = resource;
        this.descripcion = descripcion;
        this.id_Destino = id_Destino;
        this.query = new QueryEnvio();
    }
    public AbsQuery getQuery(){
        return this.query;
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public double getLatitude() {
        return this.latitude;
    }

    @Override
    public double getLongitude() {
        return this.longitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;

    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String toString(){
        return this.date+" , "+this.user.toString()+" , "+this.resource.toString()+" , "+id_Destino+" , "+descripcion+" ; "+this.latitude+" , "+this.longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Persona getUser() {
        return user;
    }

    public void setUser(Persona user) {
        this.user = user;
    }

    public Recurso getResource() {
        return resource;
    }

    public void setResource(Recurso resource) {
        this.resource = resource;
    }

    public int getId_Destino() {
        return id_Destino;
    }

    public void setId_Destino(int id_Destino) {
        this.id_Destino = id_Destino;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

     /*DATOS BIEN PERO NO EJECUTA MAS DE UNA CONSULTA EN LA BASE
        Log.d("CLASE ENVIO"," = "+this.toString());
        String query = " INSERT INTO USERS_RESOURCES(id,user_id,resource_id,cantidad,description)" +
                       " VALUES('id',"+this.id_Destino+","+this.resource.getId_resource()+
                       ", 1 ,'"+this.descripcion+"');"+ " "+//INSERTO EL RECURSO AL USUARIO
                       "DELETE FROM USERS_RESOURCES " +
                       "WHERE user_id = "+this.user.getId()+
                       "  AND resource_id = "+this.resource.getId_resource()+
                       "  AND track_code = '"+this.resource.getTrack_code()+"'; "+
                       "INSERT INTO RESOUCES_ROUTE (id,id_resource,id_origen,id_destino,date,latitude,longitude,track_code) " +
                       "VALUES ('id',"+this.resource.getId_resource()+","+this.user.getId()+","+this.id_Destino+",'"+this.date+"',"+this.latitude+","+this.longitude+",'"+this.resource.getTrack_code()+"')";


        return query;*/
}
