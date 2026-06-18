package TCP.ValidacionProductos;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Socket socket = new Socket("localhost", 6000);

            // mismo orden que en el servidor: out antes que in
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            System.out.print("Codigo del producto: ");
            String codigo = sc.nextLine();
            System.out.print("Precio del producto: ");
            double precio = Double.parseDouble(sc.nextLine());

            DatosProducto datos = new DatosProducto(codigo, precio);
            out.writeObject(datos);

            RespuestaValidacion respuesta = (RespuestaValidacion) in.readObject();
            System.out.println("Respuesta del servidor: " + respuesta);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}