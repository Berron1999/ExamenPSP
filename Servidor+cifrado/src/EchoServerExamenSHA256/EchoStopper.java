package EchoServerExamenSHA256;

import java.io.*;
import java.net.Socket;

/*
 * EchoStopper con verificación SHA-256.
 *
 * Envía "." empaquetado con su hash. El servidor responde con los datos
 * de uso (también empaquetados con su hash) antes de cerrarse.
 */
public class EchoStopper {

    public static void main(String[] args) {
        try (
                Socket         socket  = new Socket(EchoClient.HOST, EchoClient.PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("[Stopper SHA256] Enviando señal de parada al servidor...");

            // Enviar "." con su hash
            salida.println(HashSHA256.empaquetar("."));

            // Recibir datos de uso + hash y verificarlo
            String respuesta = entrada.readLine();
            String[] partes  = respuesta.split("\\" + HashSHA256.SEPARADOR, 2);
            String   datos   = partes[0];
            String   hashRx  = partes[1];

            if (HashSHA256.verificar(datos, hashRx)) {
                System.out.println("[Stopper SHA256] Datos de uso (verificados): " + datos);
            } else {
                System.out.println("[Stopper SHA256] ¡HASH inválido en respuesta!");
            }
            System.out.println("[Stopper SHA256] Servidor finalizado.");

        } catch (IOException e) {
            System.out.println("[Stopper SHA256] Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Stopper SHA256] Error de hash: " + e.getMessage());
        }
    }
}
