package Ejercicios1.Ej1;

import java.io.IOException;

public class AbrirFicheroNotepadPlusPlus {

    public static void main(String[] args) {

        // Ruta del archivo que queremos abrir
        // Cambia "mi_archivo.txt" por el nombre real de tu fichero
        String rutaFichero = System.getProperty("user.home") + "\\Documents\\mi_archivo.txt";

        // Ruta de instalación de Notepad++ (ruta típica en Windows 64 bits)
        // Cámbiala si tu Notepad++ está instalado en otra carpeta
        String rutaNotepad = "C:\\Program Files\\Notepad++\\notepad++.exe";

        try {
            // ProcessBuilder permite crear y lanzar un proceso del sistema operativo
            // Le pasamos como argumentos: el programa a ejecutar y el archivo que queremos abrir
            ProcessBuilder pb = new ProcessBuilder(rutaNotepad, rutaFichero);

            // Iniciamos el proceso → esto abre el Notepad++ con el fichero
            Process proceso = pb.start();

            // Mostramos el PID (identificador único) del proceso que acabamos de lanzar
            System.out.println("Notepad++ abierto correctamente. PID del proceso: " + proceso.pid());

            // waitFor() hace que el programa Java espere a que el Notepad++ se cierre
            // Si no lo ponemos, Java termina pero Notepad++ sigue abierto (eso también es válido)
            proceso.waitFor();

            System.out.println("El proceso de Notepad++ ha finalizado.");

        } catch (IOException e) {
            // Se lanza si no se encuentra el ejecutable o el fichero
            System.out.println("Error al lanzar el proceso: " + e.getMessage());
            e.printStackTrace();

        } catch (InterruptedException e) {
            // Se lanza si el hilo que espera es interrumpido
            System.out.println("El proceso fue interrumpido: " + e.getMessage());
            e.printStackTrace();
        }
    }
}