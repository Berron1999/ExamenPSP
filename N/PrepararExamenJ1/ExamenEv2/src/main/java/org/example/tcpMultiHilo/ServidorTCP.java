package org.example.tcpMultiHilo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor TCP multihilo.
 *
 * El servidor escucha en el puerto 7001 y acepta múltiples clientes.
 * Por cada cliente conectado, crea un nuevo hilo de tipo HiloCliente.
 */
public class ServidorTCP {

    private static final int PUERTO_SERVIDOR = 7001;

    public static void main(String[] args) {

        /*
         * Creamos el ServerSocket asociado al puerto 7001.
         * Este socket queda escuchando conexiones TCP entrantes.
         */
        try (ServerSocket serverSocket = new ServerSocket(PUERTO_SERVIDOR)) {

            System.out.println("Servidor TCP iniciado en el puerto " + PUERTO_SERVIDOR);
            System.out.println("Esperando clientes...");

            /*
             * Bucle infinito para aceptar clientes continuamente.
             * Cada cliente será atendido en un hilo independiente.
             */
            while (true) {

                /*
                 * accept() bloquea la ejecución hasta que un cliente se conecta.
                 * Cuando un cliente entra, devuelve un Socket específico para ese cliente.
                 */
                Socket socketCliente = serverSocket.accept();

                System.out.println("--------------------------------------");
                System.out.println("Cliente conectado.");
                System.out.println("IP cliente: " + socketCliente.getInetAddress().getHostAddress());
                System.out.println("Puerto cliente: " + socketCliente.getPort());

                /*
                 * Creamos un hilo para atender al cliente.
                 * Esto permite que el servidor pueda seguir aceptando más clientes.
                 */
                HiloCliente hiloCliente = new HiloCliente(socketCliente);

                /*
                 * Iniciamos el hilo. Esto ejecutará el método run() de HiloCliente.
                 */
                hiloCliente.start();
            }

        } catch (IOException e) {
            System.out.println("Error en el servidor TCP: " + e.getMessage());
        }
    }
}
