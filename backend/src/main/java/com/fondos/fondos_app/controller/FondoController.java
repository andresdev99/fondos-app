package com.fondos.fondos_app.controller;

import com.fondos.fondos_app.entity.Fondo;
import com.fondos.fondos_app.repository.FondoRepository;
import com.fondos.fondos_app.service.FondoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/fund")
public class FondoController {

    @Autowired
    private FondoService fondoService;

    @GetMapping("/funds")
    public ResponseEntity<List<Fondo>> getAllFondos() {
        List<Fondo> funds = fondoService.getAllFunds();
        if (funds.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(funds);
    }

    // GET /api/fondos/{id} -> retrieve a single fund by its id
    @GetMapping("/{id}")
    public ResponseEntity<Fondo> getFondoById(@PathVariable String id) {
        Fondo fund = fondoService.getFund(id);
        if (fund == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fund);
    }
}
