package EchoServerExamen;

import java.io.*;
import java.net.Socket;

public class EchoClient extends Thread {  // <-- añadir esto

    static final String HOST   = "localhost";
    static final int    PUERTO = 5008;

    private int idCliente;
    private int numMensajes;

    public EchoClient(int idCliente, int numMensajes) {
        this.idCliente   = idCliente;
        this.numMensajes = numMensajes;
    }

    public void run() {  // <-- cambiar enviar() por run()
        try (
                Socket         socket  = new Socket(HOST, PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            for (int i = 1; i <= numMensajes; i++) {
                String msg = "Cliente-" + idCliente + "-Msg-" + i;
                salida.println(msg);
                String eco = entrada.readLine();
                System.out.println("[Cliente-" + idCliente + "] Eco recibido: " + eco);
            }
        } catch (IOException e) {
            System.out.println("[Cliente-" + idCliente + "] Error: " + e.getMessage());
        }
    }
}