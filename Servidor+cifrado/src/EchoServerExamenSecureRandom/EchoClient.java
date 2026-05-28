package EchoServerExamenSecureRandom;

import java.io.*;
import java.net.Socket;

/*
 * Cliente que cifra con AES/CBC y un IV aleatorio por mensaje, generado
 * con SecureRandom (RNGCryptoServiceProvider equivalente en .NET).
 */
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
            for (int i = 1; i <= numMensajes; i++) {
                String msg = "Cliente-" + idCliente + "-Msg-" + i;

                // Cifrar con IV aleatorio (SecureRandom interno) y enviar
                salida.println(CifradorSecureRandom.cifrar(msg));

                // Recibir trama cifrada y descifrar
                String tramaRespuesta = entrada.readLine();
                String eco            = CifradorSecureRandom.descifrar(tramaRespuesta);

                System.out.println("[Cliente-" + idCliente + "] Eco recibido: " + eco);
            }
        } catch (IOException e) {
            System.out.println("[Cliente-" + idCliente + "] Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Cliente-" + idCliente + "] Error de cifrado: " + e.getMessage());
        }
    }
}
