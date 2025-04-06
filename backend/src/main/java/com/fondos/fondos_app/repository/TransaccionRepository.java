package com.fondos.fondos_app.repository;

import com.fondos.fondos_app.entity.Transaccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TransaccionRepository {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    public void save(Transaccion transaccion) {
        // Build the item map using the new AttributeValue builder
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("ClienteId", AttributeValue.builder().s(transaccion.getClienteId()).build());
        item.put("TransaccionId", AttributeValue.builder().s(transaccion.getTransaccionId()).build());
        item.put("Tipo", AttributeValue.builder().s(transaccion.getTipo()).build());
        item.put("FondoId", AttributeValue.builder().s(transaccion.getFondoId()).build());
        item.put("Fecha", AttributeValue.builder().s(transaccion.getFecha()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName("Transacciones")
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }
    public List<Transaccion> findByClienteId(String clienteId) {

        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":cid", AttributeValue.builder().s(clienteId).build());

        QueryRequest queryRequest = QueryRequest.builder()
                .tableName("Transacciones")
                .keyConditionExpression("ClienteId = :cid")
                .expressionAttributeValues(expressionValues)
                .build();

        QueryResponse response = dynamoDbClient.query(queryRequest);
        List<Transaccion> transacciones = new ArrayList<>();
        List<Map<String, AttributeValue>> items = response.items();
        for (Map<String, AttributeValue> item : items) {
            Transaccion t = new Transaccion();
            t.setClienteId(item.get("ClienteId").s());
            t.setTransaccionId(item.get("TransaccionId").s());
            t.setTipo(item.get("Tipo").s());
            t.setFondoId(item.get("FondoId").s());
            t.setFecha(item.get("Fecha").s());
            transacciones.add(t);
        }
        return transacciones;
    }
}
