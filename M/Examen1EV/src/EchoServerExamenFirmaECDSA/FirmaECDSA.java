package EchoServerExamenFirmaECDSA;

import java.security.*;
import java.util.Base64;

// Firma digital ECDSA (curvas elípticas): mismo concepto que RSA pero con
// claves mucho más cortas (256 bits ECDSA ≈ 3072 bits RSA en seguridad).
public class FirmaECDSA {

    public static final String SEP = "|";

    private static final PublicKey  PUBLIC_KEY;
    private static final PrivateKey PRIVATE_KEY;

    // Generamos la pareja EC con semilla fija → cliente y servidor obtienen la misma
    static {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed("clave-firma-ecdsa-examen-psp".getBytes("UTF-8"));
            KeyPairGenerator gen = KeyPairGenerator.getInstance("EC");
            gen.initialize(256, random);
            KeyPair par = gen.generateKeyPair();
            PUBLIC_KEY  = par.getPublic();
            PRIVATE_KEY = par.getPrivate();
        } catch (Exception e) {
            throw new RuntimeException("No se ha podido inicializar el par de claves EC", e);
        }
    }

    // Firma un texto con la clave PRIVADA EC y devuelve la firma en Base64
    public static String firmar(String texto) throws Exception {
        Signature sig = Signature.getInstance("SHA256withECDSA");
        sig.initSign(PRIVATE_KEY);
        sig.update(texto.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(sig.sign());
    }

    // Verifica una firma con la clave PÚBLICA EC
    public static boolean verificar(String texto, String firmaBase64) throws Exception {
        Signature sig = Signature.getInstance("SHA256withECDSA");
        sig.initVerify(PUBLIC_KEY);
        sig.update(texto.getBytes("UTF-8"));
        return sig.verify(Base64.getDecoder().decode(firmaBase64));
    }

    public static String empaquetar(String mensaje) throws Exception {
        return mensaje + SEP + firmar(mensaje);
    }
}
