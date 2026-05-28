package org.iesch.psp.TCP.svSistemaRecoleccion;

import org.iesch.psp.TCP.svSistemaRecoleccion.SensorServerThread;
import org.iesch.psp.TCP.svSistemaRecoleccion.SensorStats;

import java.net.ServerSocket;
import java.net.Socket;

public class SensorServer {
    public static void main(String[] args) {
        int puerto = 5000;
        try (ServerSocket svr = new ServerSocket(puerto)) {
            System.out.println("Servidor iniciado...");

            // ¡AQUÍ! FUERA DEL BUCLE. Se crea UNA sola vez.
            SensorStats estadisticas = new SensorStats();

            while (true) {
                Socket cli = svr.accept();

                // Todos los clientes reciben la misma "pizarra" compartida
                SensorServerThread trabajador = new SensorServerThread(cli, estadisticas);
                new Thread(trabajador).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}