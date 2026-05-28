package EchoServerExamenRSA;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/*
 * ============================================================================
 *  CifradorRSA
 * ----------------------------------------------------------------------------
 *  RSA es un algoritmo de CIFRADO ASIMÉTRICO. Cada participante tiene
 *  DOS claves:
 *      - clave PÚBLICA  -> se comparte abiertamente. Sirve para CIFRAR.
 *      - clave PRIVADA  -> se guarda en secreto. Sirve para DESCIFRAR.
 *
 *  Si quieres mandarme un mensaje:
 *      1) Yo te paso mi clave PÚBLICA.
 *      2) Tú cifras el mensaje con MI clave PÚBLICA.
 *      3) Sólo yo puedo descifrarlo (con MI clave PRIVADA).
 *
 *  Ventaja sobre AES: no hace falta intercambiar una clave secreta
 *  por un canal seguro -> las claves públicas pueden viajar a la vista.
 *
 *  Desventaja: es MUY LENTO comparado con AES y sólo cifra mensajes pequeños
 *  (con RSA 2048 + padding PKCS1 -> máx. 245 bytes). Por eso en la práctica
 *  se usa para intercambiar una clave AES, no para cifrar mensajes largos.
 *
 *  Aquí los mensajes son cortos ("Cliente-X-Msg-Y"), así que RSA puro vale.
 * ============================================================================
 */
public class CifradorRSA {

    public static final int TAMANO_CLAVE = 2048; // bits

    // Genera un par nuevo de claves RSA (público + privado).
    public static KeyPair generarParClaves() throws Exception {
        KeyPairGenerator generador = KeyPairGenerator.getInstance("RSA");
        generador.initialize(TAMANO_CLAVE);
        return generador.generateKeyPair();
    }

    // Cifra una cadena con la clave PÚBLICA destino -> Base64 listo para socket.
    public static String cifrar(String textoPlano, PublicKey clavePublica) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, clavePublica);
        byte[] bytesCifrados = cipher.doFinal(textoPlano.getBytes());
        return Base64.getEncoder().encodeToString(bytesCifrados);
    }

    // Descifra texto Base64 con la clave PRIVADA propia.
    public static String descifrar(String textoBase64, PrivateKey clavePrivada) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, clavePrivada);
        byte[] bytesCifrados = Base64.getDecoder().decode(textoBase64);
        byte[] bytesPlanos   = cipher.doFinal(bytesCifrados);
        return new String(bytesPlanos);
    }

    // Convierte una PublicKey a String Base64 -> apto para enviar por socket.
    public static String exportarClavePublica(PublicKey clavePublica) {
        return Base64.getEncoder().encodeToString(clavePublica.getEncoded());
    }

    // Reconstruye una PublicKey a partir de su Base64 (lo contrario a exportar).
    public static PublicKey importarClavePublica(String base64) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(base64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }
}
