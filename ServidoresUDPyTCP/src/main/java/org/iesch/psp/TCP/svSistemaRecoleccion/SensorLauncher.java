package org.iesch.psp.TCP.svSistemaRecoleccion;

public class SensorLauncher {
    public static void main(String[] args) {
        int numeroSensores = 50;
        int lecturasPorSensor = 200;

        System.out.println("Iniciando simulación de sensores...");

        for (int i = 0; i < numeroSensores; i++) {
            // 1. Instanciamos el cliente
            SensorClient cliente = new SensorClient(i, lecturasPorSensor);

            // 2. Lo metemos en un hilo y lo arrancamos
            Thread hilo = new Thread(cliente);
            hilo.start();
        }

        System.out.println("Todos los sensores han sido lanzados.");
    }
}