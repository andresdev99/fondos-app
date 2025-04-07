package com.fondos.fondos_app.repository;

import com.fondos.fondos_app.entity.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClienteRepositoryTest {

    @Mock
    private DynamoDbClient dynamoDbClient;

    @InjectMocks
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByClienteIdWhenItemExists() {
        String clientId = "1";
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("ClienteId", AttributeValue.builder().s(clientId).build());
        item.put("Email", AttributeValue.builder().s("test@example.com").build());
        item.put("Cedula", AttributeValue.builder().s("123456").build());
        item.put("Fondo1", AttributeValue.builder().bool(true).build());
        item.put("Fondo2", AttributeValue.builder().bool(false).build());
        item.put("Fondo3", AttributeValue.builder().bool(true).build());
        item.put("Fondo4", AttributeValue.builder().bool(false).build());
        item.put("Fondo5", AttributeValue.builder().bool(true).build());
        item.put("Monto", AttributeValue.builder().n("1000").build());
        item.put("Nombre", AttributeValue.builder().s("John Doe").build());
        item.put("TipoNotificacion", AttributeValue.builder().s("EMAIL").build());

        GetItemResponse response = GetItemResponse.builder().item(item).build();
        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(response);

        Cliente result = clienteRepository.findByClienteId(clientId);
        assertNotNull(result);
        assertEquals(clientId, result.getClienteId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("123456", result.getCedula());
        assertTrue(result.isFondo1());
        assertFalse(result.isFondo2());
        assertTrue(result.isFondo3());
        assertFalse(result.isFondo4());
        assertTrue(result.isFondo5());
        assertEquals(1000, result.getMonto());
        assertEquals("John Doe", result.getNombre());
        assertEquals("EMAIL", result.getTipoNotificacion());
    }

    @Test
    void testFindByClienteIdWhenNoItem() {
        String clientId = "1";
        GetItemResponse response = GetItemResponse.builder().item(new HashMap<>()).build();
        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(response);

        Cliente result = clienteRepository.findByClienteId(clientId);
        assertNull(result);
    }

    @Test
    void testUpdateMonto() {
        String clientId = "1";
        int newMonto = 1500;

        // Instead of doNothing(), return a dummy response
        when(dynamoDbClient.updateItem(any(UpdateItemRequest.class)))
                .thenReturn(UpdateItemResponse.builder().build());

        clienteRepository.updateMonto(clientId, newMonto);

        ArgumentCaptor<UpdateItemRequest> captor = ArgumentCaptor.forClass(UpdateItemRequest.class);
        verify(dynamoDbClient).updateItem(captor.capture());
        UpdateItemRequest capturedRequest = captor.getValue();

        assertEquals("Clientes", capturedRequest.tableName());
        // Check that the update expression contains "Monto = :newMonto"
        assertTrue(capturedRequest.updateExpression().contains("Monto = :newMonto"));
    }

    @Test
    void testUpdateFondo() {
        String clientId = "1";
        String fondoKey = "Fondo1";
        boolean newValue = true;

        when(dynamoDbClient.updateItem(any(UpdateItemRequest.class)))
                .thenReturn(UpdateItemResponse.builder().build());

        clienteRepository.updateFondo(clientId, fondoKey, newValue);

        ArgumentCaptor<UpdateItemRequest> captor = ArgumentCaptor.forClass(UpdateItemRequest.class);
        verify(dynamoDbClient).updateItem(captor.capture());
        UpdateItemRequest capturedRequest = captor.getValue();

        assertEquals("Clientes", capturedRequest.tableName());
        assertTrue(capturedRequest.updateExpression().contains(fondoKey + " = :newVal"));
    }

    @Test
    void testUpdateTipoNotificacion() {
        String clientId = "1";
        String newTipo = "SMS";
        String email = "test@example.com";

        when(dynamoDbClient.updateItem(any(UpdateItemRequest.class)))
                .thenReturn(UpdateItemResponse.builder().build());

        clienteRepository.updateTipoNotificacion(clientId, newTipo, email);

        ArgumentCaptor<UpdateItemRequest> captor = ArgumentCaptor.forClass(UpdateItemRequest.class);
        verify(dynamoDbClient).updateItem(captor.capture());
        UpdateItemRequest capturedRequest = captor.getValue();

        assertEquals("Clientes", capturedRequest.tableName());
        assertTrue(capturedRequest.updateExpression().contains("TipoNotificacion = :newTipo"));
    }

    @Test
    void testSave() {
        Cliente cliente = new Cliente();
        cliente.setClienteId("1");
        cliente.setCedula("123456");
        cliente.setFondo1(true);
        cliente.setFondo2(false);
        cliente.setFondo3(true);
        cliente.setFondo4(false);
        cliente.setFondo5(true);
        cliente.setEmail("test@example.com");
        cliente.setMonto(2000);
        cliente.setNombre("John Doe");
        cliente.setTipoNotificacion("EMAIL");

        when(dynamoDbClient.putItem(any(PutItemRequest.class)))
                .thenReturn(PutItemResponse.builder().build());

        clienteRepository.save(cliente);

        ArgumentCaptor<PutItemRequest> captor = ArgumentCaptor.forClass(PutItemRequest.class);
        verify(dynamoDbClient).putItem(captor.capture());
        PutItemRequest capturedRequest = captor.getValue();

        assertEquals("Clientes", capturedRequest.tableName());
        assertNotNull(capturedRequest.item());
        assertEquals("1", capturedRequest.item().get("ClienteId").s());
    }
}
