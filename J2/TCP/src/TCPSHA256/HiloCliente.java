package TCPSHA256;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;

// Hilo que atiende a un cliente concreto en el servidor
public class HiloCliente extends Thread {

    private Socket socket;

    public HiloCliente(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            // Abrimos los streams de entrada y salida del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Leemos el objeto que nos envía el cliente
            DatosMensaje datos = (DatosMensaje) in.readObject();
            System.out.println("Recibido de " + datos.getNombreUsuario() + ": " + datos.getMensaje());

            // Generamos el hash SHA-256 de nombreUsuario + mensaje (igual que en los apuntes)
            String textoAHashear = datos.getNombreUsuario() + datos.getMensaje();
            byte[] bytes = textoAHashear.getBytes(StandardCharsets.UTF_16LE);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(bytes);
            String hash = Base64.getEncoder().encodeToString(hashBytes);

            // Creamos la respuesta con todos los datos
            RespuestaServidor respuesta = new RespuestaServidor(
                    datos.getNombreUsuario(),
                    hash,
                    datos.getMensaje().length(),  // Número de caracteres del mensaje
                    LocalDateTime.now()           // Fecha de recepción en el servidor
            );

            // Enviamos la respuesta al cliente
            out.reset(); // Recomendado al enviar objetos por stream
            out.writeObject(respuesta);

            // Cerramos el socket de este cliente (el hilo termina aquí)
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}