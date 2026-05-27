package ClienteServidorImagenes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorImagenes {

    static final int    PUERTO     = 5001;
    static final String DIRECTORIO = "C:\\servidor_imagenes\\";

    public static void main(String[] args) {
        System.out.println("Servidor de imágenes iniciado en puerto " + PUERTO);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("Cliente conectado: " + socketCliente.getInetAddress());
                // Un hilo por cliente para atender múltiples conexiones simultáneas
                new Thread(new HiloClienteImagenes(socketCliente)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}