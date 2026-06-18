package ServidorMensajeriaSeguraTCP;

import java.io.Serializable;

public class MensajeCifrado implements Serializable {

    private byte[] datosCifrados;
    private byte[] iv;
    private String hash;

    public MensajeCifrado(byte[] datosCifrados, byte[] iv, String hash) {
        this.datosCifrados = datosCifrados;
        this.iv = iv;
        this.hash = hash;
    }

    public byte[] getDatosCifrados() {
        return datosCifrados;
    }

    public byte[] getIv() {
        return iv;
    }

    public String getHash() {
        return hash;
    }
}