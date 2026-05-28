package EchoServerExamenAES;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * EchoServer con cifrado AES.
 *
 * - Escucha en el puerto PUERTO.
 * - Acepta clientes en bucle infinito.
 * - Por cada cliente que conecta, lanza un EchoServerThread (un hilo por cliente)
 *   para poder atender VARIOS CLIENTES SIMULTÁNEAMENTE (ejercicio 2 del enunciado).
 *
 * El cifrado se aplica en cada mensaje a nivel de EchoServerThread.
 */
public class EchoServer {

    static final int      PUERTO   = 5008;
    // EchoData compartido entre todos los hilos -> debe ser thread-safe.
    static final EchoData echoData = new EchoData();

    public static void main(String[] args) {
        System.out.println("[Servidor AES] Iniciado en puerto " + PUERTO);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                // accept() es BLOQUEANTE: se queda esperando a que llegue un cliente.
                Socket socketCliente = serverSocket.accept();
                System.out.println("[Servidor AES] Cliente conectado: " + socketCliente.getInetAddress());

                // Un hilo por cliente -> permite concurrencia.
                EchoServerThread hilo = new EchoServerThread(socketCliente, echoData);
                hilo.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
