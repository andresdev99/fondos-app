package com.fondos.fondos_app.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
// We define dummy properties to simulate the environment values.
@TestPropertySource(properties = {
        "amazon.aws.accesskey=testAccessKey",
        "amazon.aws.secretkey=testSecretKey",
        "amazon.aws.region=us-east-1",
        "amazon.dynamodb.endpoint=localhost:8000"
})
public class DynamoDbConfigTest {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Test
    public void testDynamoDbClientBeanCreation() {
        // Make sure that the DynamoDbClient bean is created
        assertNotNull(dynamoDbClient, "DynamoDbClient bean should not be null");

        // Verify that the region is set correctly from the test properties
        assertEquals(Region.of("us-east-1"), dynamoDbClient.serviceClientConfiguration().region(), "Region should be us-east-1");

    }
}
