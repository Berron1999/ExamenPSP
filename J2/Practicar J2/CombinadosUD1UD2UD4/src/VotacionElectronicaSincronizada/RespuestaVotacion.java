package VotacionElectronicaSincronizada;

import java.io.Serializable;

public class RespuestaVotacion implements Serializable {

    private String resumen;

    public RespuestaVotacion(String resumen) {
        this.resumen = resumen;
    }

    public String getResumen() {
        return resumen;
    }
}