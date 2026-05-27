package EchoServerExamenAESPBKDF2;

import java.io.*;
import java.net.Socket;

// Envía la señal de parada "." cifrada con AES (clave PBKDF2) y muestra los datos de uso
public class EchoStopper {

    public static void main(String[] args) {
        try (
                Socket         socket  = new Socket(EchoClient.HOST, EchoClient.PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("[EchoStopper] Enviando señal de parada cifrada con AES (PBKDF2)...");

            salida.println(CifradoAESPBKDF2.cifrar("."));

            String datosUso = CifradoAESPBKDF2.descifrar(entrada.readLine());
            System.out.println("[EchoStopper] Datos de uso: " + datosUso);
            System.out.println("[EchoStopper] Correcto: " +
                    datosUso.contains("Mensajes procesados: 10000"));

        } catch (Exception e) {
            System.out.println("[EchoStopper] Error: " + e.getMessage());
        }
    }
}
