package ServidorCalificacionesCifradas;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TareaProfesor implements Runnable {

    private Socket socket;

    public TareaProfesor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // out antes que in en ambos lados, para evitar deadlock
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            DatosNota datos = (DatosNota) in.readObject();
            String texto = datos.getAlumno() + " ha obtenido un " + datos.getNota();

            String hash = UtilSeguridad.calcularSHA256(texto);
            UtilSeguridad.ResultadoCifrado resultado = UtilSeguridad.cifrar(texto);

            RespuestaCifrada respuesta = new RespuestaCifrada(resultado.datos, resultado.iv, hash);
            out.writeObject(respuesta);

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}