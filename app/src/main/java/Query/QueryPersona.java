package Query;

import android.support.v7.app.AppCompatActivity;

import Funcionalidad.Elemento;
import Funcionalidad.Persona;
import TareasAsincronas.TaskInsert;

/**
 * Created by Maxi on 8/5/2017.
 */

public class QueryPersona extends AbsQuery{
    public QueryPersona(){

    }
    public void executeQuery(Elemento E, AppCompatActivity activity) {
        Persona user = (Persona) E;
        TaskInsert task = new TaskInsert(E,activity);
        String query = "INSERT INTO USERS(id,role_id,username,password,name,email,address,phone," +
                                         "location_id,website,latitude, longitude) " +
                       "VALUES(`id`,2,'"+user.getUser()+"','"+user.getPass()+"'," +
                       "'"+user.getNombre()+"','"+user.getEmail()+"'," +
                       "'"+user.getDireccion()+"','"+user.getTelefono()+"'," +
                       "'"+user.getCiudad()+"','"+user.getWeb()+"',"+
                       " "+user.getLatitude()+","+" "+user.getLongitude()+")";
        task.execute(query);
    }
}
