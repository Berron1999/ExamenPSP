package EchoServerExamenHMACSHA256;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

// HMAC-SHA256: hash CON clave secreta compartida.
// A diferencia del SHA puro, un atacante NO puede modificar el mensaje y recalcular
// el MAC sin conocer la clave → integridad + autenticidad de origen.
public class HMACSHA256 {

    public static final String SEP = "|";

    // Clave secreta compartida entre cliente y servidor
    private static final byte[] KEY = {
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
            0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16
    };

    // Calcula el HMAC-SHA256 de un texto y lo devuelve en Base64
    public static String mac(String texto) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(KEY, "HmacSHA256"));
        byte[] tag = mac.doFinal(texto.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(tag);
    }

    // Empaqueta un mensaje con su MAC: "mensaje|mac"
    public static String empaquetar(String mensaje) throws Exception {
        return mensaje + SEP + mac(mensaje);
    }

    // Verifica que el MAC recibido coincide con el recalculado
    public static boolean verificar(String mensaje, String macRecibido) throws Exception {
        return mac(mensaje).equals(macRecibido);
    }
}
