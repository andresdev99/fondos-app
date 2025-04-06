package com.fondos.fondos_app.service;

import com.fondos.fondos_app.entity.Transaccion;
import com.fondos.fondos_app.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    public void registerTransaction(String clientId, String type, String fundId) {
        String transactionId = "TRANS#" + UUID.randomUUID();
        String date = Instant.now().toString();

        Transaccion transaccion = new Transaccion(clientId, transactionId, type, fundId, date);
        transaccionRepository.save(transaccion);
    }

    public List<Transaccion> getHistory(String clientId) {
        return transaccionRepository.findByClienteId(clientId);
    }
}
