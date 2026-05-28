package clienteServidorBasicos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteBasico {
    public static void main(String[] args) {
        try {

            Socket socket = new Socket("localhost",5000);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);

            pw.println("Hola servidor ");
            String respuesta = br.readLine();
            System.out.println("Servidor responde: "+respuesta);

            socket.close();



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
