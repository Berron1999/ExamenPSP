package org.example.cifradoContraseñaHashSalt;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * Clase de herramientas para trabajar con contraseñas usando hash + salt.
 *
 * IMPORTANTE:
 * Una contraseña con hash no se descifra.
 * Se verifica recalculando el hash con la misma salt.
 */
public class PasswordTools {

    private static final int SALT_LENGTH = 32;
    private static final int HASH_LENGTH = 32;
    private static final int KEY_LENGTH_BITS = 256;
    private static final int ITERATIONS = 10000;

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    /**
     * Genera una salt aleatoria de 32 bytes.
     *
     * @return salt generada
     */
    public static byte[] generateSalt() {

        byte[] salt = new byte[SALT_LENGTH];

        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        return salt;
    }

    /**
     * Genera el hash de una contraseña usando PBKDF2WithHmacSHA256.
     *
     * @param password contraseña en claro introducida por el usuario
     * @param salt salt aleatoria
     * @return hash generado
     */
    public static byte[] generateHash(String password, byte[] salt) {

        try {
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    ITERATIONS,
                    KEY_LENGTH_BITS
            );

            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);

            return factory.generateSecret(spec).getEncoded();

        } catch (Exception e) {
            throw new RuntimeException("Error generando el hash de la contraseña", e);
        }
    }

    /**
     * Une salt + hash en un único array de bytes.
     *
     * @param salt salt generada
     * @param hash hash generado
     * @return array con salt + hash
     */
    public static byte[] joinSaltAndHash(byte[] salt, byte[] hash) {

        byte[] result = new byte[salt.length + hash.length];

        System.arraycopy(salt, 0, result, 0, salt.length);
        System.arraycopy(hash, 0, result, salt.length, hash.length);

        return result;
    }

    /**
     * Crea el hash completo de una contraseña.
     *
     * Genera una salt, calcula el hash y devuelve salt + hash en Base64.
     *
     * @param password contraseña en claro
     * @return salt + hash codificados en Base64
     */
    public static String createPasswordHash(String password) {

        byte[] salt = generateSalt();

        byte[] hash = generateHash(password, salt);

        byte[] saltAndHash = joinSaltAndHash(salt, hash);

        return Base64.getEncoder().encodeToString(saltAndHash);
    }

    /**
     * Verifica si una contraseña coincide con un hash almacenado.
     *
     * @param password contraseña introducida por el usuario
     * @param storedPasswordHash salt + hash almacenados en Base64
     * @return true si la contraseña es correcta, false si no lo es
     */
    public static boolean verifyPassword(String password, String storedPasswordHash) {

        byte[] saltAndHash = Base64.getDecoder().decode(storedPasswordHash);

        byte[] salt = Arrays.copyOfRange(saltAndHash, 0, SALT_LENGTH);

        byte[] storedHash = Arrays.copyOfRange(
                saltAndHash,
                SALT_LENGTH,
                SALT_LENGTH + HASH_LENGTH
        );

        byte[] calculatedHash = generateHash(password, salt);

        return Arrays.equals(storedHash, calculatedHash);
    }
}