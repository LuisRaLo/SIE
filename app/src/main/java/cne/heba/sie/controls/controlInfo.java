package cne.heba.sie.controls;

public class controlInfo {

    public String colorCard;
    public String crpCard;
    public String nombreCard;
    public String fechaCard;

    public controlInfo(String colorCard, String crpCard, String nombreCard, String fechaCard) {
        this.colorCard = colorCard;
        this.crpCard = crpCard;
        this.nombreCard = nombreCard;
        this.fechaCard = fechaCard;
    }

    public String getColorCard() {
        return colorCard;
    }

    public void setColorCard(String colorCard) {
        this.colorCard = colorCard;
    }

    public String getCrpCard() {
        return crpCard;
    }

    public void setCrpCard(String crpCard) {
        this.crpCard = crpCard;
    }

    public String getNombreCard() {
        return nombreCard;
    }

    public void setNombreCard(String nombreCard) {
        this.nombreCard = nombreCard;
    }

    public String getFechaCard() {
        return fechaCard;
    }

    public void setFechaCard(String fechaCard) {
        this.fechaCard = fechaCard;
    }
}
