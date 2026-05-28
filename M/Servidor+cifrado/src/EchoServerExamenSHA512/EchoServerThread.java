package EchoServerExamenSHA512;

import java.io.*;
import java.net.Socket;

/*
 * Hilo del servidor: atiende a UN cliente verificando integridad con SHA-512.
 *
 * Flujo:
 *   1. Recibe línea con formato "mensaje|hashSHA512".
 *   2. Separa con split, recalcula el hash y compara.
 *   3. Si el hash no coincide -> mensaje alterado -> se descarta.
 *   4. Si coincide -> procesa el mensaje (eco o parada) y devuelve respuesta
 *      también empaquetada con su hash.
 */
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

                // 1) Separar mensaje y hash recibido
                String[] partes = linea.split("\\" + HashSHA512.SEPARADOR, 2);

                if (partes.length != 2) {
                    System.out.println("[Servidor SHA512] Trama mal formada, descartada.");
                    continue;
                }

                String mensaje      = partes[0];
                String hashRecibido = partes[1];

                // 2) Verificar integridad
                if (!HashSHA512.verificar(mensaje, hashRecibido)) {
                    System.out.println("[Servidor SHA512] ¡HASH INVÁLIDO! Mensaje alterado: " + mensaje);
                    continue;
                }

                // 3) Señal de parada
                if (mensaje.equals(".")) {
                    System.out.println("[Servidor SHA512] Orden de parada recibida.");
                    salida.println(HashSHA512.empaquetar(echoData.toString()));
                    System.exit(0);
                }

                // 4) Eco normal
                echoData.addMensaje(mensaje);
                salida.println(HashSHA512.empaquetar(mensaje));
                System.out.println("[Servidor SHA512] Eco verificado: " + mensaje);
            }

        } catch (IOException e) {
            System.out.println("[Servidor SHA512] Error con cliente: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Servidor SHA512] Error de hash: " + e.getMessage());
        }
    }
}
