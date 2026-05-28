package MonitorDeEstadoDeServidor;

import MonitorDeEstadoDeServidor.EstadoHilo;

// Hilo interno del servidor que simula trabajo continuo (contador)
public class HiloTrabajador implements Runnable {

    private String nombre;
    private long   iteracion = 0;    // cuántas veces ha "trabajado"
    private boolean activo   = true; // para poder pararlo desde fuera
    private int    pausa;            // ms entre iteraciones

    public HiloTrabajador(String nombre, int pausaMs) {
        this.nombre = nombre;
        this.pausa  = pausaMs;
    }

    @Override
    public void run() {
        while (activo) {
            iteracion++;
            try {
                Thread.sleep(pausa); // simula trabajo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[Servidor] Hilo " + nombre + " finalizado.");
    }

    // El cliente monitor llama a este método para obtener una instantánea del estado
    public EstadoHilo getEstado(Thread hilo) {
        return new EstadoHilo(
                nombre,
                iteracion,
                hilo.isAlive(),
                activo ? "TRABAJANDO" : "FINALIZADO"
        );
    }

    public String getNombre()  { return nombre; }
    public void   parar()      { activo = false; }
}