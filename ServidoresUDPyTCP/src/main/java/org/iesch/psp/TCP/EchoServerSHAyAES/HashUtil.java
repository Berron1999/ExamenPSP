package org.iesch.psp.TCP.EchoServerSHAyAES;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashUtil {

    // Método para generar el Hash SHA-256 de un texto [cite: 136]
    public static String getHash(String msg) {
        try {
            // 1. Convertimos el texto a bytes [cite: 138, 140]
            byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_16LE);

            // 2. Preparamos el algoritmo SHA-256 [cite: 141]
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 3. Calculamos la huella digital [cite: 143]
            byte[] hashValue = digest.digest(msgBytes);

            // 4. Lo convertimos a Base64 (texto legible) para poder enviarlo [cite: 144]
            return Base64.getEncoder().encodeToString(hashValue);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}