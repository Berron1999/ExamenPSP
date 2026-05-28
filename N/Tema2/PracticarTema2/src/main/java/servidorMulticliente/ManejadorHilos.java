package servidorMulticliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ManejadorHilos implements Runnable {

    Socket socket ;
    public ManejadorHilos(Socket socket) {
        this.socket=socket;
    }


    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);

            String linea;

            while ((linea= br.readLine())!=null){

                if (linea.startsWith("CALCULAR")){
                    String[] partes = linea.split(" ");
                    double numero = Double.parseDouble(partes[1]);
                    double resultado = numero*10;
                    pw.println("El resultado es:"+ resultado);
                } else if (linea.equalsIgnoreCase("SALIR")) {
                    pw.println("Conexion cerrada.");
                    break;
                }else {
                    pw.println("Comando invalido");
                }
            }
            socket.close();
            System.out.printf("Cliente desconectado");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
