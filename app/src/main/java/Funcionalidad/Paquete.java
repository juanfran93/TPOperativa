package Funcionalidad;

/**
 * Created by Flia. Ferreira on 07/04/2018.
 */

public class Paquete {
    private int id;
    private int cantidad;
    private int idPadre;
    private int idResource;
    private int enUso;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
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
    public int getEnUso() {
        return enUso;
    }
    public void setEnUso(int enUso) {
        this.enUso = enUso;
    }
}
