package Ejercicios2.Ej5;

import Ejercicios2.Ej5.Saldo;

// Clase que implementa Runnable → se ejecutará en un hilo separado
public class TareaDeposito implements Runnable {

    private Saldo saldo;    // recurso compartido entre todos los hilos
    private double cantidad;

    public TareaDeposito(Saldo saldo, double cantidad) {
        this.saldo = saldo;
        this.cantidad = cantidad;
    }

    @Override
    public void run() {
        // Llamamos al método sincronizado → solo entra un hilo a la vez
        saldo.añadir(Thread.currentThread().getName(), cantidad);
    }
}