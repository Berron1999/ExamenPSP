package org.example;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PetadorCorreo {
    public static void main(String[] args) {
        // Configuración de la cuenta
        final String fromEmail = "pablonosequw@gmail.com";
        final String password = "swqn cmdu htki svbp"; // Contraseña de aplicación
        final String toEmail = "dborjam@iesch.org";

        // Propiedades SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Creamos la sesión una sola vez para que todos los hilos la compartan
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });


        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 1; i <= 100; i++) {
            final int idEnvio = i;
            executor.execute(() -> {
                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(fromEmail, "Spammer de Java"));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                    message.setSubject("Correo número " + idEnvio);
                    message.setText("El numero de veces que me follo a tu madre " + idEnvio);

                    System.out.println("Hilo " + Thread.currentThread().getName() + " enviando correo " + idEnvio + "...");
                    Transport.send(message);
                    System.out.println("¡Correo " + idEnvio + " enviado!");

                } catch (Exception e) {
                    System.err.println("Error en el envío " + idEnvio + ": " + e.getMessage());
                }
            });
        }

        // Cerramos el pool al terminar (esperará a que todos terminen)
        executor.shutdown();
    }
}
