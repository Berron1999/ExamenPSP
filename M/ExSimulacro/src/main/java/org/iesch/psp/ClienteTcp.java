package org.iesch.psp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClienteTcp {
    public static final int PUERTO = 5000;

    public static void main(String[] args) {
        BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));
        String continuar = "si";

        while (continuar.equalsIgnoreCase("si")) {
            try {
                // Lee datos por consola.
                System.out.print("URL: ");
                String url = consola.readLine();
                System.out.print("Palabra: ");
                String cadena = consola.readLine();

                // Envia la busqueda al servidor.
                int resultado = enviarBusqueda(url, cadena);
                System.out.println("Ocurrencias: " + resultado);
            } catch (IOException e) {
                System.out.println("Error en cliente: " + e.getMessage());
            }

            try {
                // Pregunta si se desea continuar.
                System.out.print("Continuar? (si/no): ");
                continuar = consola.readLine();
            } catch (IOException e) {
                continuar = "no";
            }
        }
    }

    private static int enviarBusqueda(String url, String cadena) throws IOException {
        // Abre el socket, envia el objeto y lee la respuesta.
        try (Socket socket = new Socket("localhost", PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {
            DatosBusqueda datos = new DatosBusqueda(url, cadena);
            salida.writeObject(datos);
            salida.flush();
            return entrada.readInt();
        }
    }
}
