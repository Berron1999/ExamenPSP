package EchoServerExamenAES;

import java.io.*;
import java.net.Socket;

/*
 * Hilo del servidor: atiende a UN cliente.
 *
 * Flujo con cifrado AES:
 *   1. Recibe del cliente una línea de texto cifrada (Base64 de los bytes AES).
 *   2. La DESCIFRA con CifradorAES.descifrar() para obtener el mensaje en claro.
 *   3. Si es "." (señal de parada), responde con los datos de uso CIFRADOS y
 *      hace System.exit(0) para finalizar el servidor.
 *   4. Si no, actualiza el contador (EchoData) y devuelve el eco CIFRADO.
 *
 * IMPORTANTE: por el socket SIEMPRE viaja texto cifrado en Base64.
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
            String mensajeCifrado;
            while ((mensajeCifrado = entrada.readLine()) != null) {

                // 1) Descifrar lo recibido por el socket
                String mensaje = CifradorAES.descifrar(mensajeCifrado);

                // 2) ¿Es la señal de parada?
                if (mensaje.equals(".")) {
                    System.out.println("[Servidor AES] Orden de parada recibida.");
                    // Cifrar los datos de uso antes de devolverlos
                    salida.println(CifradorAES.cifrar(echoData.toString()));
                    System.exit(0); // cierra el servidor
                }

                // 3) Eco normal: actualizar contador y devolver el mensaje cifrado
                echoData.addMensaje(mensaje); // synchronized internamente
                salida.println(CifradorAES.cifrar(mensaje));
                System.out.println("[Servidor AES] Eco (claro): " + mensaje);
            }

        } catch (IOException e) {
            System.out.println("[Servidor AES] Error con cliente: " + e.getMessage());
        } catch (Exception e) {
            // Excepciones del cifrado (clave inválida, padding mal, etc.)
            System.out.println("[Servidor AES] Error de cifrado: " + e.getMessage());
        }
    }
}
