package org.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try{
            Socket socket = new Socket("localhost",5000);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);

            //A ver necesitamos listar los contactos , que sera la opcion 1,
            //Necesitamos la opcion dos que es buscar por nombre,
            //Y la opcion 3 que sera salir.

            int opcion=0;
            String linea;
            while (opcion!=3){

                System.out.println("Agenda");
                System.out.println("Opcion 1: Listar Contactos");
                System.out.println("Opcion 2: Buscar por nombre");
                System.out.println("Opcion 3: Salir");
                opcion = Integer.parseInt(sc.nextLine());
                switch (opcion){
                    case 1:
                        pw.println("LISTAR");


                        System.out.println("Tus contactos: \n");
                        while ((linea= br.readLine())!=null){
                            if (linea.equals("FIN")) break; // Si llega el FIN, salimos del bucle de lectura
                            System.out.println(linea);      // Imprimimos la variable 'linea'
                        }
                        break;
                    case 2:
                        System.out.println("Dime el nombre:");
                        String nombre = sc.nextLine();
                        pw.println("BUSCAR " + nombre); // Envías el comando y el nombre juntos

                        String respuesta = br.readLine();
                        System.out.println(respuesta);
                        break;
                    case 3:
                        System.out.println("Saliendo");
                        break;
                    default:
                        System.out.println("Opcion no valida");


                }

            }

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
