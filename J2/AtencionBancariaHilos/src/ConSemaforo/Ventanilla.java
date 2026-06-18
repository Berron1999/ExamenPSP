package ConSemaforo;

import java.util.concurrent.Semaphore;

public class Ventanilla {

    private int id;                  // Número de ventanilla
    private int clientesAtendidos;   // Cuántos clientes ha atendido
    private double dineroGestionado; // Dinero total gestionado

    // Semáforo con 1 permiso: solo un cliente puede ser atendido a la vez
    private Semaphore semaforo = new Semaphore(1);

    public Ventanilla(int id) {
        this.id = id;
        this.clientesAtendidos = 0;
        this.dineroGestionado = 0;
    }

    public int getId() {
        return id;
    }

    // Sin synchronized, el semáforo ya nos protege
    public void registrarAtencion(double dinero) {
        clientesAtendidos++;
        dineroGestionado += dinero;
    }
    public int getClientesAtendidos() {
        return clientesAtendidos;
    }

    public double getDineroGestionado() {
        return dineroGestionado;
    }

    // El cliente pide paso → si hay otro siendo atendido, espera aquí bloqueado
    public void adquirir() throws InterruptedException {
        semaforo.acquire();
    }

    // El cliente termina y libera la ventanilla para el siguiente
    public void liberar() {
        semaforo.release();
    }
}