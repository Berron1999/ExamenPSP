package EchoServerExamenAES;

import java.io.*;
import java.net.Socket;

/*
 * EchoStopper -> ejercicio 1 del enunciado.
 *
 * Se conecta al servidor por TCP, envía la señal de parada ".".
 * Tras la parada, el servidor le devuelve los DATOS DE USO (mensajes y
 * caracteres procesados). Como todo el canal va cifrado con AES, este
 * cliente también cifra el "." y descifra la respuesta antes de imprimirla.
 */
public class EchoStopper {

    public static void main(String[] args) {
        try (
                Socket         socket  = new Socket(EchoClient.HOST, EchoClient.PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("[Stopper AES] Enviando señal de parada al servidor...");

            // Enviar el "." cifrado
            salida.println(CifradorAES.cifrar("."));

            // Recibir y descifrar los datos de uso
            String datosUsoCifrados = entrada.readLine();
            String datosUso         = CifradorAES.descifrar(datosUsoCifrados);

            System.out.println("[Stopper AES] Datos de uso: " + datosUso);
            System.out.println("[Stopper AES] Servidor finalizado.");

        } catch (IOException e) {
            System.out.println("[Stopper AES] Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Stopper AES] Error de cifrado: " + e.getMessage());
        }
    }
}
