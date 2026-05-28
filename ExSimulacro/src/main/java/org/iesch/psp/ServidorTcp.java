package org.iesch.psp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTcp {
    public static final int PUERTO = 5000;

    public static void main(String[] args) {
        System.out.println("Servidor TCP iniciado en puerto " + PUERTO);
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            while (true) {
                // Acepta conexiones y crea un hilo por cliente.
                Socket cliente = servidor.accept();
                HiloCliente hilo = new HiloCliente(cliente);
                hilo.start();
            }
        } catch (IOException e) {
            System.out.println("Error en servidor: " + e.getMessage());
        }
    }
}
