package EchoServerExamen;

import java.io.*;
import java.net.Socket;

// Hilo del servidor: atiende a UN cliente
// Recibe mensajes, devuelve eco y para si recibe "."
public class EchoServerThread extends Thread {

    private Socket   socket;
    private EchoData echoData;

    public EchoServerThread(Socket socket, EchoData echoData) {
        this.socket   = socket;
        this.echoData = echoData;
    }

    public void run() {
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {

                // "." → señal de parada del servidor
                if (mensaje.equals(".")) {
                    System.out.println("[Servidor] Orden de parada recibida.");
                    // Respondemos con los datos de uso antes de cerrar
                    salida.println(echoData.toString());
                    System.exit(0); // cierra el servidor
                }

                // Eco normal → actualizamos EchoServerExamen.EchoData y devolvemos el mensaje
                echoData.addMensaje(mensaje); // synchronized internamente
                salida.println(mensaje);
                System.out.println("[Servidor] Eco: " + mensaje);
            }

        } catch (IOException e) {
            System.out.println("[Servidor] Error con cliente: " + e.getMessage());
        }
    }
}