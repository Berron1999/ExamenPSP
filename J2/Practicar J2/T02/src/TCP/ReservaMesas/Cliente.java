package TCP.ReservaMesas;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Socket socket = new Socket("localhost", 6500);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            System.out.print("Numero de mesa a reservar (1-5): ");
            int numeroMesa = Integer.parseInt(sc.nextLine());

            DatosReserva datos = new DatosReserva(numeroMesa);
            out.writeObject(datos);

            RespuestaReserva respuesta = (RespuestaReserva) in.readObject();
            System.out.println(respuesta);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}