package EchoServerExamenAESPBKDF2;

import java.io.*;
import java.net.Socket;

// Hilo del servidor: descifra con AES (clave derivada por PBKDF2), hace eco, cifra y devuelve.
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

                String mensajeClaro = CifradoAESPBKDF2.descifrar(mensajeCifrado);

                if (mensajeClaro.equals(".")) {
                    System.out.println("[Servidor] Orden de parada recibida.");
                    salida.println(CifradoAESPBKDF2.cifrar(echoData.toString()));
                    System.exit(0);
                }

                echoData.addMensaje(mensajeClaro);
                salida.println(CifradoAESPBKDF2.cifrar(mensajeClaro));
                System.out.println("[Servidor] Eco enviado para: " + mensajeClaro);
            }

        } catch (Exception e) {
            System.out.println("[Servidor] Error con cliente: " + e.getMessage());
        }
    }
}
