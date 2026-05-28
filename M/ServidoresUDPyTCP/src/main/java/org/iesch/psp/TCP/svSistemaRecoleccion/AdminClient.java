package org.iesch.psp.TCP.svSistemaRecoleccion;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AdminClient {
    public static void main(String[] args) {
        String host = "localhost"; // Nos conectamos a nuestra propia máquina
        int puerto = 5000;         // El puerto del servidor (inventamos el 5000)

        // 1. Conectamos al servidor y abrimos los canales de entrada y salida.
        // Al usar "try()", Java cerrará el socket automáticamente al terminar.
        try (Socket socket = new Socket(host, puerto);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner in = new Scanner(socket.getInputStream())) {

            // 2. Le enviamos el mensaje al servidor
            System.out.println("Enviando petición de estadísticas...");
            out.println("STATS");

            // 3. Esperamos y leemos la respuesta del servidor
            if (in.hasNextLine()) {
                String respuesta = in.nextLine();
                System.out.println("Respuesta recibida: " + respuesta);
            }

        } catch (Exception e) {
            System.out.println("No se pudo conectar al servidor.");
            e.printStackTrace();
        }
    }
}