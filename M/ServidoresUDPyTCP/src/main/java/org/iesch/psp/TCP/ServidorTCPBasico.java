package org.iesch.psp.TCP;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServidorTCPBasico {
    public static void main(String[] args) {
        int puerto = 5000;

        // 1. Creamos el recepcionista
        try (ServerSocket svr = new ServerSocket(puerto)) {
            System.out.println("Servidor TCP esperando cliente...");

            // 2. Aceptamos al cliente cuando llame
            try (Socket cli = svr.accept()) {
                System.out.println("¡Cliente conectado!");

                // 3. Preparamos los canales para hablar y escuchar
                Scanner in = new Scanner(cli.getInputStream());
                PrintWriter out = new PrintWriter(cli.getOutputStream(), true);

                // 4. Leemos lo que nos manda el cliente
                if (in.hasNextLine()) {
                    String mensaje = in.nextLine();
                    System.out.println("Cliente dice: " + mensaje);

                    // Le respondemos
                    out.println("Mensaje recibido alto y claro.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}