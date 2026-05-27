package EchoServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServidor {

    static final int PUERTO               = 5008;
    static final int NUM_CLIENTES         = 100;

    // Contador compartido entre todos los hilos del servidor
    static int mensajesRecibidos = 0;

    // synchronized → evita condición de carrera al incrementar desde varios hilos
    static synchronized void sumarRecibido() { mensajesRecibidos++; }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("[Servidor] Iniciado en puerto " + PUERTO);
            int conectados = 0;

            while (conectados < NUM_CLIENTES) {
                Socket socketCliente = serverSocket.accept();
                conectados++;
                System.out.println("[Servidor] Cliente " + conectados + " conectado.");
                // Un hilo por cliente para atender múltiples conexiones simultáneas
                new Thread(new HiloAtencionCliente(socketCliente)).start();
            }

            System.out.println("[Servidor] Total mensajes recibidos: " + mensajesRecibidos);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}