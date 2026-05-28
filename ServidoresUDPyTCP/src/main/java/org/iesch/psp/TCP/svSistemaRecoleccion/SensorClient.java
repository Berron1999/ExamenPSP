package org.iesch.psp.TCP.svSistemaRecoleccion;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// Implementamos Runnable para poder lanzarlo como un hilo
public class SensorClient implements Runnable {
    private int idSensor;
    private int numLecturas;

    // Constructor para decirle quién es y cuántas lecturas debe enviar
    public SensorClient(int idSensor, int numLecturas) {
        this.idSensor = idSensor;
        this.numLecturas = numLecturas;
    }

    @Override
    public void run() {
        String host = "localhost";
        int puerto = 5000;

        try (Socket socket = new Socket(host, puerto);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner in = new Scanner(socket.getInputStream())) {

            // Bucle para enviar las lecturas
            for (int i = 0; i < numLecturas; i++) {
                // Inventamos una temperatura, por ejemplo 22.5
                out.println("22.5");

                // Leemos el "OK" del servidor
                if (in.hasNextLine()) {
                    in.nextLine();
                }
            }

            // Cuando termina de enviar, avisa de que ha acabado
            out.println("FIN");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}