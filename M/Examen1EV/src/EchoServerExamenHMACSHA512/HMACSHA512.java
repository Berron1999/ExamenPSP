package EchoServerExamenHMACSHA512;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

// HMAC-SHA512: hash CON clave secreta compartida.
// Variante de mayor tamaño de tag (64 bytes vs 32 de HMAC-SHA256).
// Garantiza integridad + autenticidad de origen.
public class HMACSHA512 {

    public static final String SEP = "|";

    // Clave secreta compartida entre cliente y servidor
    private static final byte[] KEY = {
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
            0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16,
            0x17, 0x18, 0x19, 0x20, 0x21, 0x22, 0x23, 0x24,
            0x25, 0x26, 0x27, 0x28, 0x29, 0x30, 0x31, 0x32
    };

    public static String mac(String texto) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(new SecretKeySpec(KEY, "HmacSHA512"));
        byte[] tag = mac.doFinal(texto.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(tag);
    }

    public static String empaquetar(String mensaje) throws Exception {
        return mensaje + SEP + mac(mensaje);
    }

    public static boolean verificar(String mensaje, String macRecibido) throws Exception {
        return mac(mensaje).equals(macRecibido);
    }
}
