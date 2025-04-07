package com.fondos.fondos_app.service;

import com.fondos.fondos_app.entity.Cliente;
import com.fondos.fondos_app.entity.Fondo;
import com.fondos.fondos_app.entity.Transaccion;
import com.fondos.fondos_app.repository.ClienteRepository;
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

    @Autowired
    private ClienteRepository clienteRepository;

    public void registerTransaction(Cliente client, Fondo fund, String type) {
        String transactionId = "TRANS#" + UUID.randomUUID();
        String date = Instant.now().toString();
        if (type.equalsIgnoreCase("apertura")) {
            if(client.getMonto() < fund.getMontoMinimo()) {
                throw new IllegalArgumentException("No tiene saldo disponible para vincularse al fondo " + fund.getNombre());
            }
            client.setMonto(client.getMonto() - fund.getMontoMinimo());
        } else {
            client.setMonto(client.getMonto() + fund.getMontoMinimo());
        }
        clienteRepository.save(client);

        Transaccion transaccion = new Transaccion(client.getClienteId(), transactionId, type, fund.getFondoId(), date);
        transaccionRepository.save(transaccion);
    }

    public List<Transaccion> getHistory(String clientId) {
        return transaccionRepository.findByClienteId(clientId);
    }
}
