package org.example;

import com.sun.net.httpserver.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.Executors;

/*Requisitos:

Escuche en el puerto 8080.

Implemente las siguientes rutas:

/ → Página de bienvenida.

/login → Permite autenticarse enviando usuario y contraseña por GET.

/seguro → Solo accesible si el usuario se ha autenticado correctamente.

El sistema debe:

Registrar un usuario en memoria al iniciar el programa.

Guardar la contraseña usando:

Salt aleatorio de 32 bytes.

Hash con PBKDF2WithHmacSHA256.

Validar las credenciales comparando el hash.

No se debe almacenar la contraseña en claro.

Si el login es correcto, se permitirá el acceso a /seguro.*/
public class ServidorHttpConAutenticacion {

    private static final int PORT = 8080;

    private static String usuario = "admin";
    private static String passwordHash; // salt + hash combinados

    private static boolean autenticado = false;

    public static void main(String[] args) throws Exception {

        // REGISTRO INICIAL
        registrarUsuario("admin", "1234");

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.setExecutor(Executors.newFixedThreadPool(5));

        server.createContext("/", new HomeHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/seguro", new SecureHandler());

        server.start();

        System.out.println("Servidor iniciado en http://localhost:8080");
        System.out.println("Usuario: admin | Password: 1234");
    }

    // ===== REGISTRO =====

    private static void registrarUsuario(String user, String password) throws Exception {

        byte[] salt = new byte[32];
        new SecureRandom().nextBytes(salt);

        byte[] hash = generarHash(password, salt);

        byte[] combinado = new byte[64];
        System.arraycopy(salt, 0, combinado, 0, 32);
        System.arraycopy(hash, 0, combinado, 32, 32);

        passwordHash = Base64.getEncoder().encodeToString(combinado);
    }

    private static byte[] generarHash(String password, byte[] salt) throws Exception {

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 1000, 256);
        SecretKeyFactory skf =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return skf.generateSecret(spec).getEncoded();
    }

    // ===== HANDLERS =====

    static class HomeHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<h1>Bienvenido</h1>" +
                    "<p>Use /login?user=admin&pass=1234</p>";
            enviar(exchange, response, 200);
        }
    }

    static class LoginHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {

            try {
                String query = exchange.getRequestURI().getQuery();

                if (query == null) {
                    enviar(exchange, "Faltan parámetros", 400);
                    return;
                }

                String[] params = query.split("&");
                String user = params[0].split("=")[1];
                String pass = params[1].split("=")[1];

                if (verificar(user, pass)) {
                    autenticado = true;
                    enviar(exchange, "Login correcto", 200);
                } else {
                    enviar(exchange, "Login incorrecto", 401);
                }

            } catch (Exception e) {
                enviar(exchange, "Error en login", 500);
            }
        }
    }

    static class SecureHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {

            if (autenticado) {
                enviar(exchange, "Contenido seguro visible", 200);
            } else {
                enviar(exchange, "No autorizado", 403);
            }
        }
    }

    // ===== VERIFICACIÓN =====

    private static boolean verificar(String user, String password) throws Exception {

        if (!user.equals(usuario)) return false;

        byte[] combinado = Base64.getDecoder().decode(passwordHash);

        byte[] salt = new byte[32];
        byte[] hashOriginal = new byte[32];

        System.arraycopy(combinado, 0, salt, 0, 32);
        System.arraycopy(combinado, 32, hashOriginal, 0, 32);

        byte[] hashNuevo = generarHash(password, salt);

        for (int i = 0; i < 32; i++) {
            if (hashOriginal[i] != hashNuevo[i]) return false;
        }

        return true;
    }

    private static void enviar(HttpExchange exchange, String response, int code) throws IOException {
        byte[] bytes = response.getBytes();
        exchange.sendResponseHeaders(code, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}

