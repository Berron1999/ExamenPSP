package EchoServerExamenSHA512;

import java.io.*;
import java.net.Socket;

/*
 * EchoStopper con verificación SHA-512.
 * Envía "." + hash. Recibe datos de uso + hash y verifica la integridad.
 */
public class EchoStopper {

    public static void main(String[] args) {
        try (
                Socket         socket  = new Socket(EchoClient.HOST, EchoClient.PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("[Stopper SHA512] Enviando señal de parada al servidor...");

            // Enviar "." con su hash
            salida.println(HashSHA512.empaquetar("."));

            // Recibir datos de uso + hash y verificarlo
            String respuesta = entrada.readLine();
            String[] partes  = respuesta.split("\\" + HashSHA512.SEPARADOR, 2);
            String   datos   = partes[0];
            String   hashRx  = partes[1];

            if (HashSHA512.verificar(datos, hashRx)) {
                System.out.println("[Stopper SHA512] Datos de uso (verificados): " + datos);
            } else {
                System.out.println("[Stopper SHA512] ¡HASH inválido en respuesta!");
            }
            System.out.println("[Stopper SHA512] Servidor finalizado.");

        } catch (IOException e) {
            System.out.println("[Stopper SHA512] Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Stopper SHA512] Error de hash: " + e.getMessage());
        }
    }
}
