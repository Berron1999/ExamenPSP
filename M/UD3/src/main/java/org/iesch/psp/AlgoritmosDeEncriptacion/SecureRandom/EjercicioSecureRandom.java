package org.iesch.psp.AlgoritmosDeEncriptacion.SecureRandom;

import java.security.SecureRandom;
import java.util.Base64;

// SecureRandom → equivalente Java a RNGCryptoServiceProvider de los apuntes
// Se usa para generar sales, IVs, tokens, claves temporales...
public class EjercicioSecureRandom {

    public static void main(String[] args) {

        SecureRandom random = new SecureRandom();

        // Generamos una sal de 32 bytes (recomendado en los apuntes)
        byte[] sal = new byte[32];
        random.nextBytes(sal);
        System.out.println("[SecureRandom] Sal (32 bytes): " +
                Base64.getEncoder().encodeToString(sal));

        // Generamos un IV de 16 bytes para AES
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        System.out.println("[SecureRandom] IV  (16 bytes): " +
                Base64.getEncoder().encodeToString(iv));

        // Generamos un token aleatorio de 24 bytes (para sesiones, tokens...)
        byte[] token = new byte[24];
        random.nextBytes(token);
        System.out.println("[SecureRandom] Token de sesión: " +
                Base64.getEncoder().encodeToString(token));

        // Cada vez que se ejecuta los valores son distintos → verdaderamente aleatorio
        System.out.println("\n[SecureRandom] Cada ejecución genera valores distintos:");
        for (int i = 1; i <= 3; i++) {
            byte[] aleatorio = new byte[8];
            random.nextBytes(aleatorio);
            System.out.println("  #" + i + ": " + Base64.getEncoder().encodeToString(aleatorio));
        }
    }
}