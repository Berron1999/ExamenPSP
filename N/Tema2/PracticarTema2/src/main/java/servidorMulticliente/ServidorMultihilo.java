package servidorMulticliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorMultihilo {
    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(5000);
            System.out.println("Servidor esperando clientes");


            while (true){
                Socket cliente = servidor.accept();
                System.out.println("Cliente aceptado");

                new Thread((new ManejadorHilos(cliente))).start();
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
