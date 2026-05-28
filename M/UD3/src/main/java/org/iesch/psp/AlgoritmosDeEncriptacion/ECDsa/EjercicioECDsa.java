package org.iesch.psp.AlgoritmosDeEncriptacion.ECDsa;

import java.security.*;
import java.util.Base64;

// ECDsa → más moderno y eficiente que RSA para firma digital
// Clave privada para firmar, clave pública para verificar
public class EjercicioECDsa {

    public static void main(String[] args) throws Exception {

        // Generamos par de claves EC de 256 bits
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(256);
        KeyPair par = keyGen.generateKeyPair();

        PublicKey  clavePublica = par.getPublic();
        PrivateKey clavePrivada = par.getPrivate();

        System.out.println("[ECDsa] Claves generadas (256 bits)");

        String documento = "Documento firmado con ECDsa";

        // Firmamos con la clave PRIVADA
        Signature firma = Signature.getInstance("SHA256withECDSA");
        firma.initSign(clavePrivada);
        firma.update(documento.getBytes("UTF-8"));
        String firmaDigital = Base64.getEncoder().encodeToString(firma.sign());

        System.out.println("[ECDsa] Documento: " + documento);
        System.out.println("[ECDsa] Firma:     " + firmaDigital.substring(0, 40) + "...");

        // Verificamos con la clave PÚBLICA → firma válida
        firma.initVerify(clavePublica);
        firma.update(documento.getBytes("UTF-8"));
        boolean valida = firma.verify(Base64.getDecoder().decode(firmaDigital));
        System.out.println("[ECDsa] Firma válida: " + valida);

        // Verificamos con documento alterado → firma inválida
        String documentoAlterado = "Documento ALTERADO";
        firma.initVerify(clavePublica);
        firma.update(documentoAlterado.getBytes("UTF-8"));
        boolean alterada = firma.verify(Base64.getDecoder().decode(firmaDigital));
        System.out.println("[ECDsa] Firma válida (alterado): " + alterada);
    }
}