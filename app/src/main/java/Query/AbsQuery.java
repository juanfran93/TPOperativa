package Query;

import android.support.v7.app.AppCompatActivity;

import Funcionalidad.Elemento;

/**
 * Created by Maxi on 8/5/2017.
 */

public abstract class AbsQuery {
    protected String query;

    public abstract void executeQuery(Elemento elemento, AppCompatActivity activity);

    public String toString(){
        return this.query;
    }
}
