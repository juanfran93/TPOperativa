package Funcionalidad;

import Query.AbsQuery;

/**
 * Created by Maxi on 3/5/2017.
 */

public abstract class Elemento {
    protected double latitude;
    protected double longitude;
    protected AbsQuery query;


    public abstract AbsQuery getQuery();

    public abstract String getIdentifier();

    public abstract double getLatitude();

    public abstract double getLongitude();

    public abstract void setLatitude(double latitude);

    public abstract void setLongitude(double longitude);

    public String toString(){
        return this.query.toString();
    }
}
