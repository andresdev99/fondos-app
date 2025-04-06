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

@Repository
public class ClienteRepository {

    private static final String TABLE_NAME = "Clientes";

    @Autowired
    private DynamoDbClient dynamoDbClient;

    public Cliente findByClienteId(String clienteId) {
        // Clave de partición: "ClienteId"
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

        // Mapeo de columnas a la entidad
        Cliente cliente = new Cliente();
        cliente.setClienteId(item.get("ClienteId").s());
        cliente.setEmail(item.get("Email").s());
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

    public void updateMonto(String clienteId, int newMonto) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("ClienteId", AttributeValue.builder().s(clienteId).build());

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":newMonto", AttributeValue.builder().n(String.valueOf(newMonto)).build());

        // Actualizamos la columna "Monto"
        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .updateExpression("SET Monto = :newMonto")
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        dynamoDbClient.updateItem(updateRequest);
    }

    public void updateFondo(String clienteId, String fondoKey, boolean newValue) {
        // Ejemplo de fondoKey: "Fondo1", "Fondo2", ...
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("ClienteId", AttributeValue.builder().s(clienteId).build());

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":newVal", AttributeValue.builder().bool(newValue).build());

        // Actualizamos directamente la columna "Fondo1", "Fondo2", etc.
        String updateExpression = "SET " + fondoKey + " = :newVal";

        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .updateExpression(updateExpression)
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        dynamoDbClient.updateItem(updateRequest);
    }

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


    public void save(Cliente cliente) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("ClienteId", AttributeValue.builder().s(cliente.getClienteId()).build());
        item.put("Cedula", AttributeValue.builder().s(cliente.getCedula()).build());
        item.put("Fondo1", AttributeValue.builder().bool(cliente.isFondo1()).build());
        item.put("Fondo2", AttributeValue.builder().bool(cliente.isFondo2()).build());
        item.put("Fondo3", AttributeValue.builder().bool(cliente.isFondo3()).build());
        item.put("Fondo4", AttributeValue.builder().bool(cliente.isFondo4()).build());
        item.put("Fondo5", AttributeValue.builder().bool(cliente.isFondo5()).build());
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
