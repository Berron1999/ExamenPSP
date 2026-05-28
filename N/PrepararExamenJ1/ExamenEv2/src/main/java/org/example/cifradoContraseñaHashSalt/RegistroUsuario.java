package org.example.cifradoContraseñaHashSalt;

import java.util.Scanner;

/**
 * Clase ejecutable que simula el registro de un usuario.
 */
public class RegistroUsuario {

    /*
     * Credencial compartida para simular una base de datos.
     *
     * En un proyecto real esto estaría en una BBDD.
     * Aquí lo usamos para practicar el ejercicio.
     */
    public static Credential credential;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== REGISTRO DE USUARIO ===");

        System.out.print("Introduce el nombre de usuario: ");
        String user = scanner.nextLine();

        System.out.print("Introduce la contraseña: ");
        String password = scanner.nextLine();

        /*
         * Creamos el hash seguro de la contraseña.
         * Esto incluye salt + hash en Base64.
         */
        String passwordHash = PasswordTools.createPasswordHash(password);

        /*
         * Creamos la credencial.
         */
        credential = new Credential(user, passwordHash);

        System.out.println();
        System.out.println("Usuario registrado correctamente.");
        System.out.println("Usuario: " + credential.getUser());
        System.out.println("Hash almacenado en Base64:");
        System.out.println(credential.getPasswordHash());

        System.out.println();
        System.out.println("IMPORTANTE: la contraseña original no se guarda.");
    }
}