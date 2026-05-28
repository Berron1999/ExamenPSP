package EchoServerExamenAES;

import java.io.*;
import java.net.Socket;

/*
 * Cliente del eco.
 *
 * Extiende Thread porque el EchoClientLauncher lanza 100 instancias
 * en paralelo (ejercicio 3 del enunciado). Cada cliente:
 *   1. Cifra el mensaje con AES antes de enviarlo por el socket.
 *   2. Recibe el eco también cifrado y lo descifra antes de imprimirlo.
 *
 * El cliente y el servidor comparten la MISMA clave AES (cifrado simétrico).
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

                // Cifrar antes de enviar -> por el socket viaja Base64
                salida.println(CifradorAES.cifrar(msg));

                // Recibir el eco cifrado y descifrarlo para mostrarlo
                String ecoCifrado = entrada.readLine();
                String eco        = CifradorAES.descifrar(ecoCifrado);

                System.out.println("[Cliente-" + idCliente + "] Eco recibido: " + eco);
            }
        } catch (IOException e) {
            System.out.println("[Cliente-" + idCliente + "] Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Cliente-" + idCliente + "] Error de cifrado: " + e.getMessage());
        }
    }
}
