package org.iesch.psp.TCP.EchoServerSHAyAES;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class EchoClient implements Runnable {
    private int idCliente;

    public EchoClient(int idCliente) {
        this.idCliente = idCliente;
    }

    public void run() {
        String host = "localhost";
        int puerto = 5000;

        try (Socket socket = new Socket(host, puerto);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner in = new Scanner(socket.getInputStream())) {

            Random generador = new Random();

            for (int i = 1; i <= 100; i++) {
                int numeroAleatorio = generador.nextInt(401) + 100;

                // 1. Mensaje Original
                String mensaje = "Mensaje " + i + " del Cliente " + idCliente + " [NumAleatorio: " + numeroAleatorio + "]";

                // 2. Hash de Integridad
                String hashDelMensaje = HashUtil.getHash(mensaje);
                String mensajeConFirma = mensaje + "|" + hashDelMensaje;

                // 3. ¡ENCRIPTAMOS EL PAQUETE ENTERO!
                String paqueteCifrado = CryptoUtil.encriptar(mensajeConFirma);

                // Enviamos la basura encriptada (en formato Base64)
                out.println(paqueteCifrado);

                if (in.hasNextLine()) {
                    in.nextLine(); // Leemos el eco cifrado
                }
            }

            // Enviamos el FIN también cifrado para que el servidor lo entienda
            out.println(CryptoUtil.encriptar("FIN"));

        } catch (Exception e) {
            System.err.println("Error en la conexión del cliente " + idCliente);
        }
    }
}