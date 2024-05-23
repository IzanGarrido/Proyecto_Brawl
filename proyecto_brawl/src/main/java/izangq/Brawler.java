package izangq;

public class Brawler {
    private int idBrawler;
    private String brawler;
    private int idMapa;
    private int idCalidad;
    private int aspectos;
    private String origen;

    public Brawler(int idBrawler, String brawler, int idMapa, int idCalidad, int aspectos, String origen) {
        this.idBrawler = idBrawler;
        this.brawler = brawler;
        this.idMapa = idMapa;
        this.idCalidad = idCalidad;
        this.aspectos = aspectos;
        this.origen = origen;
    }

    public int getIdBrawler() {
        return idBrawler;
    }

    public String getBrawler() {
        return brawler;
    }

    public int getIdMapa() {
        return idMapa;
    }

    public int getIdCalidad() {
        return idCalidad;
    }

    public int getAspectos() {
        return aspectos;
    }

    public String getOrigen() {
        return origen;
    }
}
