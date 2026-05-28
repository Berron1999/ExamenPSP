package org.example.tcpMultiHilo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Cliente TCP que se conecta al servidor en localhost:7001.
 *
 * Envía un número entero positivo y recibe el factorial calculado.
 */
public class ClienteTCP {

    private static final String HOST_SERVIDOR = "localhost";
    private static final int PUERTO_SERVIDOR = 7001;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduce un número entero positivo:");
        int numero = scanner.nextInt();

        /*
         * Creamos el socket del cliente.
         * Al conectarnos a localhost:7001, buscamos el servidor TCP.
         */
        try (
                Socket socket = new Socket(HOST_SERVIDOR, PUERTO_SERVIDOR);

                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );

                PrintWriter salida = new PrintWriter(
                        socket.getOutputStream(),
                        true
                )
        ) {

            System.out.println("Conectado al servidor TCP.");
            System.out.println("Puerto local del cliente: " + socket.getLocalPort());

            /*
             * Enviamos el número al servidor.
             * Usamos println para enviar salto de línea.
             * Esto es importante porque el servidor usa readLine().
             */
            salida.println(numero);

            /*
             * Esperamos la respuesta del servidor.
             */
            String respuesta = entrada.readLine();

            /*
             * Mostramos la respuesta recibida.
             */
            System.out.println("Respuesta recibida del servidor:");
            System.out.println(respuesta);

        } catch (IOException e) {
            System.out.println("Error en el cliente TCP: " + e.getMessage());
        }
    }
}
