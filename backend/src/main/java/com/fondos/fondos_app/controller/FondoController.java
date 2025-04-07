package com.fondos.fondos_app.controller;

import com.fondos.fondos_app.entity.Fondo;
import com.fondos.fondos_app.service.FondoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling fund-related operations.
 * <p>
 * This controller provides endpoints for:
 * <ul>
 *     <li>Retrieving a list of all available funds.</li>
 *     <li>Retrieving a single fund by its unique identifier.</li>
 * </ul>
 * <p>
 * Cross-origin requests are allowed from <code>http://localhost:5173</code>.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/fund")
public class FondoController {

    @Autowired
    private FondoService fondoService;

    /**
     * Retrieves all funds from the system.
     *
     * @return a {@link ResponseEntity} containing the list of funds if available,
     *         or a 204 No Content status if no funds are found.
     */
    @GetMapping("/funds")
    public ResponseEntity<List<Fondo>> getAllFondos() {
        List<Fondo> funds = fondoService.getAllFunds();
        if (funds.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(funds);
    }

    /**
     * Retrieves a single fund by its unique identifier.
     *
     * @param id the unique identifier of the fund.
     * @return a {@link ResponseEntity} containing the fund if found,
     *         or a 404 Not Found status if no fund with the given id exists.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Fondo> getFondoById(@PathVariable String id) {
        Fondo fund = fondoService.getFund(id);
        if (fund == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fund);
    }
}
