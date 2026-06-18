package TransferenciasBancariasSeguras;

import java.io.Serializable;

public class RespuestaTransferencia implements Serializable {

    private String mensaje;
    private String hmacRespuesta;

    public RespuestaTransferencia(String mensaje, String hmacRespuesta) {
        this.mensaje = mensaje;
        this.hmacRespuesta = hmacRespuesta;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getHmacRespuesta() {
        return hmacRespuesta;
    }
}