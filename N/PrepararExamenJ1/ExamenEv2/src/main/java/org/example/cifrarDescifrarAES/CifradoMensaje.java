package org.example.cifrarDescifrarAES;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Clase ejecutable encargada de cifrar un mensaje
 * usando AES/ECB/PKCS5Padding.
 */
public class CifradoMensaje {

    private static final String NOMBRE_COMPLETO = "Pablo Navarro";
    private static final String TEXTO_CLARO = NOMBRE_COMPLETO + " Examen PSP";

    private static final String CARPETA = "archivos";
    private static final String FICHERO = "mensaje_cifrado.txt";

    public static void main(String[] args) {

        try {
            /*
             * Creamos la carpeta archivos si no existe.
             */
            File carpeta = new File(CARPETA);

            if (!carpeta.exists()) {
                carpeta.mkdir();
            }

            /*
             * Generamos el hash SHA3-256 del nombre completo.
             * Este hash será la clave simétrica.
             */
            byte[] hashClave = AESTools.generarHashClave(NOMBRE_COMPLETO);

            /*
             * Creamos la clave AES a partir del hash.
             */
            SecretKeySpec claveAES = AESTools.generarClaveAES(hashClave);

            /*
             * Convertimos el texto claro a bytes.
             */
            byte[] datosEnClaro = TEXTO_CLARO.getBytes(StandardCharsets.UTF_8);

            /*
             * Ciframos el contenido.
             */
            byte[] datosCifrados = AESTools.cifrar(datosEnClaro, claveAES);

            /*
             * Guardamos el contenido cifrado en el fichero.
             * Importante: guardamos bytes, no texto normal.
             */
            Path rutaFichero = Paths.get(CARPETA, FICHERO);

            Files.write(rutaFichero, datosCifrados);

            /*
             * Mostramos la información solicitada.
             */
            System.out.println("Hash de la clave de cifrado en hexadecimal:");
            System.out.println(AESTools.bytesToHex(hashClave));

            System.out.println();

            System.out.println("Contenido cifrado en hexadecimal:");
            System.out.println(AESTools.bytesToHex(datosCifrados));

            System.out.println();

            System.out.println("Fichero cifrado correctamente.");
            System.out.println("Ruta: " + rutaFichero.toAbsolutePath());

        } catch (Exception e) {
            System.out.println("Error al cifrar el mensaje.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
