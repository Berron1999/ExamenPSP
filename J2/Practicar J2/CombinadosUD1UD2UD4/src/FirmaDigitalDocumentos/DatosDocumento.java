package FirmaDigitalDocumentos;

import java.io.Serializable;

public class DatosDocumento implements Serializable {

    private String contenido;
    private byte[] firma;
    private byte[] clavePublica;

    public DatosDocumento(String contenido, byte[] firma, byte[] clavePublica) {
        this.contenido = contenido;
        this.firma = firma;
        this.clavePublica = clavePublica;
    }

    public String getContenido() {
        return contenido;
    }

    public byte[] getFirma() {
        return firma;
    }

    public byte[] getClavePublica() {
        return clavePublica;
    }
}