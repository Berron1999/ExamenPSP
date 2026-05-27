package EchoServerExamenCifradoAES;


import java.io.*;
import java.net.Socket;

// Hilo del servidor: recibe mensajes cifrados, los descifra, hace eco y los vuelve a cifrar
public class EchoServerThread extends Thread {

    private Socket   socket;
    private EchoData echoData;

    public EchoServerThread(Socket socket, EchoData echoData) {
        this.socket   = socket;
        this.echoData = echoData;
    }

    public void run() {
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String mensajeCifrado;
            while ((mensajeCifrado = entrada.readLine()) != null) {

                // Desciframos el mensaje recibido
                String mensajeClaro = CifradoAES.descifrar(mensajeCifrado);

                // "." descifrado → señal de parada
                if (mensajeClaro.equals(".")) {
                    System.out.println("[Servidor] Orden de parada recibida.");
                    // Ciframos los datos de uso y los enviamos al EchoStopper
                    salida.println(CifradoAES.cifrar(echoData.toString()));
                    System.exit(0);
                }

                // Actualizamos EchoData con el mensaje en claro (synchronized internamente)
                echoData.addMensaje(mensajeClaro);

                // Ciframos el eco y lo devolvemos al cliente
                salida.println(CifradoAES.cifrar(mensajeClaro));
                System.out.println("[Servidor] Eco enviado para: " + mensajeClaro);
            }

        } catch (Exception e) {
            System.out.println("[Servidor] Error con cliente: " + e.getMessage());
        }
    }
}