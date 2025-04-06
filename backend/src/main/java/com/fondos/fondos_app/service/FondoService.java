package com.fondos.fondos_app.service;

import com.fondos.fondos_app.entity.Fondo;
import com.fondos.fondos_app.repository.FondoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FondoService {

    @Autowired
    private FondoRepository fondoRepository;

    public void crearFondo(String FondoId, String nombre, int montoMinimo, String categoria) {
        Fondo fondo = new Fondo(FondoId, nombre, montoMinimo, categoria);
        fondoRepository.save(fondo);
    }
}
