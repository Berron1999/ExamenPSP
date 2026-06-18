package TransferenciasBancariasSeguras;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class ServidorBanco {

    public static void main(String[] args) {
        try {
            Semaphore semaforo = new Semaphore(2); // solo 2 transferencias a la vez

            ServerSocket servidor = new ServerSocket(7400);
            System.out.println("Servidor del banco escuchando en el puerto 7400...");

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());

                ManejadorTransferencia manejador = new ManejadorTransferencia(cliente, semaforo);
                manejador.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}