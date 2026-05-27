package EchoServerExamenCifradoSHA512;

import java.io.*;
import java.net.Socket;

// Envía la señal de parada "." con su hash SHA-512 y muestra los datos de uso
public class EchoStopper {

    public static void main(String[] args) {
        try (
                Socket         socket  = new Socket(EchoClient.HOST, EchoClient.PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("[EchoStopper] Enviando señal de parada con hash SHA-512...");

            // Enviamos "." con su hash → el servidor verifica integridad y para
            salida.println(HashSHA512.empaquetar("."));

            // Recibimos "stats|hash" y verificamos
            String respuesta = entrada.readLine();
            int sep = respuesta.lastIndexOf(HashSHA512.SEP);
            String datosUso     = respuesta.substring(0, sep);
            String hashRecibido = respuesta.substring(sep + 1);

            System.out.println("[EchoStopper] Datos de uso: " + datosUso);
            System.out.println("[EchoStopper] Integridad: " + HashSHA512.verificar(datosUso, hashRecibido));
            System.out.println("[EchoStopper] Correcto: " +
                    datosUso.contains("Mensajes procesados: 10000"));

        } catch (Exception e) {
            System.out.println("[EchoStopper] Error: " + e.getMessage());
        }
    }
}
