package servidorMulticliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Socket socket = new Socket("localhost",5000);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);

            int opcion =0;
            while (opcion!=2){
                System.out.println("\n ------------Menu Cliente----------");
                System.out.println("\n 1. Multiplicar por 10 el numero insertado");
                System.out.println("\n 2. salir");
                opcion=Integer.parseInt(scanner.nextLine());
                switch (opcion){
                    case 1:
                        System.out.println("Dime un numero para calcular: ");
                        double numero = Double.parseDouble(scanner.nextLine());
                        pw.println("CALCULAR "+ numero);
                        String respuesta = br.readLine();
                        System.out.println("El resultado es :"+ respuesta);
                        break;
                    case 2:
                        pw.println("SALIR");
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opcion no valida");
                }
            }
            socket.close();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
