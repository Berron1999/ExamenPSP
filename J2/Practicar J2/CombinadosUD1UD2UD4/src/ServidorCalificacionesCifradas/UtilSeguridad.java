package ServidorCalificacionesCifradas;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class UtilSeguridad {

    // clave AES de 16 bytes compartida entre servidor y cliente
    private static final byte[] CLAVE = {
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
            0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16
    };

    public static class ResultadoCifrado {
        public byte[] datos;
        public byte[] iv;
    }

    public static ResultadoCifrado cifrar(String texto) throws Exception {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        SecretKeySpec secretKey = new SecretKeySpec(CLAVE, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] datosCifrados = cipher.doFinal(texto.getBytes());

        ResultadoCifrado resultado = new ResultadoCifrado();
        resultado.datos = datosCifrados;
        resultado.iv = iv;
        return resultado;
    }

    public static String descifrar(byte[] datosCifrados, byte[] iv) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(CLAVE, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] datosClaros = cipher.doFinal(datosCifrados);

        return new String(datosClaros);
    }

    public static String calcularSHA256(String texto) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytesHash = md.digest(texto.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : bytesHash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}