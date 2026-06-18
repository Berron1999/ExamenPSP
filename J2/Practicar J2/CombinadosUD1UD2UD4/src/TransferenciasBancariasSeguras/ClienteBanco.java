package TransferenciasBancariasSeguras;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteBanco {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Cuenta origen: ");
            String origen = sc.nextLine();
            System.out.print("Cuenta destino: ");
            String destino = sc.nextLine();
            System.out.print("Importe: ");
            double importe = Double.parseDouble(sc.nextLine());

            String textoOriginal = origen + destino + importe;
            String hmacEnviado = UtilHMAC.calcularHMAC(textoOriginal);

            DatosTransferencia datos = new DatosTransferencia(origen, destino, importe, hmacEnviado);

            Socket socket = new Socket("localhost", 7400);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(datos);
            System.out.println("Transferencia enviada, esperando confirmacion...");

            RespuestaTransferencia respuesta = (RespuestaTransferencia) in.readObject();
            String hmacComprobado = UtilHMAC.calcularHMAC(respuesta.getMensaje());

            if (hmacComprobado.equals(respuesta.getHmacRespuesta())) {
                System.out.println(respuesta.getMensaje());
            } else {
                System.out.println("La respuesta del servidor no es de confianza (HMAC no coincide)");
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}