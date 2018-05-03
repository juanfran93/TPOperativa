package Funcionalidad;

/**
 * Created by Flia. Ferreira on 07/04/2018.
 */

public class Paquete {
    private int id;
    private double cantidad;
    private int idPadre;
    private int idResource;
    private double enUso;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public double getCantidad() {
        return cantidad;
    }
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
    public int getIdPadre() {
        return idPadre;
    }
    public void setIdPadre(int idPadre) {
        this.idPadre = idPadre;
    }
    public int getIdResource() {
        return idResource;
    }
    public void setIdResource(int idResource) {
        this.idResource = idResource;
    }
    public double getEnUso() {
        return enUso;
    }
    public void setEnUso(double enUso) {
        this.enUso = enUso;
    }
}
