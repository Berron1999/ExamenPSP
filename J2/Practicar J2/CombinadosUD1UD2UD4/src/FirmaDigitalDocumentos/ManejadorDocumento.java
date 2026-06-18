package FirmaDigitalDocumentos;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class ManejadorDocumento extends Thread {

    private Socket socket;

    public ManejadorDocumento(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // out antes que in en ambos lados, para evitar deadlock
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            DatosDocumento datos = (DatosDocumento) in.readObject();

            // reconstruimos la clave publica a partir de los bytes recibidos
            KeyFactory factory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(datos.getClavePublica());
            PublicKey clavePublica = factory.generatePublic(spec);

            boolean esValida = UtilFirma.verificar(datos.getContenido(), datos.getFirma(), clavePublica);

            RespuestaVerificacion respuesta;
            if (esValida) {
                respuesta = new RespuestaVerificacion(true, "Documento auténtico");
            } else {
                respuesta = new RespuestaVerificacion(false, "Firma inválida, documento rechazado");
            }

            out.writeObject(respuesta);
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}