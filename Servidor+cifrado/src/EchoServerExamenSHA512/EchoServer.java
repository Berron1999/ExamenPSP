package EchoServerExamenSHA512;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * EchoServer con VERIFICACIÓN DE INTEGRIDAD por SHA-512.
 *
 * Igual que la versión SHA-256, pero usando un hash más largo (512 bits).
 * SHA-512 NO cifra el mensaje (sigue viajando en claro), sólo comprueba
 * que no haya sido alterado en tránsito.
 */
public class EchoServer {

    static final int      PUERTO   = 5008;
    static final EchoData echoData = new EchoData();

    public static void main(String[] args) {
        System.out.println("[Servidor SHA512] Iniciado en puerto " + PUERTO);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("[Servidor SHA512] Cliente conectado: " + socketCliente.getInetAddress());

                EchoServerThread hilo = new EchoServerThread(socketCliente, echoData);
                hilo.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
