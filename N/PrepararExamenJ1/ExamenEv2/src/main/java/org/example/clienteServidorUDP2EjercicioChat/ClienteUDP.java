package org.example.clienteServidorUDP2EjercicioChat;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClienteUDP {

    private static final String HOST_SERVIDOR = "localhost";
    private static final int PUERTO_SERVIDOR = 7100;

    public static void main(String[] args) {

        try (
                DatagramSocket socket = new DatagramSocket();
                Scanner scanner = new Scanner(System.in)
        ) {

            InetAddress direccionServidor = InetAddress.getByName(HOST_SERVIDOR);

            System.out.print("Dime el número del que quieras hacer la raíz cuadrada: ");
            double numero = scanner.nextDouble();

            /*
             * IMPORTANTE:
             * Enviamos solo el número, no una frase entera.
             * Así el servidor puede convertirlo fácilmente con Double.parseDouble().
             */
            String mensaje = String.valueOf(numero);

            byte[] bufferSalida = mensaje.getBytes();

            DatagramPacket paqueteSalida = new DatagramPacket(
                    bufferSalida,
                    bufferSalida.length,
                    direccionServidor,
                    PUERTO_SERVIDOR
            );

            socket.send(paqueteSalida);

            System.out.println("Petición enviada al servidor.");
            System.out.println("Puerto local asignado al cliente: " + socket.getLocalPort());

            byte[] bufferEntrada = new byte[1024];

            DatagramPacket paqueteEntrada = new DatagramPacket(
                    bufferEntrada,
                    bufferEntrada.length
            );

            socket.receive(paqueteEntrada);

            String respuesta = new String(
                    paqueteEntrada.getData(),
                    0,
                    paqueteEntrada.getLength()
            );

            System.out.println("Respuesta recibida del servidor:");
            System.out.println(respuesta);

        } catch (UnknownHostException e) {
            System.out.println("No se ha podido resolver la dirección del servidor: " + e.getMessage());
        } catch (SocketException e) {
            System.out.println("Error creando el socket UDP del cliente: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de entrada/salida en el cliente: " + e.getMessage());
        }
    }
}