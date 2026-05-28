package tcpClienteServidorMultihilo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ManejadorHi implements Runnable {
    Socket socket;
    public ManejadorHi(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);

            String linea;
            while ((linea=br.readLine())!=null){

                if (linea.startsWith("MAY")){
                    String texto = linea.substring(4);//Utilizamos esta para que justo despues del MAY y el espacio que hay lo demas se pasa a mayus
                    //String[] partes = linea.split(" ");
                    String palabra = texto;
                    String resultado = palabra.toUpperCase();
                    pw.println("El resultado es: "+resultado);

                }else if (linea.startsWith("MIN")){
                    String texto = linea.substring(4);
                    String palabra = texto;
                    String resultado = palabra.toLowerCase();
                    pw.println("El resultado es: "+resultado);
                }else if (linea.startsWith("INV")){
                    String texto = linea.substring(4);
                    String palabra = texto;
                    StringBuilder sb = new StringBuilder(palabra);
                    sb.reverse();
                    pw.println("El resultado es: "+sb);
                }else if (linea.equalsIgnoreCase("SALIR")){
                    pw.println("Conexion cerrada");
                    break;
                }else pw.println("Comando invalido");

            }
            socket.close();
            System.out.printf("Cliente desconectado");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
