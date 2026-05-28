package EchoServerExamenSHA256;

import java.security.MessageDigest;

/*
 * ============================================================================
 *  HashSHA256
 * ----------------------------------------------------------------------------
 *  IMPORTANTE: SHA-256 NO ES UN CIFRADO, es una FUNCIÓN HASH.
 *      - Cifrado  -> se puede REVERTIR (descifrar) con la clave.
 *      - Hash     -> es de UNA SOLA DIRECCIÓN. No se puede "deshacer".
 *
 *  Uso típico: garantizar INTEGRIDAD del mensaje.
 *      El emisor envía: mensaje + hash(mensaje)
 *      El receptor recalcula hash(mensaje) y compara con el recibido.
 *      Si NO coinciden -> el mensaje ha sido manipulado o corrompido.
 *
 *  SHA-256 produce un hash de 256 bits (32 bytes), que se representa
 *  típicamente como 64 caracteres hexadecimales.
 * ============================================================================
 */
public class HashSHA256 {

    // Carácter separador entre el mensaje y su hash en la trama enviada.
    // Se usa "|" porque no aparece en los mensajes "Cliente-X-Msg-Y" ni en ".".
    public static final String SEPARADOR = "|";

    // Calcula el hash SHA-256 de una cadena y lo devuelve en hexadecimal.
    public static String calcular(String texto) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(texto.getBytes());
        return bytesAHex(hashBytes);
    }

    // Verifica si un mensaje recibido es íntegro:
    //   recalcula su hash y lo compara con el hash que vino en la trama.
    public static boolean verificar(String mensaje, String hashRecibido) throws Exception {
        String hashCalculado = calcular(mensaje);
        return hashCalculado.equals(hashRecibido);
    }

    // Empaqueta mensaje + hash en una sola línea: "mensaje|hash".
    public static String empaquetar(String mensaje) throws Exception {
        return mensaje + SEPARADOR + calcular(mensaje);
    }

    // Convierte un array de bytes a su representación hexadecimal.
    private static String bytesAHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            // %02x -> cada byte se imprime como 2 dígitos hex en minúscula
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
