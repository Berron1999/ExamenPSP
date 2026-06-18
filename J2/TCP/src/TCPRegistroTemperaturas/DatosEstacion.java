package TCPRegistroTemperaturas;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DatosEstacion implements Serializable {
    private int idEstacion;
    private double temperatura;
    private LocalDateTime fechaHora;

    public DatosEstacion(int idEstacion, double temperatura, LocalDateTime fechaHora) {
        this.idEstacion = idEstacion;
        this.temperatura = temperatura;
        this.fechaHora = fechaHora;
    }

    public int getIdEstacion() {
        return idEstacion;
    }

    public void setIdEstacion(int idEstacion) {
        this.idEstacion = idEstacion;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
