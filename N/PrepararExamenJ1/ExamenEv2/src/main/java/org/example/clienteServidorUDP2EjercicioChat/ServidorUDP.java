package org.example.clienteServidorUDP2EjercicioChat;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;

public class ServidorUDP {

    private static final int PUERTO_SERVIDOR = 7100;
    private static final int TIMEOUT_MS = 60000;

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket(PUERTO_SERVIDOR)) {

            socket.setSoTimeout(TIMEOUT_MS);

            System.out.println("Servidor UDP iniciado en el puerto " + PUERTO_SERVIDOR);
            System.out.println("Esperando peticiones...");

            while (true) {

                try {
                    byte[] bufferEntrada = new byte[1024];

                    DatagramPacket paqueteEntrada = new DatagramPacket(
                            bufferEntrada,
                            bufferEntrada.length
                    );

                    socket.receive(paqueteEntrada);

                    String mensajeCliente = new String(
                            paqueteEntrada.getData(),
                            0,
                            paqueteEntrada.getLength()
                    );

                    InetAddress ipCliente = paqueteEntrada.getAddress();
                    int puertoCliente = paqueteEntrada.getPort();

                    String respuesta;

                    try {
                        double numeroRecibido = Double.parseDouble(mensajeCliente);
                        double raizCuadrada = Math.sqrt(numeroRecibido);
                        LocalDateTime fechaHoraActual = LocalDateTime.now();

                        OperacionCliente operacionCliente = new OperacionCliente(
                                puertoCliente,
                                fechaHoraActual,
                                numeroRecibido,
                                raizCuadrada
                        );

                        System.out.println("--------------------------------------");
                        System.out.println("Número recibido: " + numeroRecibido);
                        System.out.println("IP cliente: " + ipCliente.getHostAddress());
                        System.out.println("Puerto cliente: " + puertoCliente);
                        System.out.println("Operación creada: " + operacionCliente);

                        respuesta = "Número recibido: " + numeroRecibido
                                + " | Raíz cuadrada: " + raizCuadrada
                                + " | Puerto origen del cliente: " + puertoCliente;

                    } catch (NumberFormatException e) {
                        respuesta = "Error: el dato recibido no es un número válido.";
                    }

                    byte[] bufferSalida = respuesta.getBytes();

                    DatagramPacket paqueteSalida = new DatagramPacket(
                            bufferSalida,
                            bufferSalida.length,
                            ipCliente,
                            puertoCliente
                    );

                    socket.send(paqueteSalida);

                    System.out.println("Respuesta enviada al cliente.");

                } catch (SocketTimeoutException e) {
                    System.out.println("Han pasado 60 segundos sin recibir peticiones.");
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