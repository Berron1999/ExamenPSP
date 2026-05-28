package org.example.cifradoContraseñaHashSalt;

import java.util.Scanner;

/**
 * Clase ejecutable que simula el login de un usuario.
 */
public class LoginUsuario {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        /*
         * Simulamos una credencial que ya estaría guardada en una base de datos.
         *
         * Usuario real: pablo
         * Contraseña real: 1234
         */
        Credential credential = new Credential(
                "pablo",
                PasswordTools.createPasswordHash("1234")
        );

        System.out.println("=== LOGIN DE USUARIO ===");

        System.out.print("Introduce el nombre de usuario: ");
        String user = scanner.nextLine();

        System.out.print("Introduce la contraseña: ");
        String password = scanner.nextLine();

        /*
         * Primero comprobamos que el usuario coincide.
         */
        if (!credential.getUser().equals(user)) {
            System.out.println("Credenciales incorrectas.");
            return;
        }

        /*
         * Verificamos la contraseña.
         * No se descifra nada.
         * Se recalcula el hash usando la salt guardada y se compara.
         */
        boolean validPassword = PasswordTools.verifyPassword(
                password,
                credential.getPasswordHash()
        );

        if (validPassword) {
            System.out.println("Inicio de sesión correcto.");
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }
}