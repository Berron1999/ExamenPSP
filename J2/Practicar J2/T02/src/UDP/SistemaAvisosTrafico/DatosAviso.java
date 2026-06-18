package UDP.SistemaAvisosTrafico;

import java.io.Serializable;

public class DatosAviso implements Serializable {

    private String carretera;
    private int gravedad;

    public DatosAviso(String carretera, int gravedad) {
        this.carretera = carretera;
        this.gravedad = gravedad;
    }

    public String getCarretera() {
        return carretera;
    }

    public int getGravedad() {
        return gravedad;
    }

    @Override
    public String toString() {
        return "Aviso{carretera='" + carretera + "', gravedad=" + gravedad + "}";
    }
}