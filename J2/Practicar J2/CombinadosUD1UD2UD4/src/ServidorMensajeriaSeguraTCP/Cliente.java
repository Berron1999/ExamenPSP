package ServidorMensajeriaSeguraTCP;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Mensaje a enviar: ");
            String texto = sc.nextLine();

            // el hash se calcula SOBRE EL TEXTO EN CLARO, antes de cifrar
            String hash = UtilCifrado.calcularSHA256(texto);
            UtilCifrado.ResultadoCifrado resultado = UtilCifrado.cifrar(texto);

            MensajeCifrado mensaje = new MensajeCifrado(resultado.datos, resultado.iv, hash);

            Socket socket = new Socket("localhost", 7000);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(mensaje);

            String respuesta = (String) in.readObject();
            System.out.println("Respuesta del servidor: " + respuesta);

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}