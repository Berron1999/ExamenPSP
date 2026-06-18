package UnaLinea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ClasificadorUnaLinea {
    public static void main(String[] args) {

        String ruta = args[0];  // Ruta de Numeros.txt
        String tipo = args[1];  // "POS" o "NEGA"

        ArrayList<Integer> numeros = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(ruta));

            // Leemos la única línea: "15, -4, 87, -91, 33, -2, 0, -45, 66"
            String linea = br.readLine();
            br.close();

            // Separamos por coma
            String[] partes = linea.split(",");

            for (String parte : partes) {
                int numero = Integer.parseInt(parte.trim());

                if (tipo.equals("POS") && numero > 0) {
                    numeros.add(numero);
                } else if (tipo.equals("NEGA") && numero <= 0) {
                    numeros.add(numero);
                }
            }

            // Determinamos el fichero de salida según el tipo
            String rutaSalida = "Ficheros/" + (tipo.equals("POS") ? "PositivosUnaLinea.txt" : "NegativosUnaLinea.txt");

            // Escribimos los números clasificados en el fichero de salida
            PrintWriter pw = new PrintWriter(new FileWriter(rutaSalida));
            for (int num : numeros) {
                pw.println(num);
            }
            pw.close();

            System.out.println("ClasificadorUnaLinea " + tipo + " terminado. Guardados " + numeros.size() + " números.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}