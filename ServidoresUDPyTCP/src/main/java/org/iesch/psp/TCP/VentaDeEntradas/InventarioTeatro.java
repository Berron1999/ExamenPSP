package org.iesch.psp.TCP.VentaDeEntradas;

public class InventarioTeatro {
    private int entradasDisponibles = 500;

    // Ponemos synchronized para que nadie lea mientras otro compra
    public synchronized int getEntradasDisponibles() {
        return entradasDisponibles;
    }

    // Ponemos synchronized para que la comprobación y la resta sean ATÓMICAS (indivisibles)
    public synchronized boolean venderEntradas(int cantidad) {
        if (entradasDisponibles >= cantidad) {
            // Ponemos un pequeño retraso simulado (opcional) para demostrar
            // que incluso si tarda, nadie más puede colarse en este bloque
            try { Thread.sleep(10); } catch (InterruptedException e) {}

            entradasDisponibles -= cantidad;
            return true; // Compra exitosa
        } else {
            return false; // No hay suficientes entradas
        }
    }
}