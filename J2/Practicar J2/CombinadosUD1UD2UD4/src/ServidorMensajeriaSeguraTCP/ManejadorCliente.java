package ServidorMensajeriaSeguraTCP;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ManejadorCliente extends Thread {

    private Socket socket;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // out antes que in en ambos lados, para evitar deadlock
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            MensajeCifrado mensaje = (MensajeCifrado) in.readObject();

            String textoDescifrado = UtilCifrado.descifrar(mensaje.getDatosCifrados(), mensaje.getIv());
            String hashCalculado = UtilCifrado.calcularSHA256(textoDescifrado);

            if (hashCalculado.equals(mensaje.getHash())) {
                System.out.println("Mensaje verificado: " + textoDescifrado);
                out.writeObject("OK: mensaje recibido correctamente");
            } else {
                System.out.println("¡Integridad comprometida!");
                out.writeObject("ERROR: integridad comprometida");
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}