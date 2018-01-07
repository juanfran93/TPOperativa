package Query;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

import Funcionalidad.Elemento;
import Funcionalidad.Envio;
import TareasAsincronas.TaskInsert;

/**
 * Created by Maxi on 8/5/2017.
 */

public class QueryEnvio extends AbsQuery {

    public QueryEnvio() {
    }


    public void executeQuery(Elemento elemento, AppCompatActivity activity) {
        Log.d("CLASE_QUERYENVIO","EXECUTEQUERY");
        executeQuery1((Envio) elemento,activity);
        executeQuery2((Envio) elemento,activity);
        executeQuery3((Envio) elemento,activity);

    }

    private void executeQuery3(Envio envio, AppCompatActivity activity) {
        String query3 = "DELETE FROM USERS_RESOURCES " +
                "WHERE user_id = "+envio.getUser().getId()+
                "  AND resource_id = "+envio.getResource().getId_resource()+
                "  AND track_code = '"+envio.getResource().getTrack_code()+"'";

        TaskInsert task3 = new TaskInsert(envio,activity);
        task3.execute(query3);

    }

    private void executeQuery1(Envio envio, AppCompatActivity activity) {
        String query1 = " INSERT INTO USERS_RESOURCES(id,user_id,resource_id,cantidad,description,track_code)" +
                " VALUES('id',"+envio.getId_Destino()+","+envio.getResource().getId_resource()+
                ", 1 ,'"+envio.getDescripcion()+"','"+envio.getResource().getTrack_code()+"') ";

        Log.d("TASK1","= "+query1);
        TaskInsert task1 = new TaskInsert(envio,activity);
        task1.execute(query1);
    }

    private void executeQuery2(Envio envio, AppCompatActivity activity) {
        String query2 =  "INSERT INTO RESOURCE_ROUTE (id,id_resource,id_origen,id_destino,date,latitude,longitude,track_code) " +
                "VALUES ('id',"+envio.getResource().getId_resource()+","+envio.getUser().getId()+","+envio.getId_Destino()+",'"+
                envio.getDate()+"',"+envio.getLatitude()+","+envio.getLongitude()+",'"+envio.getResource().getTrack_code()+"')";
        Log.d("TASK2","= "+query2);
        TaskInsert task2 = new TaskInsert(envio,activity);
        task2.execute(query2);
    }

}
