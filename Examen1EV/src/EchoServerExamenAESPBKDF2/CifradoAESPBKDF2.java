package EchoServerExamenAESPBKDF2;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

// Cifrado AES con clave DERIVADA de una contraseña usando PBKDF2-HMAC-SHA256.
// Equivalente Java al Rfc2898DeriveBytes de .NET.
// Ventaja: no se hardcodea la clave AES en código, solo la contraseña + salt.
// El alto número de iteraciones hace lentos los ataques de diccionario.
public class CifradoAESPBKDF2 {

    // Parámetros PBKDF2 (en producción la PASSWORD la introduce el usuario)
    private static final String PASSWORD    = "passwordExamenPSP2025";
    private static final byte[] SALT        = "saltExamenPSP".getBytes();
    private static final int    ITERACIONES = 65536;
    private static final int    KEY_LENGTH  = 128;  // AES-128

    private static final byte[] IV = {
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
            0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16
    };

    private static final SecretKey AES_KEY;

    // Derivar la clave UNA SOLA VEZ al cargar la clase (PBKDF2 es lento a propósito)
    static {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), SALT, ITERACIONES, KEY_LENGTH);
            byte[] keyBytes = factory.generateSecret(spec).getEncoded();
            AES_KEY = new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException("No se ha podido derivar la clave AES con PBKDF2", e);
        }
    }

    public static String cifrar(String textoClaro) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, AES_KEY, new IvParameterSpec(IV));
        byte[] cifrado = cipher.doFinal(textoClaro.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(cifrado);
    }

    public static String descifrar(String textoBase64) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, AES_KEY, new IvParameterSpec(IV));
        byte[] descifrado = cipher.doFinal(Base64.getDecoder().decode(textoBase64));
        return new String(descifrado, "UTF-8");
    }
}
