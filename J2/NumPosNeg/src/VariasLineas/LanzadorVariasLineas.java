package VariasLineas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

public class LanzadorVariasLineas {
    public static void main(String[] args) {

        // Ruta del fichero de entrada con ruta completa de Windows
        String rutaNumeros = "Ficheros/NumerosVariasLineas.txt";

        // Obtenemos el classpath para que el subproceso encuentre la clase
        String classPath = System.getProperty("java.class.path");

        try {
            // --- Lanzamos el proceso que clasifica los POSITIVOS ---
            ProcessBuilder pbPos = new ProcessBuilder(
                    "java", "-cp", classPath, "VariasLineas.ClasificadorVariasLineas", rutaNumeros, "POS"
            ).redirectOutput(Redirect.INHERIT)
                    .redirectError(Redirect.INHERIT); // Vemos también los errores del subproceso
            Process procPos = pbPos.start();

            // --- Lanzamos el proceso que clasifica los NEGATIVOS ---
            ProcessBuilder pbNega = new ProcessBuilder(
                    "java", "-cp", classPath, "VariasLineas.ClasificadorVariasLineas", rutaNumeros, "NEGA"
            ).redirectOutput(Redirect.INHERIT)
                    .redirectError(Redirect.INHERIT);
            Process procNega = pbNega.start();

            // Esperamos a que AMBOS procesos terminen antes de continuar
            procPos.waitFor();
            procNega.waitFor();

            System.out.println("\n--- Ambos procesos han terminado ---\n");

            // --- Leemos y mostramos PositivosVariasLineas.txt ---
            System.out.println("=== POSITIVOS ===");
            mostrarFichero("Ficheros/PositivosVariasLineas.txt");


            // --- Leemos y mostramos NegativosVariasLineas.txt ---
            System.out.println("=== NEGATIVOS/CERO ===");
            mostrarFichero("Ficheros/NegativosVariasLineas.txt");


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar que lee un fichero, muestra su contenido y la cantidad de números
    public static void mostrarFichero(String ruta) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(ruta));
            String linea;
            int cantidad = 0;

            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
                cantidad++;
            }
            br.close();

            System.out.println("Total: " + cantidad + " números\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}