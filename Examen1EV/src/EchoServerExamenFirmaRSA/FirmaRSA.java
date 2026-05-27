package EchoServerExamenFirmaRSA;

import java.security.*;
import java.util.Base64;

// Firma digital RSA: se firma con la clave PRIVADA y se verifica con la PÚBLICA.
// Garantiza integridad + AUTORÍA (no solo integridad como HMAC):
// solo quien tiene la privada puede haber generado una firma válida.
public class FirmaRSA {

    public static final String SEP = "|";

    private static final PublicKey  PUBLIC_KEY;
    private static final PrivateKey PRIVATE_KEY;

    // Generamos la pareja con semilla fija → cliente y servidor obtienen la misma
    static {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed("clave-firma-rsa-examen-psp".getBytes("UTF-8"));
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048, random);
            KeyPair par = gen.generateKeyPair();
            PUBLIC_KEY  = par.getPublic();
            PRIVATE_KEY = par.getPrivate();
        } catch (Exception e) {
            throw new RuntimeException("No se ha podido inicializar el par de claves RSA", e);
        }
    }

    // Firma un texto con la clave PRIVADA y devuelve la firma en Base64
    public static String firmar(String texto) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(PRIVATE_KEY);
        sig.update(texto.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(sig.sign());
    }

    // Verifica una firma con la clave PÚBLICA
    public static boolean verificar(String texto, String firmaBase64) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(PUBLIC_KEY);
        sig.update(texto.getBytes("UTF-8"));
        return sig.verify(Base64.getDecoder().decode(firmaBase64));
    }

    // Empaqueta un mensaje con su firma: "mensaje|firma"
    public static String empaquetar(String mensaje) throws Exception {
        return mensaje + SEP + firmar(mensaje);
    }
}
