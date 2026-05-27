package EchoServerExamenFirmaECDSA;

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

                salida.println(FirmaECDSA.empaquetar(msgClaro));

                String respuesta = entrada.readLine();
                int sep = respuesta.lastIndexOf(FirmaECDSA.SEP);
                String eco           = respuesta.substring(0, sep);
                String firmaRecibida = respuesta.substring(sep + 1);
                boolean firmaValida  = FirmaECDSA.verificar(eco, firmaRecibida);

                System.out.println("[Cliente-" + idCliente + "] Eco: " + eco
                        + " | Correcto: " + msgClaro.equals(eco)
                        + " | Firma ECDSA válida: " + firmaValida);
            }
        } catch (Exception e) {
            System.out.println("[Cliente-" + idCliente + "] Error: " + e.getMessage());
        }
    }
}
