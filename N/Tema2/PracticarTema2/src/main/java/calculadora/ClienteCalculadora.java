package calculadora;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteCalculadora {
    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 5000);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);

            Scanner scanner = new Scanner(System.in);
            String linea;
            do {
                System.out.printf("Introduce operacion ");
                linea= scanner.nextLine();
                pw.println(linea);

                if (!linea.equalsIgnoreCase("fin")){
                    System.out.printf("Servidor "+br.readLine());
                    System.out.println();
                }



            }while (!linea.equalsIgnoreCase("fin"));
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
