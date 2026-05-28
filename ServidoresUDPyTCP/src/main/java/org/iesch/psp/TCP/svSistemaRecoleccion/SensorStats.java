package org.iesch.psp.TCP.svSistemaRecoleccion;

public class SensorStats {
    private int totalLecturas = 0;
    private double sumaTemperaturas = 0.0;

    // AL PONER SYNCHRONIZED, SOLO UN HILO PUEDE EJECUTAR ESTO A LA VEZ
    public synchronized void registrarLectura(double temperatura) {
        totalLecturas++;                     // Sumamos 1 al contador
        sumaTemperaturas += temperatura;     // Acumulamos la temperatura
    }

    // También sincronizamos la lectura para que nadie lea mientras otro escribe
    public synchronized String getEstadisticas() {
        return "Total lecturas: " + totalLecturas + ", Suma temp: " + sumaTemperaturas;
    }
}