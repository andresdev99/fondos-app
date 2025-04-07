package com.fondos.fondos_app.controller;

import com.fondos.fondos_app.entity.Cliente;
import com.fondos.fondos_app.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for handling client-related operations.
 * <p>
 * This controller provides endpoints for:
 * <ul>
 *     <li>Retrieving a client's information by their ID.</li>
 *     <li>Updating the client's available amount.</li>
 *     <li>Updating a client's fund attribute (Fondo1 to Fondo5).</li>
 *     <li>Updating the client's notification type and associated email.</li>
 * </ul>
 * <p>
 * The endpoints use simple error handling and return appropriate HTTP status codes.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/client")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Retrieves the client information for a given client ID.
     *
     * @param clientId the unique identifier of the client.
     * @return a {@link ResponseEntity} containing the client data if found,
     *         a 404 status if the client is not found, or a 500 status on error.
     */
    @GetMapping("/{clientId}")
    public ResponseEntity<?> getClient(@PathVariable String clientId) {
        try {
            Cliente cliente = clienteService.getClient(clientId);

            if (cliente == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getStackTrace());
        }
    }

    /**
     * Updates the client's amount.
     *
     * <p>The request body must contain a key "monto" representing the new amount.
     *
     * @param clientId the unique identifier of the client.
     * @param body a map containing the key "monto" and its new integer value.
     * @return a {@link ResponseEntity} indicating the outcome of the update operation.
     */
    @PutMapping("/{clientId}/amount")
    public ResponseEntity<?> updateAmount(@PathVariable String clientId,
                                          @RequestBody Map<String, Integer> body) {
        if (!body.containsKey("monto")) {
            return ResponseEntity.badRequest().body("El cuerpo debe contener la clave 'monto'");
        }
        int newAmount = body.get("monto");
        try {
            clienteService.updateAmount(clientId, newAmount);
            return ResponseEntity.ok("Monto actualizado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar el monto: " + e.getMessage());
        }
    }

    /**
     * Updates the client's fund attribute.
     *
     * <p>The request body must contain a key "value" indicating the new boolean value for the specified fund.
     * The fund key must match the pattern "Fondo1" to "Fondo5".
     *
     * @param clientId the unique identifier of the client.
     * @param keyFund the key of the fund attribute (e.g., "Fondo1").
     * @param body a map containing the key "value" and its new boolean value.
     * @return a {@link ResponseEntity} indicating the outcome of the update operation.
     */
    @PutMapping("/{clientId}/fund/{keyFund}")
    public ResponseEntity<?> updateFund(@PathVariable String clientId,
                                        @PathVariable String keyFund,
                                        @RequestBody Map<String, Boolean> body) {
        if (!body.containsKey("value")) {
            return ResponseEntity.badRequest().body("El cuerpo debe contener la clave 'value'");
        }
        if (!keyFund.matches("Fondo[1-5]")) {
            return ResponseEntity.badRequest().body("El identificador del fondo debe ser 'Fondo1' a 'Fondo5'");
        }
        boolean newFundValue = body.get("value");
        try {
            clienteService.updateFund(clientId, keyFund, newFundValue);
            return ResponseEntity.ok("El atributo " + keyFund + " se actualizó exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar " + keyFund + ": " + e.getMessage());
        }
    }

    /**
     * Updates the client's notification type and email.
     *
     * <p>The request body must contain the keys "tipoNotificacion" and "email".
     *
     * @param clientId the unique identifier of the client.
     * @param body a map containing the new notification type under "tipoNotificacion" and the associated email.
     * @return a {@link ResponseEntity} indicating the outcome of the update operation.
     */
    @PutMapping("/{clientId}/notificationType")
    public ResponseEntity<?> actualizarTipoNotificacion(@PathVariable String clientId,
                                                        @RequestBody Map<String, String> body) {
        if (!body.containsKey("tipoNotificacion")) {
            return ResponseEntity.badRequest().body("Falta el parámetro 'tipoNotificacion' en el cuerpo de la solicitud.");
        }
        if (!body.containsKey("email")) {
            return ResponseEntity.badRequest().body("Falta el parámetro 'email' en el cuerpo de la solicitud.");
        }
        String newNotificationType = body.get("tipoNotificacion");
        String email = body.get("email");
        try {
            clienteService.updateNotificationType(clientId, newNotificationType, email);
            return ResponseEntity.ok("TipoNotificacion actualizado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar el tipo de notificación: " + e.getMessage());
        }
    }
}
