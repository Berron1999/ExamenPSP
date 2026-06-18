package UDP.AlertasMetereologicas;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ClienteAlertas {

    public static void main(String[] args) {
        try {
            MulticastSocket socket = new MulticastSocket(9000);

            InetAddress grupo = InetAddress.getByName("230.0.0.1");
            socket.joinGroup(grupo); // a partir de aqui el socket recibe lo que se envia al grupo

            System.out.println("Esperando alertas del grupo " + grupo.getHostAddress() + "...");

            for (int i = 0; i < 5; i++) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // deserializamos limitando al tamaño real recibido, no al tamaño del buffer
                ByteArrayInputStream byteStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                ObjectInputStream in = new ObjectInputStream(byteStream);
                DatosAlerta alerta = (DatosAlerta) in.readObject();

                System.out.println("Recibida: " + alerta);
            }

            socket.leaveGroup(grupo);
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}