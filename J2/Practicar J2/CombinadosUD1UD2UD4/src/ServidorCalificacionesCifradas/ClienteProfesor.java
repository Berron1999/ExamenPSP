package ServidorCalificacionesCifradas;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteProfesor {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Nombre del alumno: ");
            String alumno = sc.nextLine();
            System.out.print("Nota: ");
            double nota = Double.parseDouble(sc.nextLine());

            DatosNota datos = new DatosNota(alumno, nota);

            Socket socket = new Socket("localhost", 7200);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(datos);

            RespuestaCifrada respuesta = (RespuestaCifrada) in.readObject();
            String textoDescifrado = UtilSeguridad.descifrar(respuesta.getDatosCifrados(), respuesta.getIv());
            String hashCalculado = UtilSeguridad.calcularSHA256(textoDescifrado);

            System.out.println("Mensaje descifrado: " + textoDescifrado);

            if (hashCalculado.equals(respuesta.getHashIntegridad())) {
                System.out.println("Integridad correcta");
            } else {
                System.out.println("¡Integridad comprometida!");
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}