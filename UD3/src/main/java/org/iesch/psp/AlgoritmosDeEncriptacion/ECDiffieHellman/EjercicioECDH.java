package org.iesch.psp.AlgoritmosDeEncriptacion.ECDiffieHellman;

import javax.crypto.KeyAgreement;
import java.security.*;
import java.util.Base64;
import java.util.Arrays;

// ECDH → dos partes generan una clave compartida SIN enviársela por la red
// Alice y Bob acaban con la misma clave sin habérsela enviado
public class EjercicioECDH {

    public static void main(String[] args) throws Exception {

        // Alice genera su par de claves
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(256);
        KeyPair parAlice = keyGen.generateKeyPair();

        // Bob genera su par de claves
        KeyPair parBob = keyGen.generateKeyPair();

        System.out.println("[ECDH] Alice y Bob generan sus claves independientemente");

        // Alice genera la clave compartida usando:
        //   → su clave PRIVADA + clave PÚBLICA de Bob
        KeyAgreement kaAlice = KeyAgreement.getInstance("ECDH");
        kaAlice.init(parAlice.getPrivate());
        kaAlice.doPhase(parBob.getPublic(), true);
        byte[] claveAlice = kaAlice.generateSecret();

        // Bob genera la clave compartida usando:
        //   → su clave PRIVADA + clave PÚBLICA de Alice
        KeyAgreement kaBob = KeyAgreement.getInstance("ECDH");
        kaBob.init(parBob.getPrivate());
        kaBob.doPhase(parAlice.getPublic(), true);
        byte[] claveBob = kaBob.generateSecret();

        System.out.println("[ECDH] Clave de Alice: " +
                Base64.getEncoder().encodeToString(claveAlice).substring(0, 20) + "...");
        System.out.println("[ECDH] Clave de Bob:   " +
                Base64.getEncoder().encodeToString(claveBob).substring(0, 20) + "...");

        // Las dos claves deben ser idénticas aunque nunca se intercambiaron
        boolean iguales = Arrays.equals(claveAlice, claveBob);
        System.out.println("[ECDH] Claves idénticas sin haberse enviado: " + iguales);
    }
}