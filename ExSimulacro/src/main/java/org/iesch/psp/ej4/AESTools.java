package org.iesch.psp.ej4;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESTools {
    private static final String ALGORITMO = "AES";

    public static SecretKey generarClaveDesdeNombre(String nombre) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha256.digest(nombre.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(hash, ALGORITMO);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }

    public static String cifrar(String texto, SecretKey clave) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, clave);
            byte[] cifrado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cifrado);
        } catch (Exception e) {
            throw new IllegalStateException("Error al cifrar", e);
        }
    }

    public static String descifrar(String textoBase64, SecretKey clave) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, clave);
            byte[] bytes = Base64.getDecoder().decode(textoBase64);
            byte[] descifrado = cipher.doFinal(bytes);
            return new String(descifrado, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Error al descifrar", e);
        }
    }
}
