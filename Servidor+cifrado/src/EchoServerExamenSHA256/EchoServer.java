package EchoServerExamenSHA256;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * EchoServer con VERIFICACIÓN DE INTEGRIDAD por SHA-256.
 *
 * - Escucha en el puerto PUERTO.
 * - Acepta clientes en bucle infinito.
 * - Por cada cliente lanza un EchoServerThread (un hilo por cliente)
 *   para poder atender VARIOS CLIENTES SIMULTÁNEAMENTE.
 *
 * SHA-256 no cifra los mensajes (siguen viajando en claro por el socket),
 * pero permite detectar si han sido alterados durante el transporte.
 */
public class EchoServer {

    static final int      PUERTO   = 5008;
    static final EchoData echoData = new EchoData();

    public static void main(String[] args) {
        System.out.println("[Servidor SHA256] Iniciado en puerto " + PUERTO);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("[Servidor SHA256] Cliente conectado: " + socketCliente.getInetAddress());

                EchoServerThread hilo = new EchoServerThread(socketCliente, echoData);
                hilo.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
