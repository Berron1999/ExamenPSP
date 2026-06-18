package TCP.ValidacionProductos;

import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(6000);
            System.out.println("Servidor escuchando en el puerto 6000...");

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());

                // un hilo distinto por cada cliente, así el servidor puede atender varios a la vez
                ManejadorCliente manejador = new ManejadorCliente(cliente);
                manejador.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}