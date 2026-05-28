package EchoServerExamenSHA512;

import java.io.*;
import java.net.Socket;

/*
 * Cliente con SHA-512: cada mensaje viaja acompañado de su hash de 128 chars hex.
 * El servidor lo verifica antes de hacer eco, y el cliente verifica el eco.
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

                // Enviar mensaje + hash SHA-512
                salida.println(HashSHA512.empaquetar(msg));

                // Recibir eco + hash y verificarlo
                String respuesta = entrada.readLine();
                String[] partes  = respuesta.split("\\" + HashSHA512.SEPARADOR, 2);
                String   eco     = partes[0];
                String   hashRx  = partes[1];

                if (!HashSHA512.verificar(eco, hashRx)) {
                    System.out.println("[Cliente-" + idCliente + "] ¡HASH inválido en eco!");
                } else {
                    System.out.println("[Cliente-" + idCliente + "] Eco verificado: " + eco);
                }
            }
        } catch (IOException e) {
            System.out.println("[Cliente-" + idCliente + "] Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Cliente-" + idCliente + "] Error de hash: " + e.getMessage());
        }
    }
}
