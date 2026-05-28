package EchoServerExamenRSA;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;

/*
 * EchoServer con cifrado RSA (asimétrico).
 *
 * Al arrancar el servidor:
 *   1. Genera UNA VEZ su par de claves RSA (público + privado).
 *   2. La clave PÚBLICA se compartirá con cada cliente que conecte.
 *   3. La clave PRIVADA NUNCA sale del servidor: la usa para descifrar
 *      lo que le envían los clientes.
 *
 * El par del servidor es ÚNICO y se reparte entre todos los hilos -> static.
 */
public class EchoServer {

    static final int      PUERTO   = 5008;
    static final EchoData echoData = new EchoData();

    public static void main(String[] args) throws Exception {
        System.out.println("[Servidor RSA] Generando par de claves RSA " + CifradorRSA.TAMANO_CLAVE + " bits...");
        KeyPair parServidor = CifradorRSA.generarParClaves();
        System.out.println("[Servidor RSA] Claves generadas. Iniciando en puerto " + PUERTO);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("[Servidor RSA] Cliente conectado: " + socketCliente.getInetAddress());

                // Cada hilo recibe el par del servidor (lo usará para descifrar).
                EchoServerThread hilo = new EchoServerThread(socketCliente, echoData, parServidor);
                hilo.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
