package org.example.clienteServidorUdp;

import java.io.IOException;
import java.net.*;

/**
 * Cliente UDP que envía una petición al servidor solicitando la hora actual.
 *
 * El cliente no indica puerto propio, por lo que el sistema operativo
 * le asigna automáticamente un puerto libre.
 */
public class ClienteUDP {

    private static final String HOST_SERVIDOR = "localhost";
    private static final int PUERTO_SERVIDOR = 7000;

    public static void main(String[] args) {

        /*
         * Creamos un DatagramSocket sin indicar puerto.
         * De esta forma, el sistema operativo asigna automáticamente
         * un puerto libre al cliente.
         */
        try (DatagramSocket socket = new DatagramSocket()) {

            /*
             * Obtenemos la dirección del servidor.
             * Como cliente y servidor están en la misma máquina, usamos localhost.
             */
            InetAddress direccionServidor = InetAddress.getByName(HOST_SERVIDOR);

            /*
             * Mensaje que se enviará al servidor.
             */
            String mensaje = "Solicito la hora actual";

            byte[] bufferSalida = mensaje.getBytes();

            /*
             * Creamos el paquete UDP con:
             * - datos a enviar
             * - longitud de los datos
             * - dirección del servidor
             * - puerto del servidor
             */
            DatagramPacket paqueteSalida = new DatagramPacket(
                    bufferSalida,
                    bufferSalida.length,
                    direccionServidor,
                    PUERTO_SERVIDOR
            );

            /*
             * Enviamos la petición al servidor.
             */
            socket.send(paqueteSalida);

            System.out.println("Petición enviada al servidor.");
            System.out.println("Puerto local asignado al cliente: " + socket.getLocalPort());

            /*
             * Preparamos el paquete donde recibiremos la respuesta.
             */
            byte[] bufferEntrada = new byte[1024];

            DatagramPacket paqueteEntrada = new DatagramPacket(
                    bufferEntrada,
                    bufferEntrada.length
            );

            /*
             * Esperamos la respuesta del servidor.
             */
            socket.receive(paqueteEntrada);

            /*
             * Convertimos la respuesta recibida a String.
             */
            String respuesta = new String(
                    paqueteEntrada.getData(),
                    0,
                    paqueteEntrada.getLength()
            );

            /*
             * Mostramos la respuesta por pantalla.
             */
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
