package VotacionElectronicaSincronizada;

import java.io.Serializable;

public class DatosVoto implements Serializable {

    private byte[] opcionCifrada;
    private byte[] iv;

    public DatosVoto(byte[] opcionCifrada, byte[] iv) {
        this.opcionCifrada = opcionCifrada;
        this.iv = iv;
    }

    public byte[] getOpcionCifrada() {
        return opcionCifrada;
    }

    public byte[] getIv() {
        return iv;
    }
}