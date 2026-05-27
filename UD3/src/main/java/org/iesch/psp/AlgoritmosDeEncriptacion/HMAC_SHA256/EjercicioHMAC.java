package org.iesch.psp.AlgoritmosDeEncriptacion.HMAC_SHA256;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

// HMAC garantiza que un mensaje no fue alterado
// Si el mensaje cambia, el HMAC cambia → detectamos la manipulación
public class EjercicioHMAC {

    private static final byte[] KEY_HMAC = "claveSecretaHMAC1234".getBytes();

    public static void main(String[] args) throws Exception {

        String mensaje = "Mensaje importante que no debe ser alterado";

        // Generamos HMAC con SHA-256 y SHA-512
        String hmac256 = generarHMAC(mensaje, "HmacSHA256");
        String hmac512 = generarHMAC(mensaje, "HmacSHA512");

        System.out.println("[HMAC] Mensaje original: " + mensaje);
        System.out.println("[HMAC] HMACSHA256: " + hmac256);
        System.out.println("[HMAC] HMACSHA512: " + hmac512);

        // Verificamos que el mensaje no fue alterado
        System.out.println("\n[HMAC] Verificando mensaje original...");
        System.out.println("  SHA256 OK: " + verificarHMAC(mensaje, hmac256, "HmacSHA256"));
        System.out.println("  SHA512 OK: " + verificarHMAC(mensaje, hmac512, "HmacSHA512"));

        // Simulamos un mensaje alterado
        String mensajeAlterado = "Mensaje importante que SI fue alterado";
        System.out.println("\n[HMAC] Verificando mensaje ALTERADO...");
        System.out.println("  SHA256 OK: " + verificarHMAC(mensajeAlterado, hmac256, "HmacSHA256"));
        System.out.println("  SHA512 OK: " + verificarHMAC(mensajeAlterado, hmac512, "HmacSHA512"));
    }

    // Genera un código HMAC del mensaje con el algoritmo indicado
    private static String generarHMAC(String mensaje, String algoritmo) throws Exception {
        Mac mac = Mac.getInstance(algoritmo);
        mac.init(new SecretKeySpec(KEY_HMAC, algoritmo));
        byte[] resultado = mac.doFinal(mensaje.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(resultado);
    }

    // Verifica si el HMAC del mensaje coincide con el original
    private static boolean verificarHMAC(String mensaje, String hmacOriginal,
                                         String algoritmo) throws Exception {
        return generarHMAC(mensaje, algoritmo).equals(hmacOriginal);
    }
}