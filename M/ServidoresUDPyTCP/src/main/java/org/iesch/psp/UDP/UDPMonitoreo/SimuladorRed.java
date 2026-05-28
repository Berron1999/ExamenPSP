package org.iesch.psp.UDP.UDPMonitoreo;

public class SimuladorRed {
    public static void main(String[] args) {
        int numeroEquipos = 30;

        System.out.println("Iniciando Test de Estrés con " + numeroEquipos + " equipos...");

        for (int i = 1; i <= numeroEquipos; i++) {
            // Creamos un agente nuevo dándole un nombre (Equipo_1, Equipo_2...)
            AgenteCliente agente = new AgenteCliente("Equipo_" + i);

            // Lo metemos en un hilo y lo arrancamos
            Thread hilo = new Thread(agente);
            hilo.start();
        }

        System.out.println("Todos los equipos han sido lanzados.");
    }
}