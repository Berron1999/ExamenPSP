package EchoServerExamenCifradoAES;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

// Clase auxiliar con métodos estáticos para cifrar y descifrar con AES
public class CifradoAES {

    // Clave de cifrado compartida entre cliente y servidor (16 bytes = 128 bits)
    private static final byte[] KEY = {
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
            0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16
    };

    // Vector de inicialización compartido (16 bytes)
    private static final byte[] IV = {
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
            0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16
    };

    // Cifra un texto en claro y devuelve el resultado en Base64
    public static String cifrar(String textoCifrado) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(KEY, "AES");
        IvParameterSpec ivSpec  = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] cifrado = cipher.doFinal(textoCifrado.getBytes("UTF-8"));
        // Convertimos a Base64 para poder enviarlo como texto por el socket
        return Base64.getEncoder().encodeToString(cifrado);
    }

    // Descifra un texto en Base64 y devuelve el texto en claro
    public static String descifrar(String textoBase64) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(KEY, "AES");
        IvParameterSpec ivSpec  = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] descifrado = cipher.doFinal(Base64.getDecoder().decode(textoBase64));
        return new String(descifrado, "UTF-8");
    }
}