package org.example.tcpMultiHilo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;

/**
 * Hilo encargado de atender a un cliente TCP concreto.
 *
 * Cada cliente conectado al servidor tendrá su propio HiloCliente.
 * El cliente envía un número entero positivo y el hilo calcula su factorial.
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
     * Código que se ejecuta cuando se llama a start().
     */
    @Override
    public void run() {

        /*
         * Usamos try-with-resources para cerrar automáticamente
         * los flujos de entrada y salida.
         */
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
             * Leemos el número enviado por el cliente.
             * El cliente lo enviará como texto usando println().
             */
            String lineaRecibida = entrada.readLine();

            System.out.println("Dato recibido del cliente " + socketCliente.getPort() + ": " + lineaRecibida);

            /*
             * Convertimos el texto recibido a número entero.
             */
            int numero = Integer.parseInt(lineaRecibida);

            /*
             * Validamos que el número sea positivo.
             */
            if (numero < 0) {
                salida.println("Error: el número debe ser entero positivo.");
                return;
            }

            /*
             * Calculamos el factorial del número recibido.
             */
            BigInteger factorial = calcularFactorial(numero);

            /*
             * Creamos la respuesta solicitada por el enunciado.
             */
            String respuesta = "Puerto origen del cliente: " + socketCliente.getPort()
                    + " | Número recibido: " + numero
                    + " | Resultado del factorial: " + factorial;

            /*
             * Enviamos la respuesta al cliente.
             */
            salida.println(respuesta);

            System.out.println("Respuesta enviada al cliente " + socketCliente.getPort());

        } catch (NumberFormatException e) {
            System.out.println("El cliente no envió un número entero válido.");

            try {
                PrintWriter salida = new PrintWriter(socketCliente.getOutputStream(), true);
                salida.println("Error: debes enviar un número entero válido.");
            } catch (IOException ex) {
                System.out.println("No se pudo enviar mensaje de error al cliente.");
            }

        } catch (IOException e) {
            System.out.println("Error atendiendo al cliente: " + e.getMessage());

        } finally {
            /*
             * Cerramos el socket del cliente al terminar.
             */
            try {
                socketCliente.close();
                System.out.println("Conexión cerrada con el cliente.");
            } catch (IOException e) {
                System.out.println("Error cerrando el socket del cliente.");
            }
        }
    }

    /**
     * Calcula el factorial de un número entero positivo.
     *
     * Ejemplo:
     * 5! = 5 * 4 * 3 * 2 * 1 = 120
     *
     * @param numero número del que se quiere calcular el factorial
     * @return factorial calculado como BigInteger
     */
    private BigInteger calcularFactorial(int numero) {

        BigInteger resultado = BigInteger.ONE;

        for (int i = 2; i <= numero; i++) {
            resultado = resultado.multiply(BigInteger.valueOf(i));
        }

        return resultado;
    }
}
