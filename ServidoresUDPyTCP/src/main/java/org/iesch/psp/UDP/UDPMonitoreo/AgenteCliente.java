package org.iesch.psp.UDP.UDPMonitoreo;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Random;

public class AgenteCliente implements Runnable {
    private String nombreEquipo;

    // Constructor
    public AgenteCliente(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    @Override
    public void run() {
        String host = "localhost";
        int puertoServidor = 8000;

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress ipServidor = InetAddress.getByName(host);
            Random random = new Random();

            // TRUCO DE EXAMEN: Timeout corto de medio segundo. 
            // Si el servidor no nos da una alerta en 500ms, asumimos que todo va bien y seguimos.
            socket.setSoTimeout(500);

            // Bucle de 100 envíos
            for (int i = 0; i < 100; i++) {
                int cpu = random.nextInt(100) + 1; // CPU aleatoria del 1 al 100
                int ram = random.nextInt(100) + 1; // RAM aleatoria del 1 al 100

                // 1. Creamos el objeto
                MetricaSistema metrica = new MetricaSistema(nombreEquipo, cpu, ram);

                // 2. Serializamos el objeto a bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(metrica);
                oos.close();

                // 3. Enviamos el paquete
                byte[] bufferEnvio = baos.toByteArray();
                DatagramPacket paqueteEnvio = new DatagramPacket(bufferEnvio, bufferEnvio.length, ipServidor, puertoServidor);
                socket.send(paqueteEnvio);

                // 4. Intentamos escuchar por si nos llega una alerta de CPU alta
                try {
                    byte[] bufferRecepcion = new byte[1024];
                    DatagramPacket paqueteRecepcion = new DatagramPacket(bufferRecepcion, bufferRecepcion.length);
                    socket.receive(paqueteRecepcion);

                    String alerta = new String(paqueteRecepcion.getData(), 0, paqueteRecepcion.getLength());
                    System.out.println("⚠️ " + nombreEquipo + " RECIBIÓ: " + alerta);

                } catch (SocketTimeoutException e) {
                    // No pasa nada. El timeout significa que la CPU era < 90 y el servidor no dijo nada.
                }

                // Pequeña pausa para no fundir el ordenador al lanzar los 30 hilos a la vez
                Thread.sleep(50);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}