package EchoServerExamen;

import java.io.*;
import java.net.Socket;

// Envía la señal de parada "." al servidor y muestra los datos de uso recibidos
public class EchoStopper {

    public static void main(String[] args) {
        try (
                Socket         socket  = new Socket(EchoClient.HOST, EchoClient.PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("[EchoServerExamen.EchoStopper] Enviando señal de parada al servidor...");

            // Enviamos el punto → señal de parada
            salida.println(".");

            // El servidor responde con los datos de uso antes de cerrar
            String datosUso = entrada.readLine();
            System.out.println("[EchoServerExamen.EchoStopper] Datos de uso: " + datosUso);
            System.out.println("[EchoServerExamen.EchoStopper] Servidor finalizado.");

        } catch (IOException e) {
            System.out.println("[EchoServerExamen.EchoStopper] Error: " + e.getMessage());
        }
    }
}