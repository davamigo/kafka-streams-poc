package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLine;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class WarehouseOrderLineGeneratorStreamTest extends StreamTestBase {

    /** Consumer record factories to send input data for testing */
    private ConsumerRecordFactory<byte[], byte[]> purchaseOrderLinesConsumerRecordFactory = null;

    /** Constants */
    private final String PURCHASE_ORDER_LINES_INPUT_TOPIC = "t.purchase-order-lines-input";
    private final String WAREHOUSE_ORDER_LINES_OUTPUT_TOPIC = "t.warehouse-order-lines-output";

    /**
     * Sets up the environment before testing the Kafka streams process
     */
    @Before
    public void setUp() {

        // Create the schema registry mock to avoid using the real schema registry
        final SchemaRegistryClient schemaRegistryClient = new MockSchemaRegistryClient();

        // Create the topology builder to get the topology (what we are testing)
        final WarehouseOrderLineGeneratorStream streamTopologyBuilder = new WarehouseOrderLineGeneratorStream(
                schemaRegistryClient,
                DUMMY_SCHEMA_REGISTRY_URL,
                PURCHASE_ORDER_LINES_INPUT_TOPIC,
                WAREHOUSE_ORDER_LINES_OUTPUT_TOPIC
        );

        final StreamsBuilder streamsBuilder = streamTopologyBuilder.startProcessing(new StreamsBuilder());
        final Topology topology = streamsBuilder.build();

        // Parent Setup to create the TopologyTestDriver
        parentSetUp(schemaRegistryClient, topology);

        // Create the purchase order line consumer record factory to send records to the Kafka stream consumer
        purchaseOrderLinesConsumerRecordFactory = new ConsumerRecordFactory<>(
                PURCHASE_ORDER_LINES_INPUT_TOPIC,
                new ByteArraySerializer(),
                new ByteArraySerializer()
        );
    }

    /**
     * Clears the environment after testing the Kafka streams process
     */
    @After
    public void tearDown() {
        parentTearDown();
    }

    /**
     * Unit test to check one purchase order line generates one warehouse order line
     */
    @Test
    public void testOnePurchaseOrderLineGeneratesOneWarehouseOrderLine() {

        // Create a input purchase order line
        PurchaseOrderLine inputValue = PurchaseOrderLine
                .newBuilder()
                .setUuid("pol1")
                .setAggregationKey("aggr1")
                .setCountry("ES")
                .setDate(946681200015L)
                .setProductUuid("prod1")
                .setProductName("prod1n")
                .setProductType("prod1t")
                .setProductBarCode("prod1b")
                .setProductPrice(100f)
                .setQuantity(1)
                .build();
        String inputKey = inputValue.getUuid();

        // Send the input purchase order line
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, inputValue);
        ConsumerRecord<byte[], byte[]> input = purchaseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Read the output topic
        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                WAREHOUSE_ORDER_LINES_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is an output
        Assert.assertNotNull(output);

        // Validate the output
        GenericRecord outputValue = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
        Assert.assertNotNull(outputValue);

        Assert.assertNotNull(outputValue.get("uuid"));
        Assert.assertEquals("ES", outputValue.get("country"));
        Assert.assertEquals(946681200015L, outputValue.get("date"));
        Assert.assertEquals("prod1", outputValue.get("productUuid"));
        Assert.assertNull(outputValue.get("productLegacyId"));
        Assert.assertEquals("prod1n", outputValue.get("productName"));
        Assert.assertEquals("prod1b", outputValue.get("productBarCode"));
        Assert.assertEquals(1, (int) outputValue.get("quantity"));
    }

    /**
     * Unit test to check multiple purchase order lines with the same aggregation key generate multiple warehouse
     * order line with the same UUID
     */
    @Test
    public void testMultiplePurchaseOrderLinesWithSameKeyGenerateMultipleWarehouseOrderLineWithSameKey() {

        // Create a input purchase order line #1
        PurchaseOrderLine inputValue = PurchaseOrderLine
                .newBuilder()
                .setUuid("pol1")
                .setAggregationKey("aggr1")
                .setCountry("ES")
                .setDate(946681200015L)
                .setProductUuid("prod1")
                .setProductName("prod1n")
                .setProductType("prod1t")
                .setProductBarCode("prod1b")
                .setProductPrice(100f)
                .setQuantity(1)
                .build();
        String inputKey = inputValue.getUuid();

        // Send the input purchase order line #1
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, inputValue);
        ConsumerRecord<byte[], byte[]> input = purchaseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Create a input purchase order line #2
        inputValue = PurchaseOrderLine
                .newBuilder(inputValue)
                .setQuantity(3)
                .build();
        inputKey = inputValue.getUuid();

        // Send the input purchase order line #2
        inputKeyEncoded = keyAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, inputKey);
        inputValueEncoded = valueAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, inputValue);
        input = purchaseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Read the output topic
        ProducerRecord<byte[], byte[]> output;
        GenericRecord outputValue = null;
        int outputCount = 0;
        String uuid = null;
        do {
            output = testDriver.readOutput(
                    WAREHOUSE_ORDER_LINES_OUTPUT_TOPIC,
                    new ByteArrayDeserializer(),
                    new ByteArrayDeserializer()
            );
            if (output != null) {
                ++outputCount;

                outputValue = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
                Assert.assertNotNull(outputValue);

                if (null == uuid) {
                    uuid = (String) outputValue.get("uuid");
                }
                else {
                    Assert.assertEquals(uuid, (String) outputValue.get("uuid"));
                }
            }
        }
        while (output != null);

        // Ensure there are two outputs
        Assert.assertEquals(2, outputCount);
        Assert.assertEquals(3, (int) outputValue.get("quantity"));
    }
}
