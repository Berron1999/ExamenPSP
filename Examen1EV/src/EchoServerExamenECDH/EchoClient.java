package EchoServerExamenECDH;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;

// Cliente con HANDSHAKE ECDH:
// 1. Genera su pareja EC, envía su pública (primera línea)
// 2. Recibe la pública del servidor
// 3. Deriva la clave AES de sesión
// 4. Envía mensajes cifrados con AES
public class EchoClient extends Thread {

    static final String HOST   = "localhost";
    static final int    PUERTO = 5008;

    private int idCliente;
    private int numMensajes;

    public EchoClient(int idCliente, int numMensajes) {
        this.idCliente   = idCliente;
        this.numMensajes = numMensajes;
    }

    public void run() {
        try (
                Socket         socket  = new Socket(HOST, PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // === HANDSHAKE ECDH ===
            KeyPair miPar = ECDHHelper.generarParejaEC();
            salida.println(ECDHHelper.codificarPublica(miPar.getPublic()));
            PublicKey publicaServidor = ECDHHelper.decodificarPublica(entrada.readLine());
            byte[] aesKey = ECDHHelper.derivarClaveAES(miPar.getPrivate(), publicaServidor);

            // === Comunicación cifrada ===
            for (int i = 1; i <= numMensajes; i++) {
                String msgClaro = "Cliente-" + idCliente + "-Msg-" + i;
                salida.println(ECDHHelper.cifrar(msgClaro, aesKey));
                String ecoClaro = ECDHHelper.descifrar(entrada.readLine(), aesKey);
                System.out.println("[Cliente-" + idCliente + "] Eco: " + ecoClaro
                        + " | Correcto: " + msgClaro.equals(ecoClaro));
            }
        } catch (Exception e) {
            System.out.println("[Cliente-" + idCliente + "] Error: " + e.getMessage());
        }
    }
}
