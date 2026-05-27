package MonitorDeEstadoDeServidor;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClienteMonitor {

    static final String HOST   = "localhost";
    static final int    PUERTO = 5006;

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        try (
                Socket             socket = new Socket(HOST, PUERTO);
                ObjectOutputStream out    = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream  in     = new ObjectInputStream(socket.getInputStream())
        ) {
            System.out.println("Conectado al monitor del servidor.");

            boolean ejecutando = true;
            while (ejecutando) {
                System.out.println("\n1. Ver estado de los hilos");
                System.out.println("2. Parar un hilo");
                System.out.println("3. Salir");
                System.out.print("Opción: ");
                String opcion = teclado.nextLine().trim();

                switch (opcion) {
                    case "1":
                        out.reset();
                        out.writeObject("ESTADO");

                        // Recibimos la lista de objetos EstadoHilo serializados
                        List<EstadoHilo> estados = (List<EstadoHilo>) in.readObject();
                        System.out.println("\n--- Estado de los hilos ---");
                        estados.forEach(e -> System.out.println("  " + e));
                        break;

                    case "2":
                        System.out.print("Nombre del hilo a parar: ");
                        String nombre = teclado.nextLine().trim();
                        out.reset();
                        out.writeObject("PARAR:" + nombre);
                        System.out.println((String) in.readObject());
                        break;

                    case "3":
                        out.reset();
                        out.writeObject("SALIR");
                        ejecutando = false;
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}