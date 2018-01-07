package Funcionalidad;

/**
 * Created by Maxi on 6/5/2017.
 */

public class Recurso {
    private int id_resource;
    private String track_code;
    private String name;

    public Recurso(int id_resource, String track_code, String name){
        this.id_resource = id_resource;
        this.track_code = track_code;
        this.name = name;

    }

    public Recurso(int id, String name) {
        this.id_resource = id;
        this.name = name;
    }

    public int getId_resource() {
        return id_resource;
    }

    public void setId_resource(int id_resource) {
        this.id_resource = id_resource;
    }

    public String getTrack_code() {
        return track_code;
    }

    public void setTrack_code(String track_code) {
        this.track_code = track_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return this.name+" , "+this.id_resource+" , "+this.track_code+" ; ";
    }
}
