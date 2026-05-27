package EchoServerExamenFirmaECDSA;

import java.io.*;
import java.net.Socket;

// Envía la señal de parada "." firmada con ECDSA y muestra los datos de uso
public class EchoStopper {

    public static void main(String[] args) {
        try (
                Socket         socket  = new Socket(EchoClient.HOST, EchoClient.PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("[EchoStopper] Enviando señal de parada firmada con ECDSA...");

            salida.println(FirmaECDSA.empaquetar("."));

            String respuesta = entrada.readLine();
            int sep = respuesta.lastIndexOf(FirmaECDSA.SEP);
            String datosUso      = respuesta.substring(0, sep);
            String firmaRecibida = respuesta.substring(sep + 1);

            System.out.println("[EchoStopper] Datos de uso: " + datosUso);
            System.out.println("[EchoStopper] Firma ECDSA válida: " + FirmaECDSA.verificar(datosUso, firmaRecibida));
            System.out.println("[EchoStopper] Correcto: " +
                    datosUso.contains("Mensajes procesados: 10000"));

        } catch (Exception e) {
            System.out.println("[EchoStopper] Error: " + e.getMessage());
        }
    }
}
