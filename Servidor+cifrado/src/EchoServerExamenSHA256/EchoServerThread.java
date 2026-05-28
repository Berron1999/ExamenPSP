package EchoServerExamenSHA256;

import java.io.*;
import java.net.Socket;

/*
 * Hilo del servidor: atiende a UN cliente.
 *
 * Flujo con SHA-256 (verificación de integridad):
 *   1. Recibe una línea con formato:  "mensaje|hashSHA256(mensaje)"
 *   2. Separa los dos trozos con split("|", 2).
 *   3. Recalcula el SHA-256 del mensaje y lo compara con el hash recibido.
 *      - Si NO coinciden -> el mensaje fue alterado -> se descarta.
 *      - Si coinciden    -> se procesa normalmente y se devuelve el eco
 *                            (también empaquetado con su hash).
 *   4. Si el mensaje es "." -> envía datos de uso + hash y cierra el servidor.
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

                // 1) Separar mensaje y hash. split con limit=2 por si el mensaje
                //    contuviera el separador (no es nuestro caso, pero es robusto).
                //    Nota: split usa regex, por eso "\\|" para escapar "|".
                String[] partes = linea.split("\\" + HashSHA256.SEPARADOR, 2);

                if (partes.length != 2) {
                    System.out.println("[Servidor SHA256] Trama mal formada, descartada.");
                    continue;
                }

                String mensaje      = partes[0];
                String hashRecibido = partes[1];

                // 2) Verificar integridad
                if (!HashSHA256.verificar(mensaje, hashRecibido)) {
                    System.out.println("[Servidor SHA256] ¡HASH INVÁLIDO! Mensaje alterado: " + mensaje);
                    continue; // no procesamos el mensaje
                }

                // 3) ¿Señal de parada?
                if (mensaje.equals(".")) {
                    System.out.println("[Servidor SHA256] Orden de parada recibida.");
                    // Empaquetamos la respuesta con su hash para que el cliente
                    // también pueda verificar integridad.
                    salida.println(HashSHA256.empaquetar(echoData.toString()));
                    System.exit(0);
                }

                // 4) Eco normal
                echoData.addMensaje(mensaje);
                salida.println(HashSHA256.empaquetar(mensaje));
                System.out.println("[Servidor SHA256] Eco verificado: " + mensaje);
            }

        } catch (IOException e) {
            System.out.println("[Servidor SHA256] Error con cliente: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Servidor SHA256] Error de hash: " + e.getMessage());
        }
    }
}
