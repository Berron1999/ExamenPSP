package EchoServer;

import EchoServer.EchoServidor;

import java.io.*;
import java.net.Socket;

// Hilo del servidor que atiende a UN cliente: recibe mensaje y devuelve eco
public class HiloAtencionCliente implements Runnable {

    private Socket socket;

    public HiloAtencionCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                // Incrementamos el contador compartido de forma segura (synchronized)
                EchoServidor.sumarRecibido();
                System.out.println("[Servidor] Recibido: " + mensaje + " → devolviendo eco.");
                // Echo: devolvemos exactamente el mismo mensaje al cliente
                salida.println(mensaje);
            }
        } catch (IOException e) {
            System.out.println("[Servidor] Error con cliente: " + e.getMessage());
        }
    }
}