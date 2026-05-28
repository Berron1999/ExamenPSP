package EchoServerExamenRSA;

import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;

/*
 * Hilo del servidor: atiende a UN cliente con RSA.
 *
 * HANDSHAKE inicial (justo al conectar):
 *   1) Servidor envía SU clave PÚBLICA al cliente.
 *   2) Cliente envía SU clave PÚBLICA al servidor.
 *   -> A partir de aquí, cada lado conoce la pública del otro.
 *
 * CICLO de mensajes:
 *   - Cliente cifra con la pública del SERVIDOR -> servidor descifra con
 *     su propia clave privada.
 *   - Servidor cifra el eco con la pública del CLIENTE -> cliente descifra
 *     con su propia clave privada.
 */
public class EchoServerThread extends Thread {

    private Socket   socket;
    private EchoData echoData;
    private KeyPair  parServidor; // clave pública+privada del servidor

    public EchoServerThread(Socket socket, EchoData echoData, KeyPair parServidor) {
        this.socket      = socket;
        this.echoData    = echoData;
        this.parServidor = parServidor;
    }

    public void run() {
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // ===== HANDSHAKE =====
            // 1) Mandar al cliente nuestra clave pública (Base64).
            salida.println(CifradorRSA.exportarClavePublica(parServidor.getPublic()));

            // 2) Recibir la clave pública del cliente.
            String clientePubBase64 = entrada.readLine();
            PublicKey clavePublicaCliente = CifradorRSA.importarClavePublica(clientePubBase64);

            // ===== BUCLE DE MENSAJES =====
            String mensajeCifrado;
            while ((mensajeCifrado = entrada.readLine()) != null) {

                // Descifrar con NUESTRA clave privada
                String mensaje = CifradorRSA.descifrar(mensajeCifrado, parServidor.getPrivate());

                // Señal de parada
                if (mensaje.equals(".")) {
                    System.out.println("[Servidor RSA] Orden de parada recibida.");
                    // Cifrar respuesta con la pública DEL CLIENTE
                    salida.println(CifradorRSA.cifrar(echoData.toString(), clavePublicaCliente));
                    System.exit(0);
                }

                // Eco normal
                echoData.addMensaje(mensaje);
                salida.println(CifradorRSA.cifrar(mensaje, clavePublicaCliente));
                System.out.println("[Servidor RSA] Eco (claro): " + mensaje);
            }

        } catch (IOException e) {
            System.out.println("[Servidor RSA] Error con cliente: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Servidor RSA] Error de cifrado: " + e.getMessage());
        }
    }
}
