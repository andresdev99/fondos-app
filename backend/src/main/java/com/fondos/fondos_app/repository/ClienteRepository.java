package com.fondos.fondos_app.repository;

import com.fondos.fondos_app.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository for accessing and manipulating client data in the DynamoDB table "Clientes".
 *
 * <p>This repository provides methods to retrieve, update, and save {@link Cliente} objects in DynamoDB.
 * It handles operations such as fetching a client by ID, updating client attributes (like "Monto", fund flags,
 * and notification type), and persisting a new client record.</p>
 *
 * <p>It uses the AWS SDK for Java v2 {@link DynamoDbClient} to communicate with DynamoDB.</p>
 *
 * @see com.fondos.fondos_app.entity.Cliente
 */
@Repository
public class ClienteRepository {

    private static final String TABLE_NAME = "Clientes";

    @Autowired
    private DynamoDbClient dynamoDbClient;

    /**
     * Retrieves a client from the DynamoDB table based on the provided client ID.
     *
     * @param clienteId the unique identifier of the client.
     * @return the {@link Cliente} object if found; otherwise, {@code null}.
     */
    public Cliente findByClienteId(String clienteId) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("ClienteId", AttributeValue.builder().s(clienteId).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();

        var response = dynamoDbClient.getItem(request);
        Map<String, AttributeValue> item = response.item();

        if (item == null || item.isEmpty()) {
            return null;
        }

        // Map columns from the DynamoDB item to the Cliente entity
        Cliente cliente = new Cliente();
        cliente.setClienteId(item.get("ClienteId").s());
        cliente.setEmail(item.containsKey("Email") ? item.get("Email").s() : "");
        cliente.setCedula(item.containsKey("Cedula") ? item.get("Cedula").s() : null);
        cliente.setFondo1(item.containsKey("Fondo1") ? item.get("Fondo1").bool() : false);
        cliente.setFondo2(item.containsKey("Fondo2") ? item.get("Fondo2").bool() : false);
        cliente.setFondo3(item.containsKey("Fondo3") ? item.get("Fondo3").bool() : false);
        cliente.setFondo4(item.containsKey("Fondo4") ? item.get("Fondo4").bool() : false);
        cliente.setFondo5(item.containsKey("Fondo5") ? item.get("Fondo5").bool() : false);
        cliente.setMonto(item.containsKey("Monto") ? Integer.parseInt(item.get("Monto").n()) : 0);
        cliente.setNombre(item.containsKey("Nombre") ? item.get("Nombre").s() : null);
        cliente.setTipoNotificacion(item.containsKey("TipoNotificacion") ? item.get("TipoNotificacion").s() : null);

        return cliente;
    }

    /**
     * Updates the "Monto" (amount) attribute of a client.
     *
     * @param clienteId the unique identifier of the client.
     * @param newMonto  the new amount to be set.
     */
    public void updateMonto(String clienteId, int newMonto) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("ClienteId", AttributeValue.builder().s(clienteId).build());

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":newMonto", AttributeValue.builder().n(String.valueOf(newMonto)).build());

        // Update the "Monto" column in the DynamoDB table
        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .updateExpression("SET Monto = :newMonto")
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        dynamoDbClient.updateItem(updateRequest);
    }

    /**
     * Updates one of the client's fund attributes (e.g., Fondo1, Fondo2, etc.) in the DynamoDB table.
     *
     * @param clienteId the unique identifier of the client.
     * @param fondoKey  the key of the fund attribute to update (e.g., "Fondo1").
     * @param newValue  the new boolean value to be set for the specified fund attribute.
     */
    public void updateFondo(String clienteId, String fondoKey, boolean newValue) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("ClienteId", AttributeValue.builder().s(clienteId).build());

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":newVal", AttributeValue.builder().bool(newValue).build());

        // Build the update expression dynamically based on the provided fund key
        String updateExpression = "SET " + fondoKey + " = :newVal";

        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .updateExpression(updateExpression)
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        dynamoDbClient.updateItem(updateRequest);
    }

    /**
     * Updates the client's notification type and email in the DynamoDB table.
     *
     * @param clienteId         the unique identifier of the client.
     * @param newTipoNotificacion the new notification type.
     * @param email             the new email address to associate with the notification.
     */
    public void updateTipoNotificacion(String clienteId, String newTipoNotificacion, String email) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("ClienteId", AttributeValue.builder().s(clienteId).build());

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":newTipo", AttributeValue.builder().s(newTipoNotificacion).build());
        expressionAttributeValues.put(":newEmail", AttributeValue.builder().s(email).build());

        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .updateExpression("SET TipoNotificacion = :newTipo, Email = :newEmail")
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        dynamoDbClient.updateItem(updateRequest);
    }

    /**
     * Saves a new client record to the DynamoDB table.
     *
     * @param cliente the {@link Cliente} object to be saved.
     */
    public void save(Cliente cliente) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("ClienteId", AttributeValue.builder().s(cliente.getClienteId()).build());
        item.put("Cedula", AttributeValue.builder().s(cliente.getCedula()).build());
        item.put("Fondo1", AttributeValue.builder().bool(cliente.isFondo1()).build());
        item.put("Fondo2", AttributeValue.builder().bool(cliente.isFondo2()).build());
        item.put("Fondo3", AttributeValue.builder().bool(cliente.isFondo3()).build());
        item.put("Fondo4", AttributeValue.builder().bool(cliente.isFondo4()).build());
        item.put("Fondo5", AttributeValue.builder().bool(cliente.isFondo5()).build());
        item.put("Email", AttributeValue.builder().s(cliente.getEmail()).build());
        item.put("Monto", AttributeValue.builder().n(String.valueOf(cliente.getMonto())).build());
        item.put("Nombre", AttributeValue.builder().s(cliente.getNombre()).build());
        item.put("TipoNotificacion", AttributeValue.builder().s(cliente.getTipoNotificacion()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }
}
