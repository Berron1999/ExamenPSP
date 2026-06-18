package UDP.SistemaAvisosTrafico;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

public class ServidorAvisos {

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(8000);
            socket.setSoTimeout(60000); // 1 minuto de inactividad maxima
            System.out.println("Servidor de avisos escuchando en el puerto 8000...");

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                try {
                    socket.receive(packet);
                } catch (SocketTimeoutException e) {
                    // no es un error, es el aviso de que pasó el minuto sin recibir nada
                    System.out.println("Servidor cerrado por inactividad");
                    break;
                }

                // deserializamos el objeto a partir de los bytes recibidos
                ByteArrayInputStream byteStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                ObjectInputStream in = new ObjectInputStream(byteStream);
                DatosAviso aviso = (DatosAviso) in.readObject();

                System.out.println("Recibido: " + aviso);

                String mensaje = "Aviso registrado: carretera " + aviso.getCarretera()
                        + ", gravedad " + aviso.getGravedad();
                byte[] respuesta = mensaje.getBytes();

                // respondemos a la IP y puerto de origen del paquete recibido
                DatagramPacket respPacket = new DatagramPacket(
                        respuesta, respuesta.length, packet.getAddress(), packet.getPort());
                socket.send(respPacket);
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}