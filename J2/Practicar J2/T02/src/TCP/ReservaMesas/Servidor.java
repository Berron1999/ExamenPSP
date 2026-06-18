package TCP.ReservaMesas;

import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) {
        try {
            GestorMesas gestor = new GestorMesas(); // una sola instancia compartida por todos los clientes

            ServerSocket servidor = new ServerSocket(6500);
            System.out.println("Servidor de reservas escuchando en el puerto 6500...");

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());

                ManejadorCliente manejador = new ManejadorCliente(cliente, gestor);
                manejador.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}