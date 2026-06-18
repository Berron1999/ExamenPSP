package UDP.SistemaAvisosTrafico;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClienteAviso {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            DatagramSocket socket = new DatagramSocket(); // puerto temporal asignado por el sistema

            System.out.print("Carretera: ");
            String carretera = sc.nextLine();
            System.out.print("Gravedad (1-3): ");
            int gravedad = Integer.parseInt(sc.nextLine());

            DatosAviso aviso = new DatosAviso(carretera, gravedad);

            // serializamos el objeto a un array de bytes para poder meterlo en el datagrama
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteStream);
            out.writeObject(aviso);
            out.flush();
            byte[] datos = byteStream.toByteArray();

            InetAddress destino = InetAddress.getByName("localhost");
            DatagramPacket packet = new DatagramPacket(datos, datos.length, destino, 8000);
            socket.send(packet);

            // esperamos la respuesta del servidor
            byte[] bufferRespuesta = new byte[1024];
            DatagramPacket respuestaPacket = new DatagramPacket(bufferRespuesta, bufferRespuesta.length);
            socket.receive(respuestaPacket);

            String respuesta = new String(respuestaPacket.getData(), 0, respuestaPacket.getLength());
            System.out.println("Respuesta del servidor: " + respuesta);

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}