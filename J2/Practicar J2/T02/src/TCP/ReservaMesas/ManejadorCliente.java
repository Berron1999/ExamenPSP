package TCP.ReservaMesas;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ManejadorCliente extends Thread {

    private Socket socket;
    private GestorMesas gestor;

    public ManejadorCliente(Socket socket, GestorMesas gestor) {
        this.socket = socket;
        this.gestor = gestor;
    }

    @Override
    public void run() {
        try {
            // out antes que in en ambos lados, para evitar deadlock
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            DatosReserva datos = (DatosReserva) in.readObject();
            int mesa = datos.getNumeroMesa();

            boolean exito = gestor.reservar(mesa);

            RespuestaReserva respuesta;
            if (exito) {
                respuesta = new RespuestaReserva("Mesa " + mesa + " reservada con éxito", true);
            } else {
                respuesta = new RespuestaReserva("Mesa " + mesa + " no disponible", false);
            }

            out.writeObject(respuesta);
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}