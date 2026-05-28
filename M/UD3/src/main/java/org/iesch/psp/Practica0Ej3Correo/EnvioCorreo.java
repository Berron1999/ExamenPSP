package org.iesch.psp.Practica0Ej3Correo;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.util.Properties;
import java.util.Scanner;

public class EnvioCorreo {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Pedimos los datos al usuario
        System.out.print("Correo emisor: ");
        String fromEmail = sc.nextLine().trim();

        System.out.print("Contraseña: ");
        String password = sc.nextLine().trim();

        System.out.print("Correo destinatario: ");
        String toEmail = sc.nextLine().trim();

        System.out.print("Asunto: ");
        String asunto = sc.nextLine().trim();

        System.out.print("Cuerpo del mensaje: ");
        String cuerpo = sc.nextLine().trim();

        System.out.print("¿Adjuntar archivo? (s/n): ");
        String adjuntarOp = sc.nextLine().trim();

        String rutaAdjunto = null;
        if (adjuntarOp.equalsIgnoreCase("s")) {
            System.out.print("Ruta del archivo a adjuntar: ");
            rutaAdjunto = sc.nextLine().trim();
        }

        // Propiedades SMTP → igual que en los apuntes (Gmail puerto 587 STARTTLS)
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Creamos la sesión con autenticación → igual que en los apuntes
        final String finalPassword = password;
        final String finalEmail    = fromEmail;

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(finalEmail, finalPassword);
            }
        });

        // Enviamos con o sin adjunto según la elección del usuario
        try {
            if (rutaAdjunto != null) {
                enviarConAdjunto(session, fromEmail, toEmail, asunto, cuerpo, rutaAdjunto);
            } else {
                enviarSinAdjunto(session, fromEmail, toEmail, asunto, cuerpo);
            }
        } catch (Exception e) {
            System.out.println("[SMTP] Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Envío simple sin adjunto → igual que SendMailExample de los apuntes
    private static void enviarSinAdjunto(Session session, String fromEmail,
                                         String toEmail, String asunto,
                                         String cuerpo) throws Exception {
        // Creamos el mensaje
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(toEmail));
        message.setSubject(asunto);
        message.setText(cuerpo);

        // Enviamos el mensaje
        Transport.send(message);
        System.out.println("[SMTP] Correo enviado correctamente a " + toEmail);
    }

    // Envío con adjunto → igual que SendMailWithAttachment de los apuntes
    private static void enviarConAdjunto(Session session, String fromEmail,
                                         String toEmail, String asunto,
                                         String cuerpo, String rutaAdjunto) throws Exception {

        File archivo = new File(rutaAdjunto);
        if (!archivo.exists()) {
            System.out.println("[SMTP] Error: el archivo adjunto no existe: " + rutaAdjunto);
            return;
        }

        // Creamos el mensaje
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(toEmail));
        message.setSubject(asunto);

        // Parte 1: cuerpo del mensaje en texto plano
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(cuerpo);

        // Parte 2: archivo adjunto
        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource source = new FileDataSource(archivo);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(archivo.getName());
        attachmentPart.setDisposition(Part.ATTACHMENT);

        // Creamos el contenedor multipart/mixed y añadimos las dos partes
        Multipart multipart = new MimeMultipart("mixed");
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);

        // Asignamos el multipart como cuerpo del mensaje
        message.setContent(multipart);

        // Enviamos el mensaje
        Transport.send(message);
        System.out.println("[SMTP] Correo enviado con adjunto correctamente a " + toEmail);
        System.out.println("[SMTP] Archivo adjunto: " + archivo.getName());
    }
}