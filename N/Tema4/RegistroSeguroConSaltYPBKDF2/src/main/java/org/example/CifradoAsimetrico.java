package org.example;

import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import javax.crypto.Cipher;

public class CifradoAsimetrico {

    public static void main(String[] args) throws Exception {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);

        KeyPair pair = keyGen.generateKeyPair();

        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();

        String mensaje = "Mensaje secreto";

        // CIFRAR
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cifrado = cipher.doFinal(mensaje.getBytes());

        // DESCIFRAR
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] descifrado = cipher.doFinal(cifrado);

        System.out.println("Original: " + mensaje);
        System.out.println("Descifrado: " + new String(descifrado));
    }
}