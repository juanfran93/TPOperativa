package Funcionalidad;

import java.sql.ResultSet;

/**
 * Created by Maxi on 23/3/2017.
 */

public abstract class AbsQuery {

    protected boolean existe;

    public abstract void execute(String dateBase,String user, String pass);
    public boolean isExists(){
        return existe;
    };
}
