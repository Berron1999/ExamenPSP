package EchoServerExamenCifradoRSA;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

// Clase auxiliar con métodos estáticos para cifrar y descifrar con RSA
public class CifradoRSA {

    // Clave pública y privada compartidas entre cliente y servidor
    private static final PublicKey  PUBLIC_KEY;
    private static final PrivateKey PRIVATE_KEY;

    // Generamos la pareja de claves con semilla fija → cliente y servidor obtienen
    // exactamente la misma pareja al arrancar (mismo SecureRandom determinista)
    static {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed("clave-fija-examen-psp".getBytes("UTF-8"));
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048, random);
            KeyPair par = gen.generateKeyPair();
            PUBLIC_KEY  = par.getPublic();
            PRIVATE_KEY = par.getPrivate();
        } catch (Exception e) {
            throw new RuntimeException("No se ha podido inicializar el par de claves RSA", e);
        }
    }

    // Cifra con la CLAVE PÚBLICA y devuelve el resultado en Base64
    public static String cifrar(String textoClaro) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, PUBLIC_KEY);
        byte[] cifrado = cipher.doFinal(textoClaro.getBytes("UTF-8"));
        // Base64 para poder enviarlo como texto por el socket
        return Base64.getEncoder().encodeToString(cifrado);
    }

    // Descifra con la CLAVE PRIVADA un texto en Base64 y devuelve el texto en claro
    public static String descifrar(String textoBase64) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, PRIVATE_KEY);
        byte[] descifrado = cipher.doFinal(Base64.getDecoder().decode(textoBase64));
        return new String(descifrado, "UTF-8");
    }
}
