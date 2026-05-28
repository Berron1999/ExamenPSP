package org.iesch.psp.UDP.UDPSerializar;

import org.iesch.psp.UDP.UDPSerializar.DatosCliente;

import java.io.*;
import java.net.*;

public class ClienteUDP {
    public static void main(String[] args) {
        String host = "localhost";
        int puertoServidor = 6000;

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress ipServidor = InetAddress.getByName(host);

            // 1. Enviar el saludo al servidor
            String saludo = "¡Hola Servidor! ¿Qué tal?";
            byte[] bufferSaludo = saludo.getBytes();
            DatagramPacket paqueteSaludo = new DatagramPacket(bufferSaludo, bufferSaludo.length, ipServidor, puertoServidor);
            socket.send(paqueteSaludo);
            System.out.println("Saludo enviado.");

            // 2. Recibir el mensaje personalizado del servidor
            byte[] bufferRespuestaMsg = new byte[1024];
            DatagramPacket paqueteRespuestaMsg = new DatagramPacket(bufferRespuestaMsg, bufferRespuestaMsg.length);
            socket.receive(paqueteRespuestaMsg);

            String respuestaTexto = new String(paqueteRespuestaMsg.getData(), 0, paqueteRespuestaMsg.getLength());
            System.out.println("Respuesta del servidor: " + respuestaTexto);

            // 3. Recibir el paquete que contiene el objeto serializado
            byte[] bufferObjeto = new byte[1024];
            DatagramPacket paqueteObjeto = new DatagramPacket(bufferObjeto, bufferObjeto.length);
            socket.receive(paqueteObjeto);

            // 4. Deserializar el objeto recibido
            ByteArrayInputStream bais = new ByteArrayInputStream(paqueteObjeto.getData(), 0, paqueteObjeto.getLength());
            ObjectInputStream ois = new ObjectInputStream(bais);
            DatosCliente datosRecibidos = (DatosCliente) ois.readObject();
            ois.close();

            // ✅ CORRECCIÓN: Llamar a getNombreCliente() en lugar del método inexistente
            System.out.println("Objeto recibido con éxito:");
            System.out.println("- Nombre: " + datosRecibidos.getNombreCliente());
            System.out.println("- Número: " + datosRecibidos.getNumero());

        } catch (UnknownHostException e) {
            System.err.println("No se encuentra el host: " + e.getMessage());
        } catch (SocketException e) {
            System.err.println("Error de Socket en el cliente: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de entrada/salida: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error al deserializar: Clase no encontrada -> " + e.getMessage());
        }
    }
}