package org.example.cifradoContraseñaHashSalt;

/**
 * Clase que representa una credencial de usuario.
 *
 * No almacena la contraseña en claro.
 * Solo almacena:
 * - nombre de usuario
 * - salt + hash en formato Base64
 */
public class Credential {

    private String user;
    private String passwordHash;

    /**
     * Constructor de la credencial.
     *
     * @param user nombre de usuario
     * @param passwordHash salt + hash codificados en Base64
     */
    public Credential(String user, String passwordHash) {
        this.user = user;
        this.passwordHash = passwordHash;
    }

    public String getUser() {
        return user;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String toString() {
        return "Credential{" +
                "user='" + user + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}