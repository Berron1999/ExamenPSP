package EchoServerExamenHibridoAESRSA;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

// Cifrado HÍBRIDO: combina lo mejor de AES (rápido, simétrico) y RSA (asimétrico).
// Para CADA mensaje:
//   1. Se genera una clave AES aleatoria + IV aleatorio (sesión efímera)
//   2. Se cifra el mensaje con AES → rápido aunque sea largo
//   3. Se cifra el "sobre" (clave AES + IV) con RSA pública → seguro intercambio
//   4. Se envía "rsa_envelope|aes_ciphertext"
// Así se evita el coste de cifrar todo con RSA y el riesgo de claves AES estáticas.
public class CifradoHibrido {

    public static final String SEP = "|";

    private static final PublicKey  PUBLIC_KEY;
    private static final PrivateKey PRIVATE_KEY;
    private static final SecureRandom RNG = new SecureRandom();

    static {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed("clave-hibrido-examen-psp".getBytes("UTF-8"));
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048, random);
            KeyPair par = gen.generateKeyPair();
            PUBLIC_KEY  = par.getPublic();
            PRIVATE_KEY = par.getPrivate();
        } catch (Exception e) {
            throw new RuntimeException("No se ha podido inicializar RSA", e);
        }
    }

    public static String cifrar(String textoClaro) throws Exception {
        // 1. Generar clave AES de 128 bits + IV de 16 bytes aleatorios
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128, RNG);
        SecretKey aesKey = keyGen.generateKey();
        byte[] iv = new byte[16];
        RNG.nextBytes(iv);

        // 2. Cifrar el mensaje con AES/CBC
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(iv));
        byte[] cipherText = aesCipher.doFinal(textoClaro.getBytes("UTF-8"));

        // 3. Empaquetar (clave AES 16B + IV 16B) = 32 bytes y cifrar con RSA pública
        byte[] envelope = new byte[32];
        System.arraycopy(aesKey.getEncoded(), 0, envelope, 0,  16);
        System.arraycopy(iv,                  0, envelope, 16, 16);
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, PUBLIC_KEY);
        byte[] envelopeCipher = rsaCipher.doFinal(envelope);

        // 4. Devolver "rsa_envelope|aes_ciphertext" en Base64
        return Base64.getEncoder().encodeToString(envelopeCipher)
                + SEP
                + Base64.getEncoder().encodeToString(cipherText);
    }

    public static String descifrar(String textoCifrado) throws Exception {
        int sep = textoCifrado.lastIndexOf(SEP);
        byte[] envelopeCipher = Base64.getDecoder().decode(textoCifrado.substring(0, sep));
        byte[] cipherText     = Base64.getDecoder().decode(textoCifrado.substring(sep + 1));

        // 1. Descifrar el sobre con RSA privada → recuperamos clave AES + IV
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.DECRYPT_MODE, PRIVATE_KEY);
        byte[] envelope = rsaCipher.doFinal(envelopeCipher);
        byte[] aesKeyBytes = new byte[16];
        byte[] iv          = new byte[16];
        System.arraycopy(envelope, 0,  aesKeyBytes, 0, 16);
        System.arraycopy(envelope, 16, iv,          0, 16);

        // 2. Descifrar mensaje con AES
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(aesKeyBytes, "AES"),
                new IvParameterSpec(iv));
        return new String(aesCipher.doFinal(cipherText), "UTF-8");
    }
}
