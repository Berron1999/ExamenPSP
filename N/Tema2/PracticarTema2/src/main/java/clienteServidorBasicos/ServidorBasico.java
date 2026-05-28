package clienteServidorBasicos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorBasico {
    public static void main(String[] args) {

        try{
            ServerSocket servidor = new ServerSocket(5000);
            System.out.println("Servidor esperando cliente");


            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado");

            BufferedReader br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter pw = new PrintWriter(cliente.getOutputStream());

            String mensaje = br.readLine();
            System.out.println("El cliente dice: "+ mensaje);

            pw.println("Hola cliente, recibido tu mensaje!"); // responde

            cliente.close();
            servidor.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
