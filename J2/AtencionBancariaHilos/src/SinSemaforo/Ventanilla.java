package SinSemaforo;

import java.util.Random;

public class Ventanilla {

    private int id;
    private int clientesAtendidos;
    private double dineroGestionado;
    private Random random = new Random();

    public Ventanilla(int id) {
        this.id = id;
        this.clientesAtendidos = 0;
        this.dineroGestionado = 0;
    }

    public int getId() {
        return id;
    }

    // synchronized: solo un cliente a la vez puede ejecutar este método
    // los demás hilos que llamen a atender() quedarán bloqueados esperando
    public synchronized void atender(int idCliente) throws InterruptedException {
        System.out.println("Cliente " + idCliente + " siendo atendido en ventanilla " + id);

        // Tiempo de atención entre 500 y 1500ms
        Thread.sleep(500 + random.nextInt(1001));

        // Operación entre 50€ y 1000€
        double operacion = 50 + random.nextInt(951);
        clientesAtendidos++;
        dineroGestionado += operacion;

        System.out.println("Cliente " + idCliente + " abandona ventanilla " + id + " (operación: " + operacion + "€)");
    }

    public int getClientesAtendidos() {
        return clientesAtendidos;
    }

    public double getDineroGestionado() {
        return dineroGestionado;
    }
}