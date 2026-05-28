package org.example.cifradoContraseñaHashSalt;

import java.util.Scanner;

/**
 * Programa completo que registra un usuario y después permite iniciar sesión.
 */
public class ProgramaCredenciales {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== REGISTRO ===");

        System.out.print("Introduce usuario: ");
        String user = scanner.nextLine();

        System.out.print("Introduce contraseña: ");
        String password = scanner.nextLine();

        String passwordHash = PasswordTools.createPasswordHash(password);

        Credential credential = new Credential(user, passwordHash);

        System.out.println();
        System.out.println("Usuario registrado correctamente.");
        System.out.println("Hash guardado:");
        System.out.println(credential.getPasswordHash());

        System.out.println();
        System.out.println("=== LOGIN ===");

        System.out.print("Introduce usuario: ");
        String loginUser = scanner.nextLine();

        System.out.print("Introduce contraseña: ");
        String loginPassword = scanner.nextLine();

        boolean sameUser = credential.getUser().equals(loginUser);

        boolean samePassword = PasswordTools.verifyPassword(
                loginPassword,
                credential.getPasswordHash()
        );

        if (sameUser && samePassword) {
            System.out.println("Inicio de sesión correcto.");
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }
}