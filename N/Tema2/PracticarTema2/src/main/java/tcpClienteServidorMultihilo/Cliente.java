package tcpClienteServidorMultihilo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Socket socket = new Socket("localhost",5000);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);

            int opcion =0;
            while (opcion!=4){
                System.out.println("\n ========Menu clientes====");
                System.out.println("1. Pasar texto a mayusculas");
                System.out.println("2. Pasar texto a minusculas");
                System.out.println("3. Pasar texto a inversa");
                System.out.println("4. Salir");
                opcion = Integer.parseInt(sc.nextLine());

                switch (opcion){
                    case 1:
                        System.out.println("Que palabra quieres pasar a mayusculas: ");
                        String palabra = sc.nextLine();
                        pw.println("MAY "+palabra);
                        String respuesta = br.readLine();
                        System.out.println("La palabra en mayus es: "+respuesta);
                        break;
                    case 2:
                        System.out.println("Que palabra quieres pasar a minusculas: ");
                        String palabraPasada = sc.nextLine();
                        pw.println("MIN "+palabraPasada);
                        String resultado = br.readLine();
                        System.out.println("La palabra en mayus es: "+resultado);
                        break;
                    case 3:
                        System.out.println("Que palabra quieres dar la vuelta: ");
                        String p = sc.nextLine();
                        pw.println("INV "+p);
                        String r = br.readLine();
                        System.out.println("La palabra en mayus es: "+r);
                        break;
                    case 4:
                        pw.println("SALIR");
                        System.out.println("Saliendo");
                        break;
                    default:
                        System.out.println("Opcion no disponible");
                        break;
                }

            }

            socket.close();
            System.out.println("Cliente desconectado");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
