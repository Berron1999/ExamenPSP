package org.iesch.psp.TCP.VentaDeEntradas;

public class SimuladorTaquillas {
    public static void main(String[] args) {
        System.out.println("Iniciando avalancha de ventas (40 terminales simultáneos)...");

        // Arrancamos 40 hilos de golpe
        for (int i = 1; i <= 40; i++) {
            ClienteComprador comprador = new ClienteComprador("Terminal_" + i);
            Thread hilo = new Thread(comprador);
            hilo.start();
        }
    }
}