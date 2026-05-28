package org.iesch.psp.TCP.svSistemaRecoleccion;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// Implementar Runnable es obligatorio para que sea un hilo
public class SensorServerThread implements Runnable {
    private Socket socket;
    private SensorStats estadisticas;

    // El constructor recibe el enchufe (socket) de ese cliente en concreto
    public SensorServerThread(Socket socket, SensorStats estadisticas) {
        this.socket = socket;
        this.estadisticas = estadisticas;
    }

    @Override
    public void run() { // Aquí dentro va el trabajo duro
        try (Scanner in = new Scanner(socket.getInputStream());
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Mientras el cliente siga enviando cosas...
            while (in.hasNextLine()) {
                String mensaje = in.nextLine();

                if (mensaje.equals("FIN")) {
                    break; // Salimos del bucle y se corta la conexión

                } else if (mensaje.equals("STATS")) {
                    out.println(estadisticas.getEstadisticas());
                    break; // Cortamos la conexión del admin
                } else {
                    // Si no es FIN ni STATS, es una temperatura (ej: "22.5")
                    // La leemos y devolvemos OK
                    System.out.println("Recibido: " + mensaje);
                    double temp = Double.parseDouble(mensaje);
                    estadisticas.registrarLectura(temp); // <--- Guardamos el dato de forma segura
                    out.println("OK");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } // El socket se cierra solo al acabar el try
    }
}