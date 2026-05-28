package EchoServerExamenRSA;

import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;

/*
 * Cliente RSA.
 *
 * - Cada cliente genera SU PROPIO par de claves al arrancar el hilo.
 *   Así el servidor puede cifrar las respuestas con la pública del cliente,
 *   y sólo este cliente (con su privada) puede descifrarlas.
 *
 * - HANDSHAKE:
 *     1) Recibe la clave pública del servidor.
 *     2) Envía su propia clave pública al servidor.
 *
 * - Después de eso, cada mensaje:
 *     * lo cifra con la pública del SERVIDOR antes de enviar.
 *     * recibe el eco cifrado con la pública del CLIENTE y lo descifra
 *       con su propia privada.
 */
public class EchoClient extends Thread {

    static final String HOST   = "localhost";
    static final int    PUERTO = 5008;

    private int idCliente;
    private int numMensajes;

    public EchoClient(int idCliente, int numMensajes) {
        this.idCliente   = idCliente;
        this.numMensajes = numMensajes;
    }

    public void run() {
        try (
                Socket         socket  = new Socket(HOST, PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Generar par propio del cliente (público + privado)
            KeyPair parCliente = CifradorRSA.generarParClaves();

            // ===== HANDSHAKE =====
            // 1) Leer clave pública del servidor
            String servPubBase64 = entrada.readLine();
            PublicKey clavePublicaServidor = CifradorRSA.importarClavePublica(servPubBase64);

            // 2) Enviar nuestra clave pública
            salida.println(CifradorRSA.exportarClavePublica(parCliente.getPublic()));

            // ===== BUCLE DE MENSAJES =====
            for (int i = 1; i <= numMensajes; i++) {
                String msg = "Cliente-" + idCliente + "-Msg-" + i;

                // Cifrar con la pública DEL SERVIDOR
                salida.println(CifradorRSA.cifrar(msg, clavePublicaServidor));

                // Recibir eco cifrado con NUESTRA pública -> descifrar con NUESTRA privada
                String ecoCifrado = entrada.readLine();
                String eco        = CifradorRSA.descifrar(ecoCifrado, parCliente.getPrivate());

                System.out.println("[Cliente-" + idCliente + "] Eco recibido: " + eco);
            }
        } catch (IOException e) {
            System.out.println("[Cliente-" + idCliente + "] Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Cliente-" + idCliente + "] Error de cifrado: " + e.getMessage());
        }
    }
}
