package EchoServerExamenECDH;

import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;

// El Stopper también hace su propio handshake ECDH al conectar,
// luego envía "." cifrado con la clave de sesión derivada.
public class EchoStopper {

    public static void main(String[] args) {
        try (
                Socket         socket  = new Socket(EchoClient.HOST, EchoClient.PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // === HANDSHAKE ECDH ===
            KeyPair miPar = ECDHHelper.generarParejaEC();
            salida.println(ECDHHelper.codificarPublica(miPar.getPublic()));
            PublicKey publicaServidor = ECDHHelper.decodificarPublica(entrada.readLine());
            byte[] aesKey = ECDHHelper.derivarClaveAES(miPar.getPrivate(), publicaServidor);

            System.out.println("[EchoStopper] Handshake ECDH completado, enviando parada...");

            salida.println(ECDHHelper.cifrar(".", aesKey));

            String datosUso = ECDHHelper.descifrar(entrada.readLine(), aesKey);
            System.out.println("[EchoStopper] Datos de uso: " + datosUso);
            System.out.println("[EchoStopper] Correcto: " +
                    datosUso.contains("Mensajes procesados: 10000"));

        } catch (Exception e) {
            System.out.println("[EchoStopper] Error: " + e.getMessage());
        }
    }
}
