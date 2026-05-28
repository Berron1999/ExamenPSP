package calculadora;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorCalculadora {
    public static void main(String[] args) {
        try{
            ServerSocket servidor = new ServerSocket(5000);
            System.out.println("servidor esperando a cliente");


            Socket cliente = servidor.accept();
            System.out.println("cliente aceptado");


            BufferedReader br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            PrintWriter pw = new PrintWriter(cliente.getOutputStream(),true);

            String linea;
            while ((linea= br.readLine())!=null){

                if (linea.equalsIgnoreCase("fin")){
                    break;
                }

                String[] partes = linea.split(" ");
                String operacion = partes[0];
                double num1 = Double.parseDouble(partes[1]);
                double num2 = Double.parseDouble(partes[2]);

                double resultado =0;
                switch (operacion.toUpperCase()){
                    case "SUMA": resultado= num1+num2;
                        break;
                    case "RESTA": resultado= num1-num2;
                        break;
                    case "MULTI": resultado= num1*num2;
                        break;
                    case "DIV": resultado= num1/num2;
                        break;

                    default:
                        pw.println("Operacion invalida");
                        continue;
                }

                pw.println("Resultado: "+resultado);

                cliente.close();
                servidor.close();

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
