package com.fondos.fondos_app.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendNotification(String notification, String message) {
        if ("EMAIL".equalsIgnoreCase(notification)) {
            System.out.println("Enviando Email: " + message);
            // Aquí se integraría el servicio de email
        } else if ("SMS".equalsIgnoreCase(notification)) {
            System.out.println("Enviando SMS: " + message);
            // Aquí se integraría el servicio de SMS
        } else {
            System.out.println("Método de notificación desconocido: " + notification);
        }
    }
}
