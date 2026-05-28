package org.example.cifradoDescifradoAES;

import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Clase ejecutable encargada de cifrar el nombre completo del alumno
 * y guardar el resultado cifrado en un fichero.
 */
public class Cifrado {

    private static final String NOMBRE_COMPLETO = "Pablo Navarro";
    private static final String CARPETA = "./archivos";
    private static final String FICHERO = "nombre_cifrado.txt";

    public static void main(String[] args) {

        try {
            /*
             * Creamos la carpeta archivos en la raíz del proyecto
             * si todavía no existe.
             */
            File carpeta = new File(CARPETA);

            if (!carpeta.exists()) {
                carpeta.mkdir();
            }

            /*
             * Generamos el hash SHA3-256 del nombre.
             * Este hash se usará como clave de cifrado AES.
             */
            byte[] hashClave = AESTools.generarHashClave(NOMBRE_COMPLETO);

            /*
             * Creamos la clave AES usando el hash anterior.
             */
            SecretKeySpec claveAES = AESTools.generarClaveAES(hashClave);

            /*
             * Convertimos el texto en claro a bytes.
             */
            byte[] textoClaro = NOMBRE_COMPLETO.getBytes("UTF-8");

            /*
             * Ciframos el texto usando AES/ECB/PKCS5Padding.
             */
            byte[] textoCifrado = AESTools.cifrar(textoClaro, claveAES);

            /*
             * Guardamos el texto cifrado en el fichero indicado.
             */
            Path rutaFichero = Paths.get(CARPETA, FICHERO);

            Files.write(rutaFichero, textoCifrado);

            /*
             * Mostramos por pantalla la información solicitada.
             */
            System.out.println("Hash de la clave de cifrado en hexadecimal:");
            System.out.println(AESTools.bytesToHex(hashClave));

            System.out.println();

            System.out.println("Texto cifrado en hexadecimal:");
            System.out.println(AESTools.bytesToHex(textoCifrado));

            System.out.println();

            System.out.println("Fichero cifrado correctamente.");
            System.out.println("Ruta del fichero: " + rutaFichero.toAbsolutePath());

        } catch (Exception e) {
            System.out.println("Error durante el cifrado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}