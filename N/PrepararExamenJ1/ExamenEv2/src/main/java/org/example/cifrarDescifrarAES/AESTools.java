package org.example.cifrarDescifrarAES;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Clase de herramientas para cifrar y descifrar usando AES.
 */
public class AESTools {

    private static final String HASH_ALGORITHM = "SHA3-256";
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";

    /**
     * Genera el hash SHA3-256 a partir de un texto.
     *
     * @param texto texto usado para generar el hash
     * @return hash generado en bytes
     * @throws Exception si el algoritmo no está disponible
     */
    public static byte[] generarHashClave(String texto) throws Exception {

        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);

        return digest.digest(texto.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera una clave AES a partir del hash recibido.
     *
     * SHA3-256 genera 32 bytes, por lo que se usa como clave AES-256.
     *
     * @param hash hash usado como clave
     * @return clave AES
     */
    public static SecretKeySpec generarClaveAES(byte[] hash) {

        return new SecretKeySpec(hash, AES_ALGORITHM);
    }

    /**
     * Cifra datos usando AES/ECB/PKCS5Padding.
     *
     * @param datos datos en claro
     * @param clave clave AES
     * @return datos cifrados
     * @throws Exception si ocurre un error al cifrar
     */
    public static byte[] cifrar(byte[] datos, SecretKeySpec clave) throws Exception {

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);

        cipher.init(Cipher.ENCRYPT_MODE, clave);

        return cipher.doFinal(datos);
    }

    /**
     * Descifra datos usando AES/ECB/PKCS5Padding.
     *
     * @param datosCifrados datos cifrados
     * @param clave clave AES
     * @return datos descifrados
     * @throws Exception si ocurre un error al descifrar
     */
    public static byte[] descifrar(byte[] datosCifrados, SecretKeySpec clave) throws Exception {

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);

        cipher.init(Cipher.DECRYPT_MODE, clave);

        return cipher.doFinal(datosCifrados);
    }

    /**
     * Convierte un array de bytes a texto hexadecimal.
     *
     * @param bytes bytes a convertir
     * @return cadena hexadecimal
     */
    public static String bytesToHex(byte[] bytes) {

        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }

        return sb.toString();
    }
}