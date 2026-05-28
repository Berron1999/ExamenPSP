package org.iesch.psp.TCP.EchoServerSHAyAES;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {

    private static final byte[] KEY = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16 };
    private static final byte[] IV = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16 };

    // Método para cifrar un texto plano y devolverlo en Base64
    public static String encriptar(String textoPlano) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(KEY, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        // Ciframos los bytes del texto
        byte[] bytesCifrados = cipher.doFinal(textoPlano.getBytes(StandardCharsets.UTF_8));

        // Lo convertimos a Base64 para que se pueda enviar como un String normal por el PrintWriter
        return Base64.getEncoder().encodeToString(bytesCifrados);
    }

    // Método para recibir Base64, descifrarlo y devolver el texto plano
    public static String desencriptar(String textoCifradoBase64) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(KEY, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        // Deshacemos el Base64 y lo desciframos
        byte[] bytesCifrados = Base64.getDecoder().decode(textoCifradoBase64);
        byte[] bytesDescifrados = cipher.doFinal(bytesCifrados);

        return new String(bytesDescifrados, StandardCharsets.UTF_8);
    }
}