package EchoServerExamenCifradoAES;

import EchoServerExamenCifradoAES.CifradoAES;
import EchoServerExamenCifradoAES.EchoClient;

import java.io.*;
import java.net.Socket;

// Envía la señal de parada "." cifrada al servidor y muestra los datos de uso
public class EchoStopper {

    public static void main(String[] args) {
        try (
                Socket         socket  = new Socket(EchoClient.HOST, EchoClient.PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("[EchoStopper] Enviando señal de parada cifrada...");

            // Ciframos el "." y lo enviamos → el servidor lo descifrará y reconocerá la parada
            salida.println(CifradoAES.cifrar("."));

            // Recibimos los datos de uso cifrados y los desciframos
            String datosUso = CifradoAES.descifrar(entrada.readLine());
            System.out.println("[EchoStopper] Datos de uso: " + datosUso);

            // Verificamos que se procesaron exactamente 10000 mensajes
            System.out.println("[EchoStopper] Correcto: " +
                    datosUso.contains("Mensajes procesados: 10000"));

        } catch (Exception e) {
            System.out.println("[EchoStopper] Error: " + e.getMessage());
        }
    }
}