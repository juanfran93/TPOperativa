package Funcionalidad;

/**
 * Created by lucho on 07/02/2018.
 */

public class Peticion {
    private int id_peticion;
    private int id_recurso;

    public int getId_recurso() {
        return id_recurso;
    }

    public void setId_recurso(int id_recurso) {
        this.id_recurso = id_recurso;
    }

    private String recurso;
    private int cantidad;
    private int id_origen;
    private int id_package;


    public int getId_package() {
        return id_package;
    }
    public void setId_package(int id_package) {
        this.id_package = id_package;
    }
    public int getId_origen() {
        return id_origen;
    }
    public void setId_origen(int id_origen) {
        this.id_origen = id_origen;
    }

    public String getRecurso() {
        return recurso;
    }
    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public int getId_peticion() {
        return id_peticion;
    }
    public void setId_peticion(int id_peticion) {
        this.id_peticion = id_peticion;
    }



}