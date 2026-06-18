package ServidorCalificacionesCifradas;

import java.io.Serializable;

public class RespuestaCifrada implements Serializable {

    private byte[] datosCifrados;
    private byte[] iv;
    private String hashIntegridad;

    public RespuestaCifrada(byte[] datosCifrados, byte[] iv, String hashIntegridad) {
        this.datosCifrados = datosCifrados;
        this.iv = iv;
        this.hashIntegridad = hashIntegridad;
    }

    public byte[] getDatosCifrados() {
        return datosCifrados;
    }

    public byte[] getIv() {
        return iv;
    }

    public String getHashIntegridad() {
        return hashIntegridad;
    }
}