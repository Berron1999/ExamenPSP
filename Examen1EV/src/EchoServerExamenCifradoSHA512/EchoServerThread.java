package EchoServerExamenCifradoSHA512;

import java.io.*;
import java.net.Socket;

// Hilo del servidor: recibe "mensaje|hash", verifica integridad con SHA-512,
// hace eco y devuelve "mensaje|hash" recalculado.
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
            String linea;
            while ((linea = entrada.readLine()) != null) {

                // El último '|' separa mensaje y hash (lastIndexOf por robustez)
                int sep = linea.lastIndexOf(HashSHA512.SEP);
                if (sep < 0) {
                    System.out.println("[Servidor] Línea sin separador, ignorada: " + linea);
                    continue;
                }
                String mensaje      = linea.substring(0, sep);
                String hashRecibido = linea.substring(sep + 1);

                // Verificamos integridad antes de procesar nada
                if (!HashSHA512.verificar(mensaje, hashRecibido)) {
                    System.out.println("[Servidor] HASH INVÁLIDO para: " + mensaje);
                    continue;
                }

                // "." → señal de parada
                if (mensaje.equals(".")) {
                    System.out.println("[Servidor] Orden de parada recibida.");
                    salida.println(HashSHA512.empaquetar(echoData.toString()));
                    System.exit(0);
                }

                // Actualizamos EchoData (synchronized internamente)
                echoData.addMensaje(mensaje);

                // Devolvemos el eco con su hash recalculado por el servidor
                salida.println(HashSHA512.empaquetar(mensaje));
                System.out.println("[Servidor] Eco enviado para: " + mensaje);
            }

        } catch (Exception e) {
            System.out.println("[Servidor] Error con cliente: " + e.getMessage());
        }
    }
}
