package ClienteServidorFicheros;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    static final int PUERTO = 5000;
    // Directorio de trabajo donde el servidor buscará los ficheros
    static final String DIRECTORIO = "C:\\servidor_ficheros\\";

    public static void main(String[] args) {
        System.out.println("Servidor iniciado en puerto " + PUERTO);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            // Bucle infinito: acepta clientes continuamente
            while (true) {
                // accept() bloquea hasta que llega un cliente
                Socket socketCliente = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + socketCliente.getInetAddress());

                // Creamos un hilo para atender a este cliente y no bloquear el bucle
                new Thread(new HiloCliente(socketCliente)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}