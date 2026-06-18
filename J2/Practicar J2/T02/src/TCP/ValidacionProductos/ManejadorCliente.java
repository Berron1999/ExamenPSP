package TCP.ValidacionProductos;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ManejadorCliente extends Thread {

    private Socket socket;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // el out se abre antes que el in en AMBOS lados para evitar deadlock
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            DatosProducto datos = (DatosProducto) in.readObject();
            System.out.println("Recibido: " + datos);

            String hash = calcularSHA256(datos.getCodigo() + datos.getPrecio());
            boolean valido = datos.getPrecio() > 0 && !datos.getCodigo().isEmpty();

            RespuestaValidacion respuesta = new RespuestaValidacion(hash, valido);
            out.writeObject(respuesta);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // calcula el hash SHA-256 de un texto y lo devuelve en formato hexadecimal
    private String calcularSHA256(String texto) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytesHash = md.digest(texto.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : bytesHash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}