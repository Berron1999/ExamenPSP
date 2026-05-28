package eco;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class servidoreco {
    public static void main(String[] args) {

        try{
            ServerSocket servidor = new ServerSocket(5000);
            //
            System.out.println("Servidor esperando clientes");

            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado");

            BufferedReader br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            PrintWriter pw = new PrintWriter(cliente.getOutputStream(),true);

            String mensaje;

            while ((mensaje= br.readLine())!=null){
                System.out.println("Recibido: "+mensaje);

                if (mensaje.equalsIgnoreCase("fin".trim())){
                    break;
                }

                pw.println(mensaje);
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
