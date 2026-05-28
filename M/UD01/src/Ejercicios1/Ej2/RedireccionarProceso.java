package Ejercicios1.Ej2;

import java.io.File;
import java.io.IOException;

public class RedireccionarProceso {

    public static void main(String[] args) {

        // Ficheros de entrada, salida y error
        File ficheroEntrada = new File(System.getProperty("user.home") + "\\Documents\\comandos.bat");
        File ficheroSalida  = new File(System.getProperty("user.home") + "\\Documents\\log_salida.txt");
        File ficheroError   = new File(System.getProperty("user.home") + "\\Documents\\log_errores.txt");

        // Creamos el proceso cmd.exe que ejecutará el .bat
        // /c → ejecuta el comando y cierra la consola al terminar
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", ficheroEntrada.getAbsolutePath());

        // Redirigimos entrada, salida y errores a sus ficheros correspondientes
        pb.redirectInput(ficheroEntrada);   // comandos desde el .bat
        pb.redirectOutput(ficheroSalida);   // resultados correctos al log de salida
        pb.redirectError(ficheroError);     // errores al log de errores

        try {
            Process proceso = pb.start();
            System.out.println("Proceso lanzado. PID: " + proceso.pid());

            // Esperamos a que cmd termine todos los comandos
            int codigoSalida = proceso.waitFor();
            System.out.println("Proceso finalizado. Código de salida: " + codigoSalida);
            System.out.println("Salida → " + ficheroSalida.getAbsolutePath());
            System.out.println("Errores → " + ficheroError.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}