package org.example.cifradoDescifradoAES;

import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Clase ejecutable encargada de leer el fichero cifrado,
 * descifrarlo y mostrar el contenido original por pantalla.
 */
public class Descifrado {

    private static final String NOMBRE_COMPLETO = "Pablo Navarro";
    private static final String CARPETA = "archivos";
    private static final String FICHERO = "nombre_cifrado.txt";

    public static void main(String[] args) {

        try {
            /*
             * Generamos otra vez el hash SHA3-256 del nombre.
             * Debe ser el mismo texto usado en Cifrado.java,
             * porque si cambia el texto, cambia la clave.
             */
            byte[] hashClave = AESTools.generarHashClave(NOMBRE_COMPLETO);

            /*
             * Creamos la misma clave AES a partir del hash.
             */
            SecretKeySpec claveAES = AESTools.generarClaveAES(hashClave);

            /*
             * Obtenemos la ruta del fichero cifrado.
             */
            Path rutaFichero = Paths.get(CARPETA, FICHERO);

            /*
             * Leemos todos los bytes cifrados del fichero.
             */
            byte[] textoCifrado = Files.readAllBytes(rutaFichero);

            /*
             * Desciframos los bytes leídos.
             */
            byte[] textoDescifrado = AESTools.descifrar(textoCifrado, claveAES);

            /*
             * Convertimos el texto descifrado a String.
             */
            String textoClaro = new String(textoDescifrado, "UTF-8");

            /*
             * Mostramos por pantalla la información solicitada.
             */
            System.out.println("Hash de la clave de cifrado en hexadecimal:");
            System.out.println(AESTools.bytesToHex(hashClave));

            System.out.println();

            System.out.println("Texto cifrado leído del fichero en hexadecimal:");
            System.out.println(AESTools.bytesToHex(textoCifrado));

            System.out.println();

            System.out.println("Texto descifrado:");
            System.out.println(textoClaro);

            System.out.println();

            System.out.println("Texto descifrado en hexadecimal:");
            System.out.println(AESTools.bytesToHex(textoDescifrado));

            System.out.println();

            System.out.println("Fichero descifrado correctamente.");

        } catch (Exception e) {
            System.out.println("Error durante el descifrado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}