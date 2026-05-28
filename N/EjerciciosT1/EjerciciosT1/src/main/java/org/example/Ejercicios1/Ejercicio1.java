package org.example.Ejercicios1;

import java.io.File;
import java.io.IOException;

public class Ejercicio1 {
    public static void main(String[] args) {
        /*
        * 1. Crea un archivo de texto con la carpeta Documentos de tu ordenador. Crea un
programa en java que lance un proceso para abrir este fichero con
Notepad++.*/

        String carpeta = "C:\\Users\\dam2\\Documents";
        File carpetaDocumentos = new File(carpeta);

        if (carpetaDocumentos.isDirectory()){
            File[] documentos = carpetaDocumentos.listFiles();

            for (File archivo: documentos){
                if (archivo.isFile() && archivo.getName().endsWith(".txt")){
                    System.out.println(archivo.getPath());
                    ProcessBuilder proceso = new ProcessBuilder("C:\\Program Files\\Notepad++\\Notepad++.exe",archivo.getPath());

                    try {
                        proceso.start();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }




    }
}
