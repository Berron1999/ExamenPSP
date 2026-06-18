package TCPSSLTLS;

import java.io.Serializable;
import java.time.LocalDateTime;

// Objeto que el cliente envía al servidor
public class DatosMensaje implements Serializable {

    private String nombreUsuario;
    private String mensaje;
    private LocalDateTime fechaHora; // Momento en que el cliente crea el objeto

    public DatosMensaje(String nombreUsuario, String mensaje, LocalDateTime fechaHora) {
        this.nombreUsuario = nombreUsuario;
        this.mensaje = mensaje;
        this.fechaHora = fechaHora;
    }

    public String getNombreUsuario() { return nombreUsuario; }
    public String getMensaje() { return mensaje; }
    public LocalDateTime getFechaHora() { return fechaHora; }
}