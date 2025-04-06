package com.fondos.fondos_app.service;

import com.fondos.fondos_app.entity.Cliente;
import com.fondos.fondos_app.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente getClient(String clientId) {
        return clienteRepository.findByClienteId(clientId);
    }

    public String getNotificationType(String clientId) {
        Cliente cliente = clienteRepository.findByClienteId(clientId);
        return cliente.getTipoNotificacion();
    }

    public int getAmount(String clientId) {
        Cliente cliente = clienteRepository.findByClienteId(clientId);
        if (cliente == null) {
            return 0;
        }
        return cliente.getMonto();
    }

    public void updateNotificationType(String clientId, String newNotificationType) {
        clienteRepository.updateTipoNotificacion(clientId, newNotificationType);
    }


    public void updateAmount(String clientId, int newAmount) {
        clienteRepository.updateMonto(clientId, newAmount);
    }

    public void updateFund(String clientId, String fundKey, boolean newValue) {
        clienteRepository.updateFondo(clientId, fundKey, newValue);
    }

    public void saveClient(Cliente client) {
        clienteRepository.save(client);
    }
}
