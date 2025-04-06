package com.fondos.fondos_app.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import com.fondos.fondos_app.entity.Fondo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FondoRepository {

    private static final String TABLE_NAME = "Fondos";

    @Autowired
    private DynamoDbClient dynamoDbClient;

    public Fondo findById(String id) {
        // Build the key map (assuming the key attribute in DynamoDB is "Id")
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("FondoId", AttributeValue.builder().s(id).build());

        // Create a GetItemRequest for the table
        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();

        // Execute the request
        Map<String, AttributeValue> item = dynamoDbClient.getItem(request).item();
        if (item == null || item.isEmpty()) {
            return null;
        }

        // Map the retrieved attributes to a Fondo object
        Fondo fondo = new Fondo();
        fondo.setFondoId(item.get("FondoId").s());
        fondo.setNombre(item.get("Nombre").s());
        fondo.setMontoMinimo(Integer.parseInt(item.get("MontoMinimo").n()));
        fondo.setCategoria(item.get("Categoria").s());
        return fondo;
    }

    // Retrieve all Fondos from the table
    public List<Fondo> findAll() {
        // Create a ScanRequest for the table
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .build();

        // Execute the scan operation
        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
        List<Map<String, AttributeValue>> items = scanResponse.items();

        // Map each item to a Fondo object and add it to a list
        List<Fondo> fondos = new ArrayList<>();
        for (Map<String, AttributeValue> item : items) {
            Fondo fondo = new Fondo();
            fondo.setFondoId(item.get("FondoId").s());
            fondo.setNombre(item.get("Nombre").s());
            fondo.setMontoMinimo(Integer.parseInt(item.get("MontoMinimo").n()));
            fondo.setCategoria(item.get("Categoria").s());
            fondos.add(fondo);
        }
        return fondos;
    }
}
