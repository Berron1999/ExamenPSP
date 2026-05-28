package EchoServerExamenAES;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


public class CifradorAES {

    // Clave simétrica de 16 bytes (128 bits). En un examen se hardcodea;
    // en un sistema real se intercambiaría por un canal seguro (RSA, etc.).
    private static final String CLAVE = "ClaveSecretaAES1"; // 16 caracteres ASCII = 16 bytes
    private static final SecretKeySpec SECRET_KEY =
            new SecretKeySpec(CLAVE.getBytes(), "AES");

    // Cifra una cadena -> devuelve texto Base64 listo para enviar por el socket.
    public static String cifrar(String textoPlano) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
        byte[] bytesCifrados = cipher.doFinal(textoPlano.getBytes());
        return Base64.getEncoder().encodeToString(bytesCifrados);
    }

    // Descifra un texto en Base64 -> devuelve la cadena original.
    public static String descifrar(String textoCifradoBase64) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
        byte[] bytesCifrados = Base64.getDecoder().decode(textoCifradoBase64);
        byte[] bytesPlanos   = cipher.doFinal(bytesCifrados);
        return new String(bytesPlanos);
    }
}
