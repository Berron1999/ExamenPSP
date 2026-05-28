package EchoServerExamenECDH;

import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;

// Hilo del servidor con HANDSHAKE ECDH:
// 1. Lee la pública del cliente (primera línea)
// 2. Genera su propia pareja EC y envía su pública
// 3. Deriva la clave AES de sesión combinando privada local + pública remota
// 4. A partir de ahí: comunicación normal cifrada con AES
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
            // === HANDSHAKE ECDH ===
            PublicKey publicaCliente = ECDHHelper.decodificarPublica(entrada.readLine());
            KeyPair   miPar          = ECDHHelper.generarParejaEC();
            salida.println(ECDHHelper.codificarPublica(miPar.getPublic()));
            byte[] aesKey = ECDHHelper.derivarClaveAES(miPar.getPrivate(), publicaCliente);
            System.out.println("[Servidor] Handshake ECDH completado, clave AES de sesión derivada.");

            // === Comunicación cifrada ===
            String mensajeCifrado;
            while ((mensajeCifrado = entrada.readLine()) != null) {

                String mensajeClaro = ECDHHelper.descifrar(mensajeCifrado, aesKey);

                if (mensajeClaro.equals(".")) {
                    System.out.println("[Servidor] Orden de parada recibida.");
                    salida.println(ECDHHelper.cifrar(echoData.toString(), aesKey));
                    System.exit(0);
                }

                echoData.addMensaje(mensajeClaro);
                salida.println(ECDHHelper.cifrar(mensajeClaro, aesKey));
                System.out.println("[Servidor] Eco enviado para: " + mensajeClaro);
            }

        } catch (Exception e) {
            System.out.println("[Servidor] Error con cliente: " + e.getMessage());
        }
    }
}
