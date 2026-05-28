package eco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClienteEco {
    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost",5000);


            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);

            Scanner scanner = new Scanner(System.in);
            String texto;

            do {
                System.out.print("Escribe: ");
                texto = scanner.nextLine();

                pw.println(texto);

                if (!texto.equalsIgnoreCase("fin")){
                    System.out.println("Servidor dice: "+ br.readLine());
                }

            }while (!texto.equalsIgnoreCase("fin"));
            socket.close();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
