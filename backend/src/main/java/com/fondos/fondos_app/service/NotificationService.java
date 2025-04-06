package com.fondos.fondos_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender mailSender;

    public void sendNotification(String notificationType, String email, String message) {
        if ("EMAIL".equalsIgnoreCase(notificationType)) {
            sendEmail(email, message);
        } else if ("SMS".equalsIgnoreCase(notificationType)) {
            System.out.println("Enviando SMS: " + message);
            // Integrate SMS service here (e.g., AWS SNS)
        } else {
            System.out.println("Método de notificación desconocido: " + notificationType);
        }
    }

    private void sendEmail(String to, String text) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);  // Must be the same as spring.mail.username
            mailMessage.setTo(to);
            mailMessage.setSubject("Notificación de FONDOS TEST");
            mailMessage.setText(text);

            mailSender.send(mailMessage);
            System.out.println("Email enviado exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al enviar email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
