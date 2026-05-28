package org.example.cifradoDescifradoAES;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

/**
 * Clase de herramientas para realizar operaciones de cifrado y descifrado AES.
 *
 * También incluye métodos auxiliares para generar una clave a partir de un hash
 * y para convertir arrays de bytes a formato hexadecimal.
 */
public class AESTools {

    private static final String HASH_ALGORITHM = "SHA3-256";
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";

    /**
     * Genera un hash SHA3-256 a partir de un texto.
     *
     * En este ejercicio se utilizará el nombre del alumno para generar
     * la clave simétrica de cifrado.
     *
     * @param texto texto usado para generar el hash
     * @return array de bytes con el hash SHA3-256
     * @throws Exception si el algoritmo no está disponible
     */
    public static byte[] generarHashClave(String texto) throws Exception {

        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);

        return digest.digest(texto.getBytes("UTF-8"));
    }

    /**
     * Genera una clave AES a partir del hash recibido.
     *
     * SHA3-256 genera 32 bytes, por lo que sirve como clave AES de 256 bits.
     *
     * @param hash hash usado como clave
     * @return clave AES
     */
    public static SecretKeySpec generarClaveAES(byte[] hash) {

        return new SecretKeySpec(hash, AES_ALGORITHM);
    }

    /**
     * Cifra un array de bytes utilizando AES/ECB/PKCS5Padding.
     *
     * @param datos datos en claro
     * @param clave clave AES
     * @return datos cifrados
     * @throws Exception si ocurre un error durante el cifrado
     */
    public static byte[] cifrar(byte[] datos, SecretKeySpec clave) throws Exception {

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);

        cipher.init(Cipher.ENCRYPT_MODE, clave);

        return cipher.doFinal(datos);
    }

    /**
     * Descifra un array de bytes utilizando AES/ECB/PKCS5Padding.
     *
     * @param datosCifrados datos cifrados
     * @param clave clave AES
     * @return datos descifrados
     * @throws Exception si ocurre un error durante el descifrado
     */
    public static byte[] descifrar(byte[] datosCifrados, SecretKeySpec clave) throws Exception {

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);

        cipher.init(Cipher.DECRYPT_MODE, clave);

        return cipher.doFinal(datosCifrados);
    }

    /**
     * Convierte un array de bytes a una cadena hexadecimal.
     *
     * Esto sirve para mostrar por pantalla el hash y el texto cifrado
     * de una forma legible.
     *
     * @param bytes array de bytes a convertir
     * @return representación hexadecimal
     */
    public static String bytesToHex(byte[] bytes) {

        StringBuilder resultado = new StringBuilder();

        for (byte b : bytes) {
            resultado.append(String.format("%02X", b));
        }

        return resultado.toString();
    }
}
