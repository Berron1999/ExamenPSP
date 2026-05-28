package org.example.tcpMultihilo2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Hilo encargado de atender a un cliente TCP concreto.
 *
 * Cada cliente conectado al servidor tendrá su propio HiloCliente.
 * El cliente envía una frase y el servidor calcula cuántas vocales contiene.
 */
public class HiloCliente extends Thread {

    private Socket socketCliente;

    /**
     * Constructor del hilo.
     *
     * @param socketCliente socket asociado al cliente conectado
     */
    public HiloCliente(Socket socketCliente) {
        this.socketCliente = socketCliente;
    }

    /**
     * Código que se ejecuta cuando se llama al método start().
     */
    @Override
    public void run() {

        try (
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(socketCliente.getInputStream())
                );

                PrintWriter salida = new PrintWriter(
                        socketCliente.getOutputStream(),
                        true
                )
        ) {

            /*
             * Leemos la frase enviada por el cliente.
             * El cliente la envía usando println(), por eso aquí usamos readLine().
             */
            String fraseRecibida = entrada.readLine();

            System.out.println("Frase recibida del cliente "
                    + socketCliente.getPort() + ": " + fraseRecibida);

            /*
             * Calculamos el número de vocales de la frase recibida.
             */
            int numeroVocales = contarVocales(fraseRecibida);

            /*
             * Creamos la respuesta solicitada por el enunciado.
             */
            String respuesta = "Puerto origen del cliente: " + socketCliente.getPort()
                    + " | Frase recibida: " + fraseRecibida
                    + " | Número de vocales: " + numeroVocales;

            /*
             * Enviamos la respuesta al cliente.
             */
            salida.println(respuesta);

            System.out.println("Respuesta enviada al cliente " + socketCliente.getPort());

        } catch (IOException e) {
            System.out.println("Error atendiendo al cliente: " + e.getMessage());

        } finally {
            try {
                socketCliente.close();
                System.out.println("Conexión cerrada con el cliente.");
            } catch (IOException e) {
                System.out.println("Error cerrando el socket del cliente.");
            }
        }
    }

    /**
     * Cuenta cuántas vocales contiene una frase.
     *
     * @param frase frase recibida del cliente
     * @return número total de vocales encontradas
     */
    private int contarVocales(String frase) {

        int contador = 0;

        for (char c : frase.toLowerCase().toCharArray()) {
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                contador++;
            }
        }

        return contador;
    }
}