package EchoServerExamenECDH;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

// Intercambio de claves Diffie-Hellman con curvas elípticas (ECDH):
// - Cada parte genera una pareja EC efímera.
// - Intercambian sus claves PÚBLICAS (en claro, no hace falta secreto).
// - Cada uno deriva el MISMO secreto compartido combinando su privada
//   con la pública del otro → matemática de DH.
// - Del secreto compartido se deriva una clave AES de sesión.
// Ventaja: ni siquiera el secreto compartido viaja por la red.
public class ECDHHelper {

    // IV fijo de sesión (en producción debería ser aleatorio por mensaje)
    private static final byte[] IV = {
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
            0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16
    };

    // Genera una pareja EC efímera (nueva por conexión)
    public static KeyPair generarParejaEC() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("EC");
        gen.initialize(256, new SecureRandom());
        return gen.generateKeyPair();
    }

    // Codifica una clave pública EC como Base64 (formato X.509)
    public static String codificarPublica(PublicKey pub) {
        return Base64.getEncoder().encodeToString(pub.getEncoded());
    }

    // Decodifica una clave pública EC desde Base64
    public static PublicKey decodificarPublica(String base64) throws Exception {
        KeyFactory kf = KeyFactory.getInstance("EC");
        return kf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(base64)));
    }

    // Deriva una clave AES-128 del secreto compartido ECDH.
    // El secreto compartido se hashea con SHA-256 para obtener material clave uniforme.
    public static byte[] derivarClaveAES(PrivateKey privadaLocal, PublicKey publicaRemota) throws Exception {
        KeyAgreement ka = KeyAgreement.getInstance("ECDH");
        ka.init(privadaLocal);
        ka.doPhase(publicaRemota, true);
        byte[] secreto = ka.generateSecret();

        // KDF simple: SHA-256 del secreto, truncado a 16 bytes para AES-128
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(secreto);
        byte[] aesKey = new byte[16];
        System.arraycopy(digest, 0, aesKey, 0, 16);
        return aesKey;
    }

    // Cifrar con la clave AES de sesión derivada del ECDH
    public static String cifrar(String textoClaro, byte[] aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,
                new SecretKeySpec(aesKey, "AES"),
                new IvParameterSpec(IV));
        return Base64.getEncoder().encodeToString(cipher.doFinal(textoClaro.getBytes("UTF-8")));
    }

    public static String descifrar(String base64, byte[] aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(aesKey, "AES"),
                new IvParameterSpec(IV));
        return new String(cipher.doFinal(Base64.getDecoder().decode(base64)), "UTF-8");
    }
}
