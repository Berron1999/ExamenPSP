package TCP.ReservaMesas;
import java.io.Serializable;

public class RespuestaReserva implements Serializable {

    private String mensaje;
    private boolean exito;

    public RespuestaReserva(String mensaje, boolean exito) {
        this.mensaje = mensaje;
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public boolean isExito() {
        return exito;
    }

    @Override
    public String toString() {
        return mensaje;
    }
}