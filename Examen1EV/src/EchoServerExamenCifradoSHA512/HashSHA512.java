package EchoServerExamenCifradoSHA512;

import java.security.MessageDigest;
import java.util.Base64;

// Clase auxiliar con métodos estáticos para calcular y verificar hashes SHA-512.
// SHA-512 NO es un cifrado: es una función de hash unidireccional (no se puede "descifrar").
// El servidor lo usa para verificar la integridad del mensaje recibido.
public class HashSHA512 {

    // Separador entre mensaje y hash en el protocolo de transporte
    public static final String SEP = "|";

    // Calcula el SHA-512 de un texto y devuelve el digest en Base64
    public static String hash(String texto) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] digest = md.digest(texto.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(digest);
    }

    // Empaqueta un mensaje con su hash para el envío: "mensaje|hash"
    public static String empaquetar(String mensaje) throws Exception {
        return mensaje + SEP + hash(mensaje);
    }

    // Verifica que el hash que viaja con el mensaje coincide con el calculado
    public static boolean verificar(String mensaje, String hashRecibido) throws Exception {
        return hash(mensaje).equals(hashRecibido);
    }
}
