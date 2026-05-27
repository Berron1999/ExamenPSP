package ServidorDeTareasConPoolDeHilos;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClienteCalculos {

    static final String HOST   = "localhost";
    static final int    PUERTO = 5005;

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        try (
                Socket         socket  = new Socket(HOST, PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Conectado al servidor de cálculos.");

            boolean ejecutando = true;
            while (ejecutando) {
                System.out.println("\n1. Calcular factorial");
                System.out.println("2. Salir");
                System.out.print("Opción: ");
                String opcion = teclado.nextLine().trim();

                switch (opcion) {
                    case "1":
                        System.out.print("Introduce un número: ");
                        String num = teclado.nextLine().trim();
                        salida.println(num);
                        System.out.println("Calculando... (puede tardar)");
                        // Bloqueamos aquí hasta recibir el resultado del servidor
                        System.out.println("Resultado: " + entrada.readLine());
                        break;

                    case "2":
                        salida.println("SALIR");
                        ejecutando = false;
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}