package TCPSSLTLS;

import TCPSSLTLS.DatosMensaje;
import TCPSSLTLS.RespuestaServidor;

import javax.net.ssl.SSLSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;

// Hilo que atiende a un cliente SSL concreto en el servidor
public class HiloClienteSSL extends Thread {

    private SSLSocket socket; // SSLSocket en lugar de Socket normal

    public HiloClienteSSL(SSLSocket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            // Streams de entrada y salida — igual que en TCP normal
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Leemos el objeto enviado por el cliente
            DatosMensaje datos = (DatosMensaje) in.readObject();
            System.out.println("Recibido de " + datos.getNombreUsuario() + ": " + datos.getMensaje());

            // Generamos el hash SHA-256 de nombreUsuario + mensaje
            String textoAHashear = datos.getNombreUsuario() + datos.getMensaje();
            byte[] bytes = textoAHashear.getBytes(StandardCharsets.UTF_16LE);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(bytes);
            String hash = Base64.getEncoder().encodeToString(hashBytes);

            // Creamos y enviamos la respuesta
            RespuestaServidor respuesta = new RespuestaServidor(
                    datos.getNombreUsuario(),
                    hash,
                    datos.getMensaje().length(),
                    LocalDateTime.now()
            );
            out.reset();
            out.writeObject(respuesta);

            // Cerramos el socket de este cliente
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}