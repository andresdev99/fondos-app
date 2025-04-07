package com.fondos.fondos_app.service;

import com.fondos.fondos_app.entity.Cliente;
import com.fondos.fondos_app.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetClient() {
        String clientId = "client123";
        Cliente dummyCliente = new Cliente();
        dummyCliente.setClienteId(clientId);
        when(clienteRepository.findByClienteId(clientId)).thenReturn(dummyCliente);

        Cliente result = clienteService.getClient(clientId);
        assertNotNull(result);
        assertEquals(clientId, result.getClienteId());
    }

    @Test
    void testGetNotificationType() {
        String clientId = "client123";
        Cliente dummyCliente = new Cliente();
        dummyCliente.setClienteId(clientId);
        dummyCliente.setTipoNotificacion("EMAIL");
        when(clienteRepository.findByClienteId(clientId)).thenReturn(dummyCliente);

        String notificationType = clienteService.getNotificationType(clientId);
        assertEquals("EMAIL", notificationType);
    }

    @Test
    void testGetAmount() {
        String clientId = "client123";
        Cliente dummyCliente = new Cliente();
        dummyCliente.setClienteId(clientId);
        dummyCliente.setMonto(1000);
        when(clienteRepository.findByClienteId(clientId)).thenReturn(dummyCliente);

        int amount = clienteService.getAmount(clientId);
        assertEquals(1000, amount);
    }

    @Test
    void testGetAmountClientNotFound() {
        String clientId = "nonexistent";
        when(clienteRepository.findByClienteId(clientId)).thenReturn(null);

        int amount = clienteService.getAmount(clientId);
        assertEquals(0, amount);
    }

    @Test
    void testUpdateNotificationType() {
        String clientId = "client123";
        String newNotificationType = "SMS";
        String email = "test@example.com";

        // Call the service method.
        clienteService.updateNotificationType(clientId, newNotificationType, email);

        // Verify that the repository method was called.
        verify(clienteRepository, times(1)).updateTipoNotificacion(clientId, newNotificationType, email);
    }

    @Test
    void testUpdateAmount() {
        String clientId = "client123";
        int newAmount = 2000;

        clienteService.updateAmount(clientId, newAmount);
        verify(clienteRepository, times(1)).updateMonto(clientId, newAmount);
    }

    @Test
    void testUpdateFund() {
        String clientId = "client123";
        String fundKey = "Fondo1";
        boolean newValue = true;

        clienteService.updateFund(clientId, fundKey, newValue);
        verify(clienteRepository, times(1)).updateFondo(clientId, fundKey, newValue);
    }

    @Test
    void testSaveClient() {
        Cliente client = new Cliente();
        client.setClienteId("client123");

        clienteService.saveClient(client);
        verify(clienteRepository, times(1)).save(client);
    }
}
