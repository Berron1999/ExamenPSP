package EchoServerExamenSecureRandom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * EchoServer con cifrado AES/CBC + IV generado con SecureRandom
 * (equivalente Java a RNGCryptoServiceProvider de .NET).
 *
 * Para cada mensaje viaja un IV aleatorio distinto, lo que es BUENA
 * PRÁCTICA criptográfica: dos mensajes idénticos producen tramas
 * cifradas distintas, evitando filtración de información.
 */
public class EchoServer {

    static final int      PUERTO   = 5008;
    static final EchoData echoData = new EchoData();

    public static void main(String[] args) {
        System.out.println("[Servidor SecureRandom] Iniciado en puerto " + PUERTO);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("[Servidor SecureRandom] Cliente conectado: " + socketCliente.getInetAddress());

                EchoServerThread hilo = new EchoServerThread(socketCliente, echoData);
                hilo.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
