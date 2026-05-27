package org.iesch.psp.AlgoritmosDeEncriptacion.PBKDF2;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

// PBKDF2 → equivalente Java a Rfc2898DeriveBytes de los apuntes
// Guardar contraseñas de forma segura con sal
// NUNCA guardar contraseñas en claro en base de datos
public class EjercicioPBKDF2 {

    // Parámetros recomendados en los apuntes
    private static final int ITERACIONES = 310000; // más iteraciones = más seguro
    private static final int BITS_HASH   = 256;    // longitud del hash resultante
    private static final int BYTES_SAL   = 32;     // sal de 32 bytes (igual que SHA256)

    public static void main(String[] args) throws Exception {

        // Simulamos registro de usuario
        String usuario    = "mario";
        String contrasena = "miContraseñaSegura123";

        System.out.println("[PBKDF2] Registrando usuario: " + usuario);

        // 1. Generamos sal aleatoria para este usuario (nunca reutilizar)
        byte[] sal = generarSal();
        System.out.println("[PBKDF2] Sal generada: " + Base64.getEncoder().encodeToString(sal));

        // 2. Generamos el hash de la contraseña + sal
        String hash = hashContrasena(contrasena, sal);
        System.out.println("[PBKDF2] Hash guardado: " + hash);

        // En base de datos guardaríamos: usuario, sal (en Base64), hash
        System.out.println("\n[PBKDF2] Intentando login con contraseña CORRECTA...");
        boolean loginOk = verificarContrasena(contrasena, sal, hash);
        System.out.println("[PBKDF2] Login correcto: " + loginOk);

        System.out.println("\n[PBKDF2] Intentando login con contraseña INCORRECTA...");
        boolean loginFail = verificarContrasena("contraseñaIncorrecta", sal, hash);
        System.out.println("[PBKDF2] Login correcto: " + loginFail);
    }

    // Genera una sal aleatoria de 32 bytes (igual que recomienda los apuntes)
    private static byte[] generarSal() {
        byte[] sal = new byte[BYTES_SAL];
        new SecureRandom().nextBytes(sal);
        return sal;
    }

    // Genera el hash PBKDF2 de la contraseña con la sal
    // Equivalente a Rfc2898DeriveBytes de los apuntes
    private static String hashContrasena(String contrasena, byte[] sal) throws Exception {
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(
                contrasena.toCharArray(), // contraseña como array de chars
                sal,                      // sal aleatoria
                ITERACIONES,              // número de iteraciones
                BITS_HASH                 // tamaño del hash en bits
        );
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    // Verifica si la contraseña introducida coincide con el hash guardado
    private static boolean verificarContrasena(String contrasena, byte[] sal,
                                               String hashGuardado) throws Exception {
        // Recalculamos el hash con la misma sal y comparamos
        return hashContrasena(contrasena, sal).equals(hashGuardado);
    }
}