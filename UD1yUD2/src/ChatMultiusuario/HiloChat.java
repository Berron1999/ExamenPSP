package ChatMultiusuario;

import ChatMultiusuario.ServidorChat;

import java.io.*;
import java.net.Socket;

public class HiloChat implements Runnable {

    private Socket socket;

    public HiloChat(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        PrintWriter salida = null;
        String nick = "Desconocido";

        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            salida = new PrintWriter(socket.getOutputStream(), true);

            // El primer mensaje del cliente es siempre su nick
            nick = entrada.readLine();

            // Añadimos su PrintWriter a la lista para que reciba los broadcast
            ServidorChat.clientes.add(salida);
            System.out.println(nick + " se ha conectado. Clientes: " + ServidorChat.clientes.size());

            // Avisamos a todos de que ha entrado
            ServidorChat.broadcast("*** " + nick + " se ha unido al chat ***");

            // Leemos mensajes del cliente y los retransmitimos a todos
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                if (mensaje.equalsIgnoreCase("SALIR")) break;
                ServidorChat.broadcast("[" + nick + "]: " + mensaje);
            }

        } catch (IOException e) {
            System.out.println("Error con " + nick + ": " + e.getMessage());
        } finally {
            // Al desconectarse: lo eliminamos de la lista y avisamos a todos
            if (salida != null) ServidorChat.clientes.remove(salida);
            ServidorChat.broadcast("*** " + nick + " ha abandonado el chat ***");
            System.out.println(nick + " desconectado. Clientes: " + ServidorChat.clientes.size());
            try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }
}