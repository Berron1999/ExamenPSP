package VotacionElectronicaSincronizada;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteVotante {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Tu voto (A, B o C): ");
            String opcion = sc.nextLine().toUpperCase();

            UtilVoto.ResultadoCifrado resultado = UtilVoto.cifrar(opcion);
            DatosVoto datos = new DatosVoto(resultado.datos, resultado.iv);

            Socket socket = new Socket("localhost", 7800);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(datos);
            System.out.println("Voto enviado, esperando al resto de votantes...");

            RespuestaVotacion respuesta = (RespuestaVotacion) in.readObject();
            System.out.println("Resultado de la ronda: " + respuesta.getResumen());

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}