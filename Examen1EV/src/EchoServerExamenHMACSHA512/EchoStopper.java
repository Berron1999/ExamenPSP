package EchoServerExamenHMACSHA512;

import java.io.*;
import java.net.Socket;

// Envía la señal de parada "." con su HMAC-SHA512 y muestra los datos de uso
public class EchoStopper {

    public static void main(String[] args) {
        try (
                Socket         socket  = new Socket(EchoClient.HOST, EchoClient.PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("[EchoStopper] Enviando señal de parada con HMAC-SHA512...");

            salida.println(HMACSHA512.empaquetar("."));

            String respuesta = entrada.readLine();
            int sep = respuesta.lastIndexOf(HMACSHA512.SEP);
            String datosUso    = respuesta.substring(0, sep);
            String macRecibido = respuesta.substring(sep + 1);

            System.out.println("[EchoStopper] Datos de uso: " + datosUso);
            System.out.println("[EchoStopper] Autenticidad: " + HMACSHA512.verificar(datosUso, macRecibido));
            System.out.println("[EchoStopper] Correcto: " +
                    datosUso.contains("Mensajes procesados: 10000"));

        } catch (Exception e) {
            System.out.println("[EchoStopper] Error: " + e.getMessage());
        }
    }
}
