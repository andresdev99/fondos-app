package com.fondos.fondos_app.controller;

import com.fondos.fondos_app.entity.Cliente;
import com.fondos.fondos_app.entity.Fondo;
import com.fondos.fondos_app.entity.Transaccion;
import com.fondos.fondos_app.dto.SuscripcionRequest;
import com.fondos.fondos_app.service.ClienteService;
import com.fondos.fondos_app.service.FondoService;
import com.fondos.fondos_app.service.NotificationService;
import com.fondos.fondos_app.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
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

    @Autowired
    private FondoService fondoService;

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody SuscripcionRequest request) {
        try {
            Cliente client = clienteService.getClient(CLIENT_ID);
            String notificationType = client.getTipoNotificacion();

            Fondo fund = fondoService.getFund(request.getFondoId());
            String fundName = fund.getNombre();


            transaccionService.registerTransaction(client, fund, APERTURA_TYPE);
            notificationService.sendNotification(notificationType, request.getEmail(), "Te has suscrito al fondo: " + fundName);
            return ResponseEntity.ok("Suscripción realizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al realizar la suscripción: " + e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelSubscription(@RequestBody SuscripcionRequest request) {
        try {
            Cliente client = clienteService.getClient(CLIENT_ID);
            String notificationType = client.getTipoNotificacion();

            Fondo fund = fondoService.getFund(request.getFondoId());
            String fundName = fund.getNombre();

            transaccionService.registerTransaction(client, fund, CANCELACION_TYPE);
            notificationService.sendNotification(notificationType, request.getEmail(), "Has cancelado tu suscripción al fondo: " + fundName);
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
