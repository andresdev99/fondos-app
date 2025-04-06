package com.fondos.fondos_app.controller;

import com.fondos.fondos_app.entity.Transaccion;
import com.fondos.fondos_app.dto.SuscripcionRequest;
import com.fondos.fondos_app.service.ClienteService;
import com.fondos.fondos_app.service.NotificationService;
import com.fondos.fondos_app.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransaccionController {

    private static final String CLIENT_ID = "1";
    private static final String APERTURA_TYPE = "apertura";
    private static final String CANCELACION_TYPE = "cancelacion";

    @Autowired
    private TransaccionService transaccionService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody SuscripcionRequest request) {
        try {
            String tipoNotificacion = clienteService.getNotificationType(CLIENT_ID);
            transaccionService.registerTransaction(CLIENT_ID, APERTURA_TYPE, request.getFondoId());
            notificationService.sendNotification(tipoNotificacion, "Te has suscrito al fondo: " + request.getFondoId());
            return ResponseEntity.ok("Suscripción realizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al realizar la suscripción: " + e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelSubscription(@RequestBody SuscripcionRequest request) {
        try {
            String tipoNotificacion = clienteService.getNotificationType(CLIENT_ID);
            transaccionService.registerTransaction(CLIENT_ID, CANCELACION_TYPE, request.getFondoId());
            notificationService.sendNotification(tipoNotificacion, "Has cancelado tu suscripción al fondo: " + request.getFondoId());
            return ResponseEntity.ok("Cancelación realizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al cancelar la suscripción: " + e.getMessage());
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaccion>> getHistory() {
        try {
            List<Transaccion> historial = transaccionService.getHistory(CLIENT_ID);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
