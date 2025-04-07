package com.fondos.fondos_app.repository;

import com.fondos.fondos_app.entity.Transaccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransaccionRepositoryTest {

    @Mock
    private DynamoDbClient dynamoDbClient;

    @InjectMocks
    private TransaccionRepository transaccionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test saving a transaction builds the correct PutItemRequest.
    @Test
    public void testSave() {
        Transaccion tx = new Transaccion("client1", "TRANS#123", "apertura", "fondo1", "2025-04-06T10:00:00Z");
        when(dynamoDbClient.putItem(any(PutItemRequest.class)))
                .thenReturn(PutItemResponse.builder().build());

        transaccionRepository.save(tx);

        ArgumentCaptor<PutItemRequest> captor = ArgumentCaptor.forClass(PutItemRequest.class);
        verify(dynamoDbClient).putItem(captor.capture());
        PutItemRequest request = captor.getValue();

        assertEquals("Transacciones", request.tableName());
        Map<String, AttributeValue> item = request.item();
        assertEquals("client1", item.get("ClienteId").s());
        assertEquals("TRANS#123", item.get("TransaccionId").s());
        assertEquals("apertura", item.get("Tipo").s());
        assertEquals("fondo1", item.get("FondoId").s());
        assertEquals("2025-04-06T10:00:00Z", item.get("Fecha").s());
    }

    // Test findByClienteId maps DynamoDB items to Transaccion objects.
    @Test
    public void testFindByClienteId() {
        String clientId = "client1";
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("ClienteId", AttributeValue.builder().s(clientId).build());
        item.put("TransaccionId", AttributeValue.builder().s("TRANS#123").build());
        item.put("Tipo", AttributeValue.builder().s("apertura").build());
        item.put("FondoId", AttributeValue.builder().s("fondo1").build());
        item.put("Fecha", AttributeValue.builder().s("2025-04-06T10:00:00Z").build());

        List<Map<String, AttributeValue>> items = Collections.singletonList(item);
        QueryResponse queryResponse = QueryResponse.builder().items(items).build();
        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(queryResponse);

        List<Transaccion> result = transaccionRepository.findByClienteId(clientId);
        assertEquals(1, result.size());
        Transaccion tx = result.get(0);
        assertEquals(clientId, tx.getClienteId());
        assertEquals("TRANS#123", tx.getTransaccionId());
        assertEquals("apertura", tx.getTipo());
        assertEquals("fondo1", tx.getFondoId());
        assertEquals("2025-04-06T10:00:00Z", tx.getFecha());
    }
}
