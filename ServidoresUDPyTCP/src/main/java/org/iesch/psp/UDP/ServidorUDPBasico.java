package org.iesch.psp.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServidorUDPBasico {
    public static void main(String[] args) {
        int puerto = 5000;

        // 1. Abrimos el buzón (socket) en el puerto
        try (DatagramSocket socket = new DatagramSocket(puerto)) {
            System.out.println("Servidor UDP esperando datagramas...");

            // 2. Preparamos el paquete para recibir datos
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // 3. El programa se detiene aquí hasta que llega un paquete
            socket.receive(packet);

            // Extraemos la información del paquete
            String mensaje = new String(packet.getData()).trim();
            System.out.println("Recibido: " + mensaje);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}