package EchoServerExamenSHA512;

import java.security.MessageDigest;

/*
 * ============================================================================
 *  HashSHA512
 * ----------------------------------------------------------------------------
 *  IMPORTANTE: SHA-512 NO ES UN CIFRADO, es una FUNCIÓN HASH (de una sola
 *  dirección, NO se puede invertir).
 *
 *  Diferencia con SHA-256:
 *      - SHA-256 -> hash de 256 bits (32 bytes  -> 64 caracteres hex).
 *      - SHA-512 -> hash de 512 bits (64 bytes  -> 128 caracteres hex).
 *  Es más resistente a colisiones pero también más lento y produce
 *  hashes el doble de largos.
 *
 *  Uso aquí: comprobar la INTEGRIDAD de los mensajes que viajan por el socket.
 *  El emisor envía: "mensaje|hashSHA512(mensaje)"
 *  El receptor recalcula el hash y compara.
 * ============================================================================
 */
public class HashSHA512 {

    public static final String SEPARADOR = "|";

    // Calcula el hash SHA-512 de una cadena y lo devuelve en hexadecimal (128 chars).
    public static String calcular(String texto) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] hashBytes = md.digest(texto.getBytes());
        return bytesAHex(hashBytes);
    }

    // Recalcula y compara con el hash recibido.
    public static boolean verificar(String mensaje, String hashRecibido) throws Exception {
        return calcular(mensaje).equals(hashRecibido);
    }

    // Empaqueta mensaje + hash en una sola línea: "mensaje|hash".
    public static String empaquetar(String mensaje) throws Exception {
        return mensaje + SEPARADOR + calcular(mensaje);
    }

    private static String bytesAHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
