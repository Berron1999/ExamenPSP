package EchoServerExamenHMACSHA256;

import java.io.*;
import java.net.Socket;

// Hilo del servidor: recibe "mensaje|hmac", verifica autenticidad+integridad
// con HMAC-SHA256, hace eco y devuelve "mensaje|hmac" recalculado.
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

                int sep = linea.lastIndexOf(HMACSHA256.SEP);
                if (sep < 0) {
                    System.out.println("[Servidor] Línea sin separador, ignorada.");
                    continue;
                }
                String mensaje     = linea.substring(0, sep);
                String macRecibido = linea.substring(sep + 1);

                // Si el MAC no coincide → mensaje manipulado o atacante sin clave
                if (!HMACSHA256.verificar(mensaje, macRecibido)) {
                    System.out.println("[Servidor] HMAC INVÁLIDO para: " + mensaje);
                    continue;
                }

                if (mensaje.equals(".")) {
                    System.out.println("[Servidor] Orden de parada recibida.");
                    salida.println(HMACSHA256.empaquetar(echoData.toString()));
                    System.exit(0);
                }

                echoData.addMensaje(mensaje);
                salida.println(HMACSHA256.empaquetar(mensaje));
                System.out.println("[Servidor] Eco enviado para: " + mensaje);
            }

        } catch (Exception e) {
            System.out.println("[Servidor] Error con cliente: " + e.getMessage());
        }
    }
}
