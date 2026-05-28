package org.iesch.psp.TCP.EchoServerSHAyAES;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// ClienteAdministrador que para el servidor enviándole un punto cifrado y mostrando las estadísticas
public class EchoStopper {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner in = new Scanner(socket.getInputStream())) {

            System.out.println("Enviando orden de parada al servidor...");

            // 1. Encriptamos el comando de parada (el punto)
            String comandoCifrado = CryptoUtil.encriptar(".");
            out.println(comandoCifrado);

            // 2. Leemos la respuesta (que el servidor también nos enviará cifrada)
            if (in.hasNextLine()) {
                String respuestaCifrada = in.nextLine();

                // 3. Desciframos las estadísticas para poder leerlas en pantalla
                String respuestaDescifrada = CryptoUtil.desencriptar(respuestaCifrada);
                System.out.println("Respuesta del servidor: " + respuestaDescifrada);
            }

        } catch (Exception e) {
            System.err.println("No se pudo conectar al servidor.");
            e.printStackTrace();
        }
    }
}