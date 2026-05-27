package EchoServerExamenHibridoAESRSA;

import java.io.*;
import java.net.Socket;

// Hilo del servidor: recibe mensaje cifrado en formato híbrido AES+RSA,
// lo descifra, hace eco y devuelve la respuesta también híbrida.
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

                // Descifrado híbrido: RSA descifra el sobre, luego AES descifra el mensaje
                String mensajeClaro = CifradoHibrido.descifrar(mensajeCifrado);

                if (mensajeClaro.equals(".")) {
                    System.out.println("[Servidor] Orden de parada recibida.");
                    salida.println(CifradoHibrido.cifrar(echoData.toString()));
                    System.exit(0);
                }

                echoData.addMensaje(mensajeClaro);

                // Eco cifrado de vuelta (clave AES nueva por respuesta)
                salida.println(CifradoHibrido.cifrar(mensajeClaro));
                System.out.println("[Servidor] Eco híbrido enviado para: " + mensajeClaro);
            }

        } catch (Exception e) {
            System.out.println("[Servidor] Error con cliente: " + e.getMessage());
        }
    }
}
