package com.fondos.fondos_app.service;

import com.fondos.fondos_app.entity.Cliente;
import com.fondos.fondos_app.entity.Fondo;
import com.fondos.fondos_app.entity.Transaccion;
import com.fondos.fondos_app.repository.ClienteRepository;
import com.fondos.fondos_app.repository.TransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransaccionServiceTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private TransaccionService transaccionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test successful registration for "apertura"
    @Test
    public void testRegisterTransactionAperturaSuccess() {
        // Given a client with sufficient amount and a fund
        Cliente client = new Cliente();
        client.setClienteId("client1");
        client.setMonto(5000); // sufficient funds

        Fondo fund = new Fondo("fondo1", "Fondo A", 1000, "Categoria1");

        // When registering a transaction of type "apertura"
        transaccionService.registerTransaction(client, fund, "apertura");

        // Then client's amount should be deducted by the fund's minimum amount
        assertEquals(4000, client.getMonto());

        // And both the client and a transaction should be saved
        verify(clienteRepository, times(1)).save(client);

        ArgumentCaptor<Transaccion> captor = ArgumentCaptor.forClass(Transaccion.class);
        verify(transaccionRepository, times(1)).save(captor.capture());
        Transaccion savedTx = captor.getValue();

        // Check that the transaction has the correct values
        assertEquals("client1", savedTx.getClienteId());
        assertEquals("apertura", savedTx.getTipo());
        assertEquals("fondo1", savedTx.getFondoId());
        // The transaction id should start with "TRANS#" and a date should be set
        assertTrue(savedTx.getTransaccionId().startsWith("TRANS#"));
        assertNotNull(savedTx.getFecha());
    }

    // Test failure when client funds are insufficient for "apertura"
    @Test
    public void testRegisterTransactionAperturaInsufficientFunds() {
        // Given a client with insufficient funds
        Cliente client = new Cliente();
        client.setClienteId("client1");
        client.setMonto(500); // less than required

        Fondo fund = new Fondo("fondo1", "Fondo A", 1000, "Categoria1");

        // Expect an IllegalArgumentException due to insufficient funds
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                transaccionService.registerTransaction(client, fund, "apertura")
        );
        assertTrue(exception.getMessage().contains("No tiene saldo disponible para vincularse al fondo"));
        // Make sure no saves were attempted
        verify(clienteRepository, never()).save(any());
        verify(transaccionRepository, never()).save(any());
    }

    // Test successful registration for "cancelacion"
    @Test
    public void testRegisterTransactionCancelSuccess() {
        // Given a client and a fund for cancellation, we add the fund's minimum back to the client's amount
        Cliente client = new Cliente();
        client.setClienteId("client1");
        client.setMonto(2000);

        Fondo fund = new Fondo("fondo1", "Fondo A", 1000, "Categoria1");

        transaccionService.registerTransaction(client, fund, "cancelacion");

        // Client's amount should increase by the fund's minimum
        assertEquals(3000, client.getMonto());
        verify(clienteRepository, times(1)).save(client);

        ArgumentCaptor<Transaccion> captor = ArgumentCaptor.forClass(Transaccion.class);
        verify(transaccionRepository, times(1)).save(captor.capture());
        Transaccion savedTx = captor.getValue();

        // Verify transaction fields
        assertEquals("client1", savedTx.getClienteId());
        assertEquals("cancelacion", savedTx.getTipo());
        assertEquals("fondo1", savedTx.getFondoId());
        assertTrue(savedTx.getTransaccionId().startsWith("TRANS#"));
        assertNotNull(savedTx.getFecha());
    }

    // Test retrieval of transaction history
    @Test
    public void testGetHistory() {
        Transaccion tx1 = new Transaccion("client1", "TRANS#1", "apertura", "fondo1", "2025-04-06T10:00:00Z");
        Transaccion tx2 = new Transaccion("client1", "TRANS#2", "cancelacion", "fondo1", "2025-04-06T11:00:00Z");
        List<Transaccion> history = Arrays.asList(tx1, tx2);
        when(transaccionRepository.findByClienteId("client1")).thenReturn(history);

        List<Transaccion> result = transaccionService.getHistory("client1");
        assertEquals(2, result.size());
        assertEquals("TRANS#1", result.get(0).getTransaccionId());
        assertEquals("cancelacion", result.get(1).getTipo());
    }
}
