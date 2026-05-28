package EchoServerExamenSecureRandom;

import java.io.*;
import java.net.Socket;

/*
 * EchoStopper: envía "." cifrado (con IV aleatorio) y descifra los
 * datos de uso devueltos por el servidor antes de cerrarse.
 */
public class EchoStopper {

    public static void main(String[] args) {
        try (
                Socket         socket  = new Socket(EchoClient.HOST, EchoClient.PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("[Stopper SecureRandom] Enviando señal de parada al servidor...");

            // Enviar "." cifrado con IV aleatorio
            salida.println(CifradorSecureRandom.cifrar("."));

            // Recibir trama cifrada con datos de uso y descifrarla
            String trama    = entrada.readLine();
            String datosUso = CifradorSecureRandom.descifrar(trama);

            System.out.println("[Stopper SecureRandom] Datos de uso: " + datosUso);
            System.out.println("[Stopper SecureRandom] Servidor finalizado.");

        } catch (IOException e) {
            System.out.println("[Stopper SecureRandom] Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Stopper SecureRandom] Error de cifrado: " + e.getMessage());
        }
    }
}
