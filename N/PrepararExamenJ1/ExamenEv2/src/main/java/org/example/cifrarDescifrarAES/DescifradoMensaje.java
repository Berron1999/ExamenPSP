package org.example.cifrarDescifrarAES;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Clase ejecutable encargada de descifrar el fichero
 * mensaje_cifrado.txt usando AES/ECB/PKCS5Padding.
 */
public class DescifradoMensaje {

    private static final String NOMBRE_COMPLETO = "Pablo Navarro";

    private static final String CARPETA = "archivos";
    private static final String FICHERO = "mensaje_cifrado.txt";

    public static void main(String[] args) {

        try {
            /*
             * Generamos de nuevo el hash SHA3-256 del nombre completo.
             * Debe ser exactamente el mismo nombre usado para cifrar.
             */
            byte[] hashClave = AESTools.generarHashClave(NOMBRE_COMPLETO);

            /*
             * Creamos la clave AES a partir del hash.
             */
            SecretKeySpec claveAES = AESTools.generarClaveAES(hashClave);

            /*
             * Leemos los bytes cifrados del fichero.
             */
            Path rutaFichero = Paths.get(CARPETA, FICHERO);

            byte[] datosCifrados = Files.readAllBytes(rutaFichero);

            /*
             * Desciframos el contenido.
             */
            byte[] datosDescifrados = AESTools.descifrar(datosCifrados, claveAES);

            /*
             * Convertimos los bytes descifrados a texto.
             */
            String textoDescifrado = new String(datosDescifrados, StandardCharsets.UTF_8);

            /*
             * Mostramos la información solicitada.
             */
            System.out.println("Hash de la clave de cifrado en hexadecimal:");
            System.out.println(AESTools.bytesToHex(hashClave));

            System.out.println();

            System.out.println("Contenido cifrado en hexadecimal:");
            System.out.println(AESTools.bytesToHex(datosCifrados));

            System.out.println();

            System.out.println("Texto descifrado:");
            System.out.println(textoDescifrado);

            System.out.println();

            System.out.println("Fichero descifrado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al descifrar el mensaje.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
