package EchoServerExamenRSA;

import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;

/*
 * EchoStopper para la versión RSA.
 *
 * Mismo handshake que EchoClient:
 *   1) Recibe clave pública del servidor.
 *   2) Genera su propio par y envía su clave pública al servidor.
 *
 * Después envía "." cifrado con la pública del servidor, y recibe los
 * datos de uso cifrados con su propia clave pública (que descifra con
 * su privada).
 */
public class EchoStopper {

    public static void main(String[] args) {
        try (
                Socket         socket  = new Socket(EchoClient.HOST, EchoClient.PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Generar par propio del stopper
            KeyPair parStopper = CifradorRSA.generarParClaves();

            // Handshake
            String servPubBase64 = entrada.readLine();
            PublicKey clavePublicaServidor = CifradorRSA.importarClavePublica(servPubBase64);
            salida.println(CifradorRSA.exportarClavePublica(parStopper.getPublic()));

            System.out.println("[Stopper RSA] Enviando señal de parada al servidor...");

            // Enviar "." cifrado con pública del servidor
            salida.println(CifradorRSA.cifrar(".", clavePublicaServidor));

            // Recibir datos cifrados con nuestra pública -> descifrar con nuestra privada
            String datosCifrados = entrada.readLine();
            String datosUso      = CifradorRSA.descifrar(datosCifrados, parStopper.getPrivate());

            System.out.println("[Stopper RSA] Datos de uso: " + datosUso);
            System.out.println("[Stopper RSA] Servidor finalizado.");

        } catch (IOException e) {
            System.out.println("[Stopper RSA] Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Stopper RSA] Error de cifrado: " + e.getMessage());
        }
    }
}
