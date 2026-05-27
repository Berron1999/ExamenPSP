package ChatMultiusuario;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClienteChat {

    static final String HOST   = "localhost";
    static final int    PUERTO = 5004;

    public static void main(String[] args) throws InterruptedException {
        Scanner teclado = new Scanner(System.in);
        System.out.print("Introduce tu nick: ");
        String nick = teclado.nextLine().trim();

        try (
                Socket         socket  = new Socket(HOST, PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Enviamos el nick como primer mensaje
            salida.println(nick);
            System.out.println("Conectado al chat. Escribe SALIR para salir.\n");

            // Hilo receptor: escucha mensajes del servidor en segundo plano
            Thread hiloReceptor = new Thread(() -> {
                try {
                    String mensajeRecibido;
                    while ((mensajeRecibido = entrada.readLine()) != null) {
                        System.out.println(mensajeRecibido);
                    }
                } catch (IOException e) {
                    System.out.println("Conexión cerrada.");
                }
            });
            // daemon=true → el hilo receptor muere cuando el main termina
            hiloReceptor.setDaemon(true);
            hiloReceptor.start();

            // Hilo principal: lee del teclado y envía mensajes al servidor
            String texto;
            while (!(texto = teclado.nextLine()).equalsIgnoreCase("SALIR")) {
                salida.println(texto);
            }

            // Avisamos al servidor de que salimos
            salida.println("SALIR");

        } catch (IOException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}