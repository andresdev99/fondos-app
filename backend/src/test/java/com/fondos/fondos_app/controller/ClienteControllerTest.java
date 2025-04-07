package com.fondos.fondos_app.controller;

import com.fondos.fondos_app.entity.Cliente;
import com.fondos.fondos_app.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    // GET /api/client/{clientId} - success case
    @Test
    public void testGetClientFound() throws Exception {
        String clientId = "1";
        Cliente dummyClient = new Cliente();
        dummyClient.setClienteId(clientId);
        dummyClient.setEmail("test@example.com");
        // Setup the mock service to return the dummy client when queried
        Mockito.when(clienteService.getClient(clientId)).thenReturn(dummyClient);

        mockMvc.perform(get("/api/client/{clientId}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(clientId))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    // GET /api/client/{clientId} - not found case
    @Test
    public void testGetClientNotFound() throws Exception {
        String clientId = "nonexistent";
        Mockito.when(clienteService.getClient(clientId)).thenReturn(null);

        mockMvc.perform(get("/api/client/{clientId}", clientId))
                .andExpect(status().isNotFound());
    }

    // PUT /api/client/{clientId}/amount - missing "monto" key
    @Test
    public void testUpdateAmountMissingKey() throws Exception {
        String clientId = "1";
        String requestBody = "{}"; // Empty JSON

        mockMvc.perform(put("/api/client/{clientId}/amount", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El cuerpo debe contener la clave 'monto'"));
    }

    // PUT /api/client/{clientId}/amount - success case
    @Test
    public void testUpdateAmountSuccess() throws Exception {
        String clientId = "1";
        int newAmount = 500;
        // Simulate service updating amount without throwing exception
        Mockito.doNothing().when(clienteService).updateAmount(clientId, newAmount);
        String requestBody = "{\"monto\": " + newAmount + "}";

        mockMvc.perform(put("/api/client/{clientId}/amount", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Monto actualizado exitosamente."));
    }

    // PUT /api/client/{clientId}/fund/{keyFund} - invalid keyFund
    @Test
    public void testUpdateFundInvalidKey() throws Exception {
        String clientId = "1";
        String invalidKeyFund = "Fondo6";
        String requestBody = "{\"value\": true}";

        mockMvc.perform(put("/api/client/{clientId}/fund/{keyFund}", clientId, invalidKeyFund)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El identificador del fondo debe ser 'Fondo1' a 'Fondo5'"));
    }

    // PUT /api/client/{clientId}/fund/{keyFund} - missing "value" key
    @Test
    public void testUpdateFundMissingValueKey() throws Exception {
        String clientId = "1";
        String keyFund = "Fondo1";
        String requestBody = "{}";

        mockMvc.perform(put("/api/client/{clientId}/fund/{keyFund}", clientId, keyFund)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El cuerpo debe contener la clave 'value'"));
    }

    // PUT /api/client/{clientId}/fund/{keyFund} - success case
    @Test
    public void testUpdateFundSuccess() throws Exception {
        String clientId = "1";
        String keyFund = "Fondo3";
        boolean newFundValue = true;
        Mockito.doNothing().when(clienteService).updateFund(clientId, keyFund, newFundValue);
        String requestBody = "{\"value\": true}";

        mockMvc.perform(put("/api/client/{clientId}/fund/{keyFund}", clientId, keyFund)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("El atributo " + keyFund + " se actualizó exitosamente."));
    }

    // PUT /api/client/{clientId}/notificationType - missing 'tipoNotificacion'
    @Test
    public void testUpdateNotificationTypeMissingTipo() throws Exception {
        String clientId = "1";
        String requestBody = "{\"email\": \"test@example.com\"}";

        mockMvc.perform(put("/api/client/{clientId}/notificationType", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Falta el parámetro 'tipoNotificacion' en el cuerpo de la solicitud."));
    }

    // PUT /api/client/{clientId}/notificationType - missing 'email'
    @Test
    public void testUpdateNotificationTypeMissingEmail() throws Exception {
        String clientId = "1";
        String requestBody = "{\"tipoNotificacion\": \"NEW_TYPE\"}";

        mockMvc.perform(put("/api/client/{clientId}/notificationType", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Falta el parámetro 'email' en el cuerpo de la solicitud."));
    }

    // PUT /api/client/{clientId}/notificationType - success case
    @Test
    public void testUpdateNotificationTypeSuccess() throws Exception {
        String clientId = "1";
        String newNotificationType = "NEW_TYPE";
        String email = "test@example.com";
        Mockito.doNothing().when(clienteService)
                .updateNotificationType(eq(clientId), eq(newNotificationType), eq(email));
        String requestBody = "{\"tipoNotificacion\": \"" + newNotificationType + "\", \"email\": \"" + email + "\"}";

        mockMvc.perform(put("/api/client/{clientId}/notificationType", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("TipoNotificacion actualizado exitosamente."));
    }
}
