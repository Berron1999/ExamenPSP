package org.example.clienteServidorUdp;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Servidor UDP que escucha peticiones en el puerto 7000.
 *
 * El servidor recibe un mensaje del cliente solicitando la hora actual,
 * responde con la hora del sistema y el puerto de origen del cliente.
 *
 * Además, por cada petición recibida crea un objeto SesionCliente.
 *
 * Si pasan 90 segundos sin recibir ninguna petición, el servidor finaliza.
 */
public class ServidorUDP
{

    private static final int PUERTO_SERVIDOR = 7000;
    private static final int TIMEOUT_MS = 90000;

    public static void main(String[] args) {

        Random random = new Random();

        /*
         * Creamos el socket UDP del servidor asociado al puerto 7000.
         * Al estar dentro de try-with-resources, se cerrará automáticamente.
         */
        try (DatagramSocket socket = new DatagramSocket(PUERTO_SERVIDOR))
        {

            /*
             * Indicamos que receive() esperará como máximo 90 segundos.
             * Si no llega ningún paquete en ese tiempo, lanzará SocketTimeoutException.
             */
            socket.setSoTimeout(TIMEOUT_MS);

            System.out.println("Servidor UDP iniciado en el puerto " + PUERTO_SERVIDOR);
            System.out.println("Esperando peticiones...");

            /*
             * El servidor se mantiene escuchando hasta que se produzca el timeout.
             */
            while (true)
            {

                try
                {
                    /*
                     * Creamos un buffer donde se almacenarán los datos recibidos.
                     */
                    byte[] bufferEntrada = new byte[1024];

                    /*
                     * Creamos el paquete donde se recibirá la petición del cliente.
                     */
                    DatagramPacket paqueteEntrada = new DatagramPacket(
                            bufferEntrada,
                            bufferEntrada.length
                    );

                    /*
                     * Esperamos a recibir un paquete.
                     * Esta línea se queda bloqueada hasta recibir datos
                     * o hasta que pasen 90 segundos.
                     */
                    socket.receive(paqueteEntrada);

                    /*
                     * Convertimos los bytes recibidos a String.
                     * Usamos getLength() para no leer basura del buffer.
                     */
                    String mensajeCliente = new String(
                            paqueteEntrada.getData(),
                            0,
                            paqueteEntrada.getLength()
                    );

                    /*
                     * Obtenemos la IP y el puerto de origen del cliente.
                     * El puerto lo asignó automáticamente el sistema operativo.
                     */
                    InetAddress ipCliente = paqueteEntrada.getAddress();
                    int puertoCliente = paqueteEntrada.getPort();

                    /*
                     * Obtenemos la fecha y hora exactas de la petición.
                     */
                    LocalDateTime fechaHoraActual = LocalDateTime.now();

                    /*
                     * Generamos un número aleatorio entre 1 y 50.
                     */
                    int numeroAleatorio = random.nextInt(50) + 1;

                    /*
                     * Creamos el objeto SesionCliente solicitado por el enunciado.
                     */
                    SesionCliente sesionCliente = new SesionCliente(
                            puertoCliente,
                            fechaHoraActual,
                            numeroAleatorio
                    );

                    /*
                     * Mostramos información por consola del servidor.
                     */
                    System.out.println("--------------------------------------");
                    System.out.println("Mensaje recibido: " + mensajeCliente);
                    System.out.println("IP cliente: " + ipCliente.getHostAddress());
                    System.out.println("Puerto cliente: " + puertoCliente);
                    System.out.println("Sesión creada: " + sesionCliente);

                    /*
                     * Creamos el mensaje de respuesta que recibirá el cliente.
                     */
                    String respuesta = "Hora actual del sistema: " + fechaHoraActual
                            + " | Puerto origen del cliente: " + puertoCliente;

                    byte[] bufferSalida = respuesta.getBytes();

                    /*
                     * Creamos el paquete de respuesta usando la IP y puerto del cliente.
                     */
                    DatagramPacket paqueteSalida = new DatagramPacket(
                            bufferSalida,
                            bufferSalida.length,
                            ipCliente,
                            puertoCliente
                    );

                    /*
                     * Enviamos la respuesta al cliente.
                     */
                    socket.send(paqueteSalida);

                    System.out.println("Respuesta enviada al cliente.");

                } catch (SocketTimeoutException e) {
                    /*
                     * Si pasan 90 segundos sin recibir peticiones,
                     * se entra aquí y cerramos el servidor.
                     */
                    System.out.println("Han pasado 90 segundos sin recibir peticiones.");
                    System.out.println("Cerrando servidor UDP...");
                    break;
                }
            }

        } catch (SocketException e) {
            System.out.println("Error creando el socket UDP: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de entrada/salida en el servidor: " + e.getMessage());
        }
    }
}
