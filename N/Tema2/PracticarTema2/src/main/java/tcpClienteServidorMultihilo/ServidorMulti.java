package tcpClienteServidorMultihilo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorMulti {
    public static void main(String[] args) {

        try {
            ServerSocket servidor = new ServerSocket(5000);
            System.out.println("Servidor esperando clientes");

            while (true){
                Socket socket = servidor.accept();
                System.out.println("Cliente aceptado");

                new Thread((new ManejadorHi(socket))).start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
