package org.iesch.psp.AlgoritmosDeEncriptacion.RSA;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

// RSA → clave pública para cifrar, clave privada para descifrar
// RSA → clave privada para firmar,  clave pública para verificar
public class EjercicioRSA {

    public static void main(String[] args) throws Exception {

        // Generamos el par de claves RSA de 2048 bits
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair par = keyGen.generateKeyPair();

        PublicKey  clavePublica  = par.getPublic();
        PrivateKey clavePrivada  = par.getPrivate();

        System.out.println("[RSA] Claves generadas");
        System.out.println("  Pública:  " +
                Base64.getEncoder().encodeToString(clavePublica.getEncoded())
                        .substring(0, 40) + "...");
        System.out.println("  Privada:  " +
                Base64.getEncoder().encodeToString(clavePrivada.getEncoded())
                        .substring(0, 40) + "...");

        // --- CIFRADO / DESCIFRADO ---
        String mensaje = "Mensaje secreto con RSA";

        // Ciframos con la clave PÚBLICA → solo el dueño de la privada puede descifrar
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, clavePublica);
        String cifrado = Base64.getEncoder().encodeToString(
                cipher.doFinal(mensaje.getBytes("UTF-8")));

        // Desciframos con la clave PRIVADA
        cipher.init(Cipher.DECRYPT_MODE, clavePrivada);
        String descifrado = new String(
                cipher.doFinal(Base64.getDecoder().decode(cifrado)), "UTF-8");

        System.out.println("\n[RSA] Cifrado:");
        System.out.println("  Original:  " + mensaje);
        System.out.println("  Cifrado:   " + cifrado.substring(0, 40) + "...");
        System.out.println("  Descifrado:" + descifrado);

        // --- FIRMA DIGITAL ---
        String documento = "Contrato importante a firmar";

        // Firmamos con la clave PRIVADA
        Signature firma = Signature.getInstance("SHA256withRSA");
        firma.initSign(clavePrivada);
        firma.update(documento.getBytes("UTF-8"));
        String firmaDigital = Base64.getEncoder().encodeToString(firma.sign());

        // Verificamos con la clave PÚBLICA
        firma.initVerify(clavePublica);
        firma.update(documento.getBytes("UTF-8"));
        boolean firmaValida = firma.verify(Base64.getDecoder().decode(firmaDigital));

        System.out.println("\n[RSA] Firma digital:");
        System.out.println("  Documento: " + documento);
        System.out.println("  Firma:     " + firmaDigital.substring(0, 40) + "...");
        System.out.println("  Válida:    " + firmaValida);
    }
}