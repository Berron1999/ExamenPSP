package TCPRegistroTemperaturas;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RespuestaServidor implements Serializable {

    private String idEstacion;
    private String hashGenerado;           // SHA-256 de idEstacion + temperatura
    private boolean alertaCalor;            // true si temperatura > 30°C, false en caso contrario
    private LocalDateTime fechaRecepcion;  // Momento en que el servidor recibió el objeto

    public RespuestaServidor(String idEstacion, String hashGenerado, boolean alertaCalor, LocalDateTime fechaRecepcion) {
        this.idEstacion = idEstacion;
        this.hashGenerado = hashGenerado;
        this.alertaCalor = alertaCalor;
        this.fechaRecepcion = fechaRecepcion;
    }

    public String getIdEstacion() {
        return idEstacion;
    }

    public void setIdEstacion(String idEstacion) {
        this.idEstacion = idEstacion;
    }

    public String getHashGenerado() {
        return hashGenerado;
    }

    public void setHashGenerado(String hashGenerado) {
        this.hashGenerado = hashGenerado;
    }

    public boolean isAlertaCalor() {
        return alertaCalor;
    }

    public void setAlertaCalor(boolean alertaCalor) {
        this.alertaCalor = alertaCalor;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }
}
