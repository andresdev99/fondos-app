package com.fondos.fondos_app.repository;

import com.fondos.fondos_app.entity.Fondo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository for accessing and managing funds data stored in the DynamoDB table "Fondos".
 *
 * <p>This repository provides methods to retrieve a single {@link Fondo} by its ID or to retrieve all available funds.
 * It utilizes the AWS SDK for Java v2 {@link DynamoDbClient} for communicating with DynamoDB.</p>
 *
 * @see com.fondos.fondos_app.entity.Fondo
 */
@Repository
public class FondoRepository {

    private static final String TABLE_NAME = "Fondos";

    @Autowired
    private DynamoDbClient dynamoDbClient;

    /**
     * Retrieves a {@link Fondo} from the DynamoDB table using its unique identifier.
     *
     * <p>This method builds a key map with the given fund ID, creates a {@link GetItemRequest}, and executes the request.
     * If an item is found, the method maps the DynamoDB attributes to a {@link Fondo} object; otherwise, it returns null.</p>
     *
     * @param id the unique identifier of the fund.
     * @return the {@link Fondo} object if found; otherwise, {@code null}.
     */
    public Fondo findById(String id) {
        // Build the key map (assuming the key attribute in DynamoDB is "FondoId")
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

    /**
     * Retrieves all funds from the DynamoDB table "Fondos".
     *
     * <p>This method creates a {@link ScanRequest} for the table, executes the scan operation,
     * and maps each retrieved item to a {@link Fondo} object.</p>
     *
     * @return a {@link List} of {@link Fondo} objects; if no items are found, returns an empty list.
     */
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
