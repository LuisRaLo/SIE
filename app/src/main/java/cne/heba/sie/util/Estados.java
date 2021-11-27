package cne.heba.sie.util;

public class Estados {

    private Integer id;
    private String nombre;

    public Estados(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public  Estados(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
