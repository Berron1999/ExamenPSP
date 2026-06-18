package VariasLineas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ClasificadorVariasLineas {
    public static void main(String[] args) {

        String ruta = args[0];  // Ruta de NumerosVariasLineas.txt
        String tipo = args[1];  // "POS" o "NEGA"

        ArrayList<Integer> numeros = new ArrayList<>();

        try {
            // Abrimos el fichero para leerlo línea a línea
            BufferedReader br = new BufferedReader(new FileReader(ruta));
            String linea;

            // Leemos cada línea hasta que no haya más
            while ((linea = br.readLine()) != null) {

                // Quitamos la coma del final si la tiene, y espacios
                linea = linea.replace(",", "").trim();

                // Si la línea está vacía la saltamos
                if (linea.isEmpty()) continue;

                // Convertimos el texto a número entero
                int numero = Integer.parseInt(linea);

                // Clasificamos según el tipo recibido
                if (tipo.equals("POS") && numero > 0) {
                    numeros.add(numero); // Solo mayores que 0
                } else if (tipo.equals("NEGA") && numero <= 0) {
                    numeros.add(numero); // Menores o iguales a 0
                }
            }
            br.close();

            // Determinamos el fichero de salida según el tipo
            String rutaSalida = "Ficheros/" + (tipo.equals("POS") ? "PositivosVariasLineas.txt" : "NegativosVariasLineas.txt");
            // Escribimos los números clasificados en el fichero de salida
            PrintWriter pw = new PrintWriter(new FileWriter(rutaSalida));
            for (int num : numeros) {
                pw.println(num);
            }
            pw.close();

            System.out.println("VariasLineas.ClasificadorVariasLineas " + tipo + " terminado. Guardados " + numeros.size() + " números.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}