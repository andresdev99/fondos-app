package com.fondos.fondos_app.controller;

import com.fondos.fondos_app.entity.Cliente;
import com.fondos.fondos_app.repository.ClienteRepository;
import com.fondos.fondos_app.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/client")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Endpoint para obtener la información completa del cliente
    @GetMapping("/{clientId}")
    public ResponseEntity<Cliente> getClient(@PathVariable String clientId) {
        try{
            Cliente cliente = clienteService.getClient(clientId);

            if (cliente == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(cliente);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    @PutMapping("/{clientId}/amount")
    public ResponseEntity<String> updateAmount(@PathVariable String clientId,
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

    // Endpoint para actualizar el valor booleano de un fondo específico
    // Se espera un JSON con: { "value": true }
    @PutMapping("/{clientId}/fund/{keyFund}")
    public ResponseEntity<String> updateFund(@PathVariable String clientId,
                                                  @PathVariable String keyFund,
                                                  @RequestBody Map<String, Boolean> body) {
        if (!body.containsKey("value")) {
            return ResponseEntity.badRequest().body("El cuerpo debe contener la clave 'value'");
        }
        // Validar que fondoKey sea "Fondo1" a "Fondo5"
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

    @PutMapping("/{clientId}/notificationType")
    public ResponseEntity<String> actualizarTipoNotificacion(@PathVariable String clientId,
                                                             @RequestBody Map<String, String> body) {
        if (!body.containsKey("tipoNotificacion")) {
            return ResponseEntity.badRequest().body("Falta el parámetro 'tipoNotificacion' en el cuerpo de la solicitud.");
        }
        String newNotificationType = body.get("tipoNotificacion");
        try {
            clienteService.updateNotificationType(clientId, newNotificationType);
            return ResponseEntity.ok("TipoNotificacion actualizado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar el tipo de notificación: " + e.getMessage());
        }
    }


}
