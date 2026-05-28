package org.iesch.psp.ej3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ServidorUdp {
    public static final int PUERTO = 6000;
    private static final int TAMANIO_BUFFER = 8192;
    private static final int TIEMPO_CIERRE_MS = 60_000;
    private static final int TIMEOUT_RECEPCION_MS = 5_000;

    public static void main(String[] args) {
        System.out.println("Servidor UDP iniciado en puerto " + PUERTO);
        long fin = System.currentTimeMillis() + TIEMPO_CIERRE_MS;
        Random random = new Random();

        try (DatagramSocket socket = new DatagramSocket(PUERTO)) {
            socket.setSoTimeout(TIMEOUT_RECEPCION_MS);
            while (System.currentTimeMillis() < fin) {
                try {
                    // Espera el saludo del cliente.
                    byte[] buffer = new byte[TAMANIO_BUFFER];
                    DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                    socket.receive(paquete);

                    String saludo = new String(paquete.getData(), 0, paquete.getLength(), StandardCharsets.UTF_8);
                    System.out.println("Recibido: " + saludo);

                    // Envia un mensaje personalizado con numero aleatorio.
                    int numero = random.nextInt(101);
                    DatosCliente datos = new DatosCliente(saludo, numero);
                    byte[] respuestaBytes = serializar(datos);
                    DatagramPacket paqueteRespuesta = new DatagramPacket(
                            respuestaBytes,
                            respuestaBytes.length,
                            paquete.getAddress(),
                            paquete.getPort());
                    socket.send(paqueteRespuesta);
                } catch (SocketTimeoutException e) {
                    // Permite comprobar el tiempo de cierre.
                }
            }
        } catch (IOException e) {
            System.out.println("Error en servidor UDP: " + e.getMessage());
        }

        System.out.println("Servidor UDP finalizado por timeout.");
    }

    private static byte[] serializar(DatosCliente datos) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream salida = new ObjectOutputStream(bytes)) {
            salida.writeObject(datos);
            salida.flush();
            return bytes.toByteArray();
        }
    }
}
