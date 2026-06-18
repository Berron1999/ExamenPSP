package TCP.ValidacionProductos;

import java.io.Serializable;

public class RespuestaValidacion implements Serializable {

    private String hash;
    private boolean valido;

    public RespuestaValidacion(String hash, boolean valido) {
        this.hash = hash;
        this.valido = valido;
    }

    public String getHash() {
        return hash;
    }

    public boolean isValido() {
        return valido;
    }

    @Override
    public String toString() {
        return "RespuestaValidacion{hash='" + hash + "', valido=" + valido + "}";
    }
}