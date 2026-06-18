package FirmaDigitalDocumentos;

import java.io.Serializable;

public class RespuestaVerificacion implements Serializable {

    private boolean valido;
    private String mensaje;

    public RespuestaVerificacion(boolean valido, String mensaje) {
        this.valido = valido;
        this.mensaje = mensaje;
    }

    public boolean isValido() {
        return valido;
    }

    public String getMensaje() {
        return mensaje;
    }
}