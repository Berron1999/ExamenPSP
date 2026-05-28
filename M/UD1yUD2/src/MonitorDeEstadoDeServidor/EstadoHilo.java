package MonitorDeEstadoDeServidor;

import java.io.Serializable;

// Objeto serializable que representa el estado de un hilo en un momento dado
// Se envía por el socket al cliente
public class EstadoHilo implements Serializable {

    private String nombre;
    private long   iteracion;
    private boolean vivo;
    private String estado; // "TRABAJANDO" o "FINALIZADO"

    public EstadoHilo(String nombre, long iteracion, boolean vivo, String estado) {
        this.nombre    = nombre;
        this.iteracion = iteracion;
        this.vivo      = vivo;
        this.estado    = estado;
    }

    @Override
    public String toString() {
        return String.format("%-15s | Iteración: %-8d | Vivo: %-5s | Estado: %s",
                nombre, iteracion, vivo, estado);
    }
}