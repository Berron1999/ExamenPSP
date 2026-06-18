package FirmaDigitalDocumentos;

import java.net.ServerSocket;
import java.net.Socket;

public class ServidorNotaria {

    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(7600);
            System.out.println("Servidor de la notaria escuchando en el puerto 7600...");

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());

                ManejadorDocumento manejador = new ManejadorDocumento(cliente);
                manejador.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}