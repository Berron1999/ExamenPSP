package ejercicio1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class cliente {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try{
            Socket socket = new Socket("localhost",5000);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);

            int opcion=0;

            while (opcion!=3){
                System.out.println("\n ------------Menu Cliente----------");
                System.out.println("\n 1. listar ficheros");
                System.out.println("\n 2. mostrar fichero");
                System.out.println("\n 3. salir");
                opcion=Integer.parseInt(scanner.nextLine());

                switch (opcion){
                    case 1:
                        pw.println("LISTAR");
                        String lista = br.readLine();
                        System.out.println("Ficheros disponibles: ");
                        System.out.println(lista.replace(" ","\n"));
                        break;
                    case 2:
                        System.out.println(" Nombre del fichero");
                        String nombre = scanner.nextLine();
                        pw.println("MOSTRAR "+ nombre);
                        String contenido = br.readLine();
                        System.out.println("Contenido recibido \n"+ contenido);
                        break;
                    case 3:
                        pw.println("SALIR");
                        System.out.println("Saliendo...");
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
