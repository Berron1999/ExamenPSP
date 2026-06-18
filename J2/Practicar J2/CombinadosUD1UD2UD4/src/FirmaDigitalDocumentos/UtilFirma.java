package FirmaDigitalDocumentos;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class UtilFirma {

    public static KeyPair generarClaves() throws Exception {
        KeyPairGenerator generador = KeyPairGenerator.getInstance("RSA");
        generador.initialize(2048);
        return generador.generateKeyPair();
    }

    public static byte[] firmar(String texto, PrivateKey clavePrivada) throws Exception {
        Signature firma = Signature.getInstance("SHA256withRSA");
        firma.initSign(clavePrivada);
        firma.update(texto.getBytes());
        return firma.sign();
    }

    public static boolean verificar(String texto, byte[] firma, PublicKey clavePublica) throws Exception {
        Signature verificador = Signature.getInstance("SHA256withRSA");
        verificador.initVerify(clavePublica);
        verificador.update(texto.getBytes());
        return verificador.verify(firma);
    }
}