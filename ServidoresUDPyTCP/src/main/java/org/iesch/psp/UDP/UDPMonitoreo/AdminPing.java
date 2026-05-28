package org.iesch.psp.UDP.UDPMonitoreo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class AdminPing {
    public static void main(String[] args) {
        String host = "localhost";
        int puertoServidor = 8000;

        // 1. Abrimos un socket UDP (sin puerto fijo porque somos el cliente)
        try (DatagramSocket socket = new DatagramSocket()) {

            System.out.println("Enviando comando STATUS al servidor...");
            InetAddress ipServidor = InetAddress.getByName(host);

            // 2. Preparamos el paquete de ENVÍO (De texto a Bytes)
            String mensaje = "STATUS";
            byte[] bufferEnvio = mensaje.getBytes();
            DatagramPacket paqueteEnvio = new DatagramPacket(
                    bufferEnvio, bufferEnvio.length, ipServidor, puertoServidor
            );

            // 3. Lanzamos el paquete
            socket.send(paqueteEnvio);

            // 4. Preparamos el paquete de RECEPCIÓN (Una caja vacía para la respuesta)
            byte[] bufferRecepcion = new byte[1024];
            DatagramPacket paqueteRecepcion = new DatagramPacket(bufferRecepcion, bufferRecepcion.length);

            // 5. Nos quedamos esperando a que el servidor nos responda
            socket.receive(paqueteRecepcion);

            // 6. Traducimos la respuesta (De Bytes a Texto) y la imprimimos
            String respuesta = new String(paqueteRecepcion.getData(), 0, paqueteRecepcion.getLength());
            System.out.println("El servidor responde: " + respuesta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}