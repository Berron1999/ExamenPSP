package ServidorDeTurnos;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTurnos {

    static final String HOST   = "localhost";
    static final int    PUERTO = 5003;

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.print("Introduce tu nombre: ");
        String nombre = teclado.nextLine().trim();

        try (
                Socket         socket  = new Socket(HOST, PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Enviamos nuestro nombre al servidor
            salida.println(nombre);

            // Leemos todos los mensajes que el servidor nos vaya enviando
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                System.out.println("[Servidor] " + mensaje);
            }

        } catch (IOException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}