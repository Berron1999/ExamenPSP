package ClienteServidorFicheros;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    static final String HOST  = "localhost";
    static final int    PUERTO = 5000;

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        try (
                Socket socket         = new Socket(HOST, PUERTO);
                PrintWriter salida    = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Conectado al servidor.");

            boolean ejecutando = true;
            while (ejecutando) {
                // Menú principal
                System.out.println("\n1. Listar ficheros");
                System.out.println("2. Mostrar fichero");
                System.out.println("3. Salir");
                System.out.print("Elige una opción: ");
                String opcion = teclado.nextLine().trim();

                switch (opcion) {
                    case "1":
                        // Enviamos petición de listado al servidor
                        salida.println("LISTAR");
                        System.out.println("\n--- Ficheros disponibles ---");
                        recibirHastaFin(entrada);
                        break;

                    case "2":
                        System.out.print("Nombre del fichero: ");
                        String nombre = teclado.nextLine().trim();
                        // Enviamos petición con el nombre del fichero
                        salida.println("MOSTRAR:" + nombre);
                        System.out.println("\n--- Contenido de " + nombre + " ---");
                        recibirHastaFin(entrada);
                        break;

                    case "3":
                        salida.println("SALIR");
                        ejecutando = false;
                        System.out.println("Desconectando...");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    // Lee líneas del servidor hasta recibir la señal "FIN"
    private static void recibirHastaFin(BufferedReader entrada) throws IOException {
        String linea;
        while ((linea = entrada.readLine()) != null && !linea.equals("FIN")) {
            System.out.println(linea);
        }
    }
}