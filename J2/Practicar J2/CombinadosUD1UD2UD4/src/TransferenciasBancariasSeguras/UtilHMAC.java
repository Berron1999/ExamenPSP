package TransferenciasBancariasSeguras;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class UtilHMAC {

    // clave secreta de 16 bytes compartida entre cliente y servidor
    private static final byte[] CLAVE = {
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
            0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16
    };

    public static String calcularHMAC(String texto) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(CLAVE, "HmacSHA256");

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        byte[] resultado = mac.doFinal(texto.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : resultado) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}