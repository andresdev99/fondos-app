package com.fondos.fondos_app.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import com.fondos.fondos_app.entity.Fondo;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FondoRepository {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    public void save(Fondo fondo) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("FondoId", AttributeValue.builder().s(fondo.getFondoId()).build());
        item.put("nombre", AttributeValue.builder().s(fondo.getNombre()).build());
        item.put("montoMinimo", AttributeValue.builder().n(String.valueOf(fondo.getMontoMinimo())).build());
        item.put("categoria", AttributeValue.builder().s(fondo.getCategoria()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName("Fondos")
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }
}
