package EchoServerExamenHibridoAESRSA;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
                String msgClaro = "Cliente-" + idCliente + "-Msg-" + i;

                salida.println(CifradoHibrido.cifrar(msgClaro));
                String ecoClaro = CifradoHibrido.descifrar(entrada.readLine());

                System.out.println("[Cliente-" + idCliente + "] Eco: " + ecoClaro
                        + " | Correcto: " + msgClaro.equals(ecoClaro));
            }
        } catch (Exception e) {
            System.out.println("[Cliente-" + idCliente + "] Error: " + e.getMessage());
        }
    }
}
