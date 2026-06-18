package TCPSHA256;

import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    private static final int PUERTO = 6300;

    public static void main(String[] args) {
        try {
            // Abrimos el ServerSocket en el puerto 6300
            ServerSocket serverSocket = new ServerSocket(PUERTO);
            System.out.println("TCPSSLTLS.Servidor escuchando en el puerto " + PUERTO);

            // El servidor permanece activo indefinidamente
            while (true) {
                // Esperamos a que un cliente se conecte
                Socket socket = serverSocket.accept();
                System.out.println("TCPSSLTLS.Cliente conectado: " + socket.getInetAddress());

                // Creamos un hilo para atenderle y lo lanzamos
                new HiloCliente(socket).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}