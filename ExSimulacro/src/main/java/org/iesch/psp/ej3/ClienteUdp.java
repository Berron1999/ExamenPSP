package org.iesch.psp.ej3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class ClienteUdp {
    public static final int PUERTO = 6000;
    private static final int TAMANIO_BUFFER = 8192;

    public static void main(String[] args) {
        String nombre = "Cliente";
        if (args.length > 0 && !args[0].trim().isEmpty()) {
            nombre = args[0].trim();
        }

        try (DatagramSocket socket = new DatagramSocket()) {
            // Envia el saludo al servidor.
            byte[] saludo = nombre.getBytes(StandardCharsets.UTF_8);
            DatagramPacket paquete = new DatagramPacket(
                    saludo,
                    saludo.length,
                    InetAddress.getByName("localhost"),
                    PUERTO);
            socket.send(paquete);

            // Espera la respuesta del servidor.
            byte[] buffer = new byte[TAMANIO_BUFFER];
            DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
            socket.receive(respuesta);

            DatosCliente datos = deserializar(respuesta.getData(), respuesta.getLength());
            System.out.println("Hola " + datos.getNombreCliente() + ", numero recibido: " + datos.getNumero());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error en cliente UDP: " + e.getMessage());
        }
    }

    private static DatosCliente deserializar(byte[] bytes, int longitud) throws IOException, ClassNotFoundException {
        try (ObjectInputStream entrada = new ObjectInputStream(new ByteArrayInputStream(bytes, 0, longitud))) {
            return (DatosCliente) entrada.readObject();
        }
    }
}
