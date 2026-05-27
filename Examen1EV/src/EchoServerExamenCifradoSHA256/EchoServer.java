package EchoServerExamenCifradoSHA256;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    static final int      PUERTO   = 5008;
    static final EchoData echoData = new EchoData();

    public static void main(String[] args) {
        System.out.println("[Servidor] Iniciado en puerto " + PUERTO);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO, 200)) {
            // Bucle infinito → acepta clientes continuamente
            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("[Servidor] Cliente conectado: " + socketCliente.getInetAddress());
                // Un hilo por cliente para atender múltiples conexiones simultáneas
                new EchoServerThread(socketCliente, echoData).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
