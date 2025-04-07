package com.fondos.fondos_app.controller;

import com.fondos.fondos_app.entity.Cliente;
import com.fondos.fondos_app.entity.Fondo;
import com.fondos.fondos_app.entity.Transaccion;
import com.fondos.fondos_app.dto.SuscripcionRequest;
import com.fondos.fondos_app.service.ClienteService;
import com.fondos.fondos_app.service.FondoService;
import com.fondos.fondos_app.service.NotificationService;
import com.fondos.fondos_app.service.TransaccionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransaccionController.class)
public class TransaccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransaccionService transaccionService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private FondoService fondoService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSubscribeSuccess() throws Exception {
        // Build a sample request (SuscripcionRequest should have fondoId and email)
        SuscripcionRequest request = new SuscripcionRequest();
        request.setFondoId("fondo1");
        request.setEmail("test@example.com");

        // Prepare dummy Cliente and Fondo returned by service calls.
        Cliente client = new Cliente();
        client.setClienteId("1");
        client.setTipoNotificacion("EMAIL");

        Fondo fund = new Fondo("fondo1", "Fondo A", 1000, "Categoria1");

        Mockito.when(clienteService.getClient("1")).thenReturn(client);
        Mockito.when(fondoService.getFund("fondo1")).thenReturn(fund);
        // For subscribe, we expect registerTransaction to be called with type "apertura"
        Mockito.doNothing().when(transaccionService).registerTransaction(client, fund, "apertura");
        // And a notification is sent.
        Mockito.doNothing().when(notificationService)
                .sendNotification(anyString(), anyString(), anyString());

        // Perform the POST request.
        mockMvc.perform(post("/api/transaction/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Suscripción realizada exitosamente"));
    }

    // Test /api/transaction/cancel endpoint
    @Test
    public void testCancelSubscriptionSuccess() throws Exception {
        SuscripcionRequest request = new SuscripcionRequest();
        request.setFondoId("fondo1");
        request.setEmail("test@example.com");

        Cliente client = new Cliente();
        client.setClienteId("1");
        client.setTipoNotificacion("EMAIL");

        Fondo fund = new Fondo("fondo1", "Fondo A", 1000, "Categoria1");

        Mockito.when(clienteService.getClient("1")).thenReturn(client);
        Mockito.when(fondoService.getFund("fondo1")).thenReturn(fund);
        // For cancel, registerTransaction is called with type "cancelacion"
        Mockito.doNothing().when(transaccionService).registerTransaction(client, fund, "cancelacion");
        Mockito.doNothing().when(notificationService)
                .sendNotification(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/transaction/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Cancelación realizada exitosamente"));
    }

    // Test GET /api/transaction/transactions endpoint
    @Test
    public void testGetHistory() throws Exception {
        Transaccion tx1 = new Transaccion("1", "TRANS#1", "apertura", "fondo1", "2025-04-06T10:00:00Z");
        Transaccion tx2 = new Transaccion("1", "TRANS#2", "cancelacion", "fondo1", "2025-04-06T11:00:00Z");
        List<Transaccion> history = Arrays.asList(tx1, tx2);
        Mockito.when(transaccionService.getHistory("1")).thenReturn(history);

        mockMvc.perform(get("/api/transaction/transactions"))
                .andExpect(status().isOk())
                // JSON property names will be in camelCase by default.
                .andExpect(jsonPath("$[0].clienteId").value("1"))
                .andExpect(jsonPath("$[0].transaccionId").value("TRANS#1"))
                .andExpect(jsonPath("$[0].tipo").value("apertura"))
                .andExpect(jsonPath("$[0].fondoId").value("fondo1"))
                .andExpect(jsonPath("$[0].fecha").value("2025-04-06T10:00:00Z"))
                .andExpect(jsonPath("$[1].transaccionId").value("TRANS#2"));
    }
}
