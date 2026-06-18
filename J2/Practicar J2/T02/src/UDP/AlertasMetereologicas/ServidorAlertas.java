package UDP.AlertasMetereologicas;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class ServidorAlertas {

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(); // socket normal, solo va a enviar

            InetAddress grupo = InetAddress.getByName("230.0.0.1");
            int puerto = 9000;

            String[] tipos = {"LLUVIA", "VIENTO", "NIEVE"};
            Random random = new Random();

            for (int i = 0; i < 5; i++) {
                String tipo = tipos[random.nextInt(tipos.length)];
                int nivel = random.nextInt(3) + 1; // entre 1 y 3
                DatosAlerta alerta = new DatosAlerta(tipo, nivel);

                // serializamos la alerta a bytes para poder enviarla en el datagrama
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteStream);
                out.writeObject(alerta);
                out.flush();
                byte[] datos = byteStream.toByteArray();

                DatagramPacket packet = new DatagramPacket(datos, datos.length, grupo, puerto);
                socket.send(packet);

                System.out.println("Emitida: " + alerta);
                Thread.sleep(2000);
            }

            System.out.println("El servidor ha terminado de emitir alertas.");
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}