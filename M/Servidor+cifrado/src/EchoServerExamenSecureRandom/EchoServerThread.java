package EchoServerExamenSecureRandom;

import java.io.*;
import java.net.Socket;

/*
 * Hilo del servidor: descifra cada trama recibida y cifra cada respuesta
 * con un IV NUEVO generado con SecureRandom.
 *
 * Trama por el socket: "IV_base64|cifrado_base64"
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
            String trama;
            while ((trama = entrada.readLine()) != null) {

                // Descifrar (CifradorSecureRandom separa IV y cifrado internamente)
                String mensaje = CifradorSecureRandom.descifrar(trama);

                // Señal de parada
                if (mensaje.equals(".")) {
                    System.out.println("[Servidor SecureRandom] Orden de parada recibida.");
                    // Cifrar respuesta con un IV nuevo
                    salida.println(CifradorSecureRandom.cifrar(echoData.toString()));
                    System.exit(0);
                }

                // Eco normal
                echoData.addMensaje(mensaje);
                salida.println(CifradorSecureRandom.cifrar(mensaje));
                System.out.println("[Servidor SecureRandom] Eco (claro): " + mensaje);
            }

        } catch (IOException e) {
            System.out.println("[Servidor SecureRandom] Error con cliente: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Servidor SecureRandom] Error de cifrado: " + e.getMessage());
        }
    }
}
