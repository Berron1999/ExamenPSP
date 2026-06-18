package ServidorMensajeriaSeguraTCP;

import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(7000);
            System.out.println("Servidor de mensajeria segura escuchando en el puerto 7000...");

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());

                ManejadorCliente manejador = new ManejadorCliente(cliente);
                manejador.start(); // hilo independiente para no bloquear al resto de clientes
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}