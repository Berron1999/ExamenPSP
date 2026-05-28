package org.example;

import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;
import java.util.Scanner;

/*
* Desarrolla una aplicación que:

Permita registrar un usuario solicitando nombre y contraseña.

Genere un salt aleatorio de 32 bytes.

Obtenga el hash de la contraseña usando PBKDF2WithHmacSHA256.

Guarde salt + hash concatenados en Base64.

Permita posteriormente verificar el login.*/

public class RegistroSeguro {

    private static String usuarioGuardado;
    private static String hashGuardado;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        // REGISTRO
        System.out.println("=== REGISTRO ===");
        System.out.print("Usuario: ");
        usuarioGuardado = sc.nextLine();

        System.out.print("Contraseña: ");
        String password = sc.nextLine();

        byte[] salt = generarSalt();
        byte[] hash = generarHash(password, salt);

        byte[] combinado = new byte[64];
        System.arraycopy(salt, 0, combinado, 0, 32);
        System.arraycopy(hash, 0, combinado, 32, 32);

        hashGuardado = Base64.getEncoder().encodeToString(combinado);

        System.out.println("Usuario registrado correctamente\n");

        // LOGIN
        System.out.println("=== LOGIN ===");
        System.out.print("Usuario: ");
        String userLogin = sc.nextLine();

        System.out.print("Contraseña: ");
        String passLogin = sc.nextLine();

        if (verificar(userLogin, passLogin)) {
            System.out.println("Login correcto");
        } else {
            System.out.println("Login incorrecto");
        }
    }

    private static byte[] generarSalt() {
        byte[] salt = new byte[32];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static byte[] generarHash(String password, byte[] salt)
            throws Exception {

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 1000, 256);
        SecretKeyFactory skf =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return skf.generateSecret(spec).getEncoded();
    }

    private static boolean verificar(String user, String password)
            throws Exception {

        if (!user.equals(usuarioGuardado)) return false;

        byte[] combinado = Base64.getDecoder().decode(hashGuardado);

        byte[] salt = new byte[32];
        byte[] hashOriginal = new byte[32];

        System.arraycopy(combinado, 0, salt, 0, 32);
        System.arraycopy(combinado, 32, hashOriginal, 0, 32);

        byte[] hashNuevo = generarHash(password, salt);

        for (int i = 0; i < 32; i++) {
            if (hashOriginal[i] != hashNuevo[i]) {
                return false;
            }
        }
        return true;
    }
}

