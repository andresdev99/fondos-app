package com.fondos.fondos_app.service;

import com.fondos.fondos_app.entity.Fondo;
import com.fondos.fondos_app.repository.FondoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FondoService {

    @Autowired
    private FondoRepository fondoRepository;

    public Fondo getFund(String fondoId) {
        return fondoRepository.findById(fondoId);
    }

    public String getFundName(String fondoId) {
        Fondo fondo = fondoRepository.findById(fondoId);
        if (fondo != null) {
            return fondo.getNombre();
        } else {
            return null;
        }
    }

    public List<Fondo> getAllFunds() {
        return fondoRepository.findAll();
    }
}
