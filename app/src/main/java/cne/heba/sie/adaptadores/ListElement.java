package cne.heba.sie.adaptadores;

import java.io.Serializable;

public class ListElement implements Serializable {

    public String id_pe;
    public String colorC;
    public String nameC;
    public String curpC;
    public String fechaC;

    public ListElement(String id_pe, String colorC, String nameC, String curpC, String fechaC) {
        this.id_pe = id_pe;
        this.colorC = colorC;
        this.nameC = nameC;
        this.curpC = curpC;
        this.fechaC = fechaC;
    }

    public String getId_pe() {
        return id_pe;
    }

    public void setId_pe(String id_pe) {
        this.id_pe = id_pe;
    }

    public String getColorC() {
        return colorC;
    }

    public void setColorC(String colorC) {
        this.colorC = colorC;
    }

    public String getNameC() {
        return nameC;
    }

    public void setNameC(String nameC) {
        this.nameC = nameC;
    }

    public String getCurpC() {
        return curpC;
    }

    public void setCurpC(String curpC) {
        this.curpC = curpC;
    }

    public String getFechaC() {
        return fechaC;
    }

    public void setFechaC(String fechaC) {
        this.fechaC = fechaC;
    }
}
