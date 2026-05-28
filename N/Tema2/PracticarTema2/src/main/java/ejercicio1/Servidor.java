package ejercicio1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    static final String ruta ="./src/servidor_archivos";
    public static void main(String[] args) {
        try {

            ServerSocket servidor = new ServerSocket(5000);
            System.out.println("Servidor esperando a cliente...");

            while (true){
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado");

                new Thread(new ManejadorCliente(cliente)).start();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
