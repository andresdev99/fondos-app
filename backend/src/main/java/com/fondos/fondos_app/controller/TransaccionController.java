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

/**
 * Controller for handling transaction-related operations.
 * <p>
 * This controller provides endpoints for:
 * <ul>
 *   <li>Subscribing to a fund (registering an "apertura" transaction).</li>
 *   <li>Cancelling a subscription (registering a "cancelacion" transaction).</li>
 *   <li>Retrieving the transaction history for a client.</li>
 * </ul>
 * <p>
 * <b>Note:</b> The client ID is hardcoded to "1" for simplicity.
 * <p>
 * Cross-origin requests are allowed from <code>http://localhost:5173</code>.
 */
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

    /**
     * Subscribes a client to a fund.
     * <p>
     * This endpoint performs the following steps:
     * <ol>
     *   <li>Retrieves the client using a fixed client ID ("1").</li>
     *   <li>Retrieves the fund using the fund ID provided in the request.</li>
     *   <li>Registers an "apertura" transaction for the subscription.</li>
     *   <li>Sends a notification to the client confirming the subscription.</li>
     * </ol>
     *
     * @param request the subscription request containing the fund ID and email.
     * @return a {@link ResponseEntity} with a success message, or an error message with HTTP status 500.
     */
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

    /**
     * Cancels a client's subscription to a fund.
     * <p>
     * This endpoint performs the following steps:
     * <ol>
     *   <li>Retrieves the client using a fixed client ID ("1").</li>
     *   <li>Retrieves the fund using the fund ID provided in the request.</li>
     *   <li>Registers a "cancelacion" transaction for the cancellation.</li>
     *   <li>Sends a notification to the client confirming the cancellation.</li>
     * </ol>
     *
     * @param request the subscription request containing the fund ID and email.
     * @return a {@link ResponseEntity} with a success message, or an error message with HTTP status 500.
     */
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

    /**
     * Retrieves the transaction history for the client.
     * <p>
     * This endpoint returns the list of transactions associated with the client identified
     * by the fixed client ID ("1").
     *
     * @return a {@link ResponseEntity} containing the list of transactions, or HTTP status 500 if an error occurs.
     */
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
