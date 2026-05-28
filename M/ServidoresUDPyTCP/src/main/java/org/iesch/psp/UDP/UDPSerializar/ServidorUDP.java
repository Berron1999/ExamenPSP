package org.iesch.psp.UDP.UDPSerializar;

import java.io.*;
import java.net.*;
import java.util.Random;

public class ServidorUDP {
    public static void main(String[] args) {
        int puerto = 6000; // ✅ Servidor escucha en el puerto 6000
        boolean parar = false; // ✅ Variable parar definida y utilizada

        try (DatagramSocket socket = new DatagramSocket(puerto)) {

            // ✅ CORRECCIÓN: Implementar timeout de 1 minuto (60.000 ms)
            socket.setSoTimeout(60000);
            System.out.println("Servidor UDP iniciado. Esperando mensajes (Timeout: 1 min)...");

            // Bucle controlado por la variable parar
            while (!parar) {
                try {
                    // 1. Preparar y recibir el saludo del cliente
                    byte[] bufferRecepcion = new byte[1024];
                    DatagramPacket paqueteRecepcion = new DatagramPacket(bufferRecepcion, bufferRecepcion.length);

                    // El programa se pausa aquí. Si pasa 1 min sin recibir nada, lanza SocketTimeoutException
                    socket.receive(paqueteRecepcion);

                    // Procesar el mensaje recibido
                    String saludoRecibido = new String(paqueteRecepcion.getData(), 0, paqueteRecepcion.getLength());
                    System.out.println("Cliente dice: " + saludoRecibido);

                    // Obtener la IP y el puerto del cliente para poder responderle
                    InetAddress ipCliente = paqueteRecepcion.getAddress();
                    int puertoCliente = paqueteRecepcion.getPort();

                    // 2. Enviar respuesta personalizada
                    String respuesta = "¡Hola! He recibido tu saludo: '" + saludoRecibido + "'";
                    byte[] bufferRespuesta = respuesta.getBytes();
                    DatagramPacket paqueteRespuesta = new DatagramPacket(bufferRespuesta, bufferRespuesta.length, ipCliente, puertoCliente);
                    socket.send(paqueteRespuesta);

                    // 3. Generar número aleatorio y crear el objeto DatosCliente
                    Random random = new Random();
                    // ✅ CORRECCIÓN: random.nextInt(101) genera de 0 a 100 inclusive
                    int numeroAleatorio = random.nextInt(101);
                    DatosCliente datosCliente = new DatosCliente("ClienteUDP_" + puertoCliente, numeroAleatorio);

                    // 4. Serializar el objeto para poder enviarlo
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(datosCliente);
                    oos.close();

                    // 5. Enviar el objeto serializado (el array de bytes) al cliente
                    byte[] bufferObjeto = baos.toByteArray();
                    DatagramPacket paqueteObjeto = new DatagramPacket(bufferObjeto, bufferObjeto.length, ipCliente, puertoCliente);
                    socket.send(paqueteObjeto);
                    System.out.println("Objeto DatosCliente enviado al cliente.");

                } catch (SocketTimeoutException e) {
                    // ✅ CORRECCIÓN: El servidor se cierra si salta el timeout
                    System.out.println("\n[!] Han pasado 60 segundos sin actividad.");
                    System.out.println("Cerrando el servidor por inactividad...");
                    parar = true; // Esto rompe el bucle while

                } catch (IOException e) {
                    System.err.println("Error de E/S durante la comunicación: " + e.getMessage());
                }
            }
        } catch (SocketException e) {
            // ✅ Control de errores específico, no genérico
            System.err.println("Error al abrir el puerto UDP 6000: " + e.getMessage());
        }
    }
}