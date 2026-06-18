package VotacionElectronicaSincronizada;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class UtilVoto {

    // contraseña y salt fijos, compartidos entre todos los votantes y el servidor
    private static final String PASSWORD = "AsambleaSegura2026";
    private static final byte[] SALT = {
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08
    };

    public static class ResultadoCifrado {
        public byte[] datos;
        public byte[] iv;
    }

    // deriva la clave AES a partir de la contraseña y el salt fijos
    private static SecretKeySpec derivarClave() throws Exception {
        PBEKeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), SALT, 65536, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] clave = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(clave, "AES");
    }

    public static ResultadoCifrado cifrar(String opcion) throws Exception {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, derivarClave(), new IvParameterSpec(iv));
        byte[] datosCifrados = cipher.doFinal(opcion.getBytes());

        ResultadoCifrado resultado = new ResultadoCifrado();
        resultado.datos = datosCifrados;
        resultado.iv = iv;
        return resultado;
    }

    public static String descifrar(byte[] datos, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, derivarClave(), new IvParameterSpec(iv));
        byte[] datosClaros = cipher.doFinal(datos);
        return new String(datosClaros);
    }
}