package EchoServerExamenSecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/*
 * ============================================================================
 *  CifradorSecureRandom
 * ----------------------------------------------------------------------------
 *  Equivalente Java de RNGCryptoServiceProvider de .NET.
 *
 *  ¿Qué es SecureRandom?
 *      Es la clase de Java para generar números aleatorios
 *      CRIPTOGRÁFICAMENTE SEGUROS. A diferencia de java.util.Random
 *      (que es predecible si conoces la semilla), SecureRandom usa
 *      fuentes de entropía del sistema operativo y produce bytes
 *      imposibles de adivinar.
 *
 *      EQUIVALENCIA con .NET:
 *          .NET:  RNGCryptoServiceProvider (legacy) o RandomNumberGenerator
 *          Java:  java.security.SecureRandom
 *
 *  En esta versión usamos SecureRandom para:
 *      1) Generar un IV (Initialization Vector) ALEATORIO POR CADA MENSAJE.
 *         El IV es necesario para AES en modo CBC y debe ser único por
 *         mensaje -> si se repite, dos mensajes idénticos darían el mismo
 *         texto cifrado y se filtraría información.
 *
 *  Formato del mensaje enviado por el socket:
 *      "IV_base64|texto_cifrado_base64"
 *
 *  La clave AES sigue siendo simétrica y compartida (hardcodeada).
 *  Lo importante aquí es el USO DE SecureRandom para el IV.
 * ============================================================================
 */
public class CifradorSecureRandom {

    // Clave AES compartida (16 bytes = 128 bits)
    private static final String CLAVE = "ClaveSecretaAES1";
    private static final SecretKeySpec SECRET_KEY =
            new SecretKeySpec(CLAVE.getBytes(), "AES");

    // Separador entre IV y texto cifrado en la trama.
    public static final String SEPARADOR = "|";

    // Una sola instancia de SecureRandom para todo el programa.
    // Es thread-safe y costoso de crear, así que conviene reutilizarla.
    private static final SecureRandom RNG = new SecureRandom();

    /**
     * Cifra un texto plano usando AES/CBC/PKCS5Padding con un IV ALEATORIO
     * generado con SecureRandom.
     *
     * Devuelve: "IV_base64|cifrado_base64"
     */
    public static String cifrar(String textoPlano) throws Exception {
        // 1) Generar IV aleatorio de 16 bytes con SecureRandom
        byte[] iv = new byte[16];
        RNG.nextBytes(iv); // <-- AQUÍ se usa SecureRandom (RNGCryptoServiceProvider equivalente)
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // 2) Cifrar con AES/CBC + ese IV
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, ivSpec);
        byte[] cifrado = cipher.doFinal(textoPlano.getBytes());

        // 3) Empaquetar IV+cifrado en Base64 separados por "|"
        String ivB64       = Base64.getEncoder().encodeToString(iv);
        String cifradoB64  = Base64.getEncoder().encodeToString(cifrado);
        return ivB64 + SEPARADOR + cifradoB64;
    }

    /**
     * Descifra una trama "IV_base64|cifrado_base64" y devuelve el texto plano.
     */
    public static String descifrar(String trama) throws Exception {
        // 1) Separar IV y cifrado
        String[] partes = trama.split("\\" + SEPARADOR, 2);
        byte[] iv       = Base64.getDecoder().decode(partes[0]);
        byte[] cifrado  = Base64.getDecoder().decode(partes[1]);

        // 2) Descifrar con AES/CBC usando el IV recibido
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, new IvParameterSpec(iv));
        byte[] plano = cipher.doFinal(cifrado);
        return new String(plano);
    }
}
