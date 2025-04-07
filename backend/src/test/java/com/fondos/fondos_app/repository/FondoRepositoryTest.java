package com.fondos.fondos_app.repository;

import com.fondos.fondos_app.entity.Fondo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FondoRepositoryTest {

    @Mock
    private DynamoDbClient dynamoDbClient;

    @InjectMocks
    private FondoRepository fondoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test findById when the item exists in DynamoDB.
    @Test
    void testFindByIdWhenItemExists() {
        String fondoId = "fondo123";
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("FondoId", AttributeValue.builder().s(fondoId).build());
        item.put("Nombre", AttributeValue.builder().s("Fondo A").build());
        item.put("MontoMinimo", AttributeValue.builder().n("1000").build());
        item.put("Categoria", AttributeValue.builder().s("Categoria1").build());

        GetItemResponse response = GetItemResponse.builder().item(item).build();
        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(response);

        Fondo result = fondoRepository.findById(fondoId);
        assertNotNull(result);
        assertEquals(fondoId, result.getFondoId());
        assertEquals("Fondo A", result.getNombre());
        assertEquals(1000, result.getMontoMinimo());
        assertEquals("Categoria1", result.getCategoria());
    }

    // Test findById when no item is found.
    @Test
    void testFindByIdWhenItemDoesNotExist() {
        String fondoId = "nonexistent";
        GetItemResponse response = GetItemResponse.builder().item(Collections.emptyMap()).build();
        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(response);

        Fondo result = fondoRepository.findById(fondoId);
        assertNull(result);
    }

    // Test findAll which should return a list of funds.
    @Test
    void testFindAll() {
        Map<String, AttributeValue> item1 = new HashMap<>();
        item1.put("FondoId", AttributeValue.builder().s("fondo1").build());
        item1.put("Nombre", AttributeValue.builder().s("Fondo A").build());
        item1.put("MontoMinimo", AttributeValue.builder().n("1000").build());
        item1.put("Categoria", AttributeValue.builder().s("Categoria1").build());

        Map<String, AttributeValue> item2 = new HashMap<>();
        item2.put("FondoId", AttributeValue.builder().s("fondo2").build());
        item2.put("Nombre", AttributeValue.builder().s("Fondo B").build());
        item2.put("MontoMinimo", AttributeValue.builder().n("2000").build());
        item2.put("Categoria", AttributeValue.builder().s("Categoria2").build());

        List<Map<String, AttributeValue>> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        ScanResponse scanResponse = ScanResponse.builder().items(items).build();
        when(dynamoDbClient.scan(any(ScanRequest.class))).thenReturn(scanResponse);

        List<Fondo> fondos = fondoRepository.findAll();
        assertEquals(2, fondos.size());
        Fondo first = fondos.get(0);
        assertEquals("fondo1", first.getFondoId());
        assertEquals("Fondo A", first.getNombre());
        assertEquals(1000, first.getMontoMinimo());
        assertEquals("Categoria1", first.getCategoria());

        Fondo second = fondos.get(1);
        assertEquals("fondo2", second.getFondoId());
        assertEquals("Fondo B", second.getNombre());
        assertEquals(2000, second.getMontoMinimo());
        assertEquals("Categoria2", second.getCategoria());
    }
}
