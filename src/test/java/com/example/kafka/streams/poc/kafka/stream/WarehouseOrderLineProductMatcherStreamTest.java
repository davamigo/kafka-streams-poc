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
public class WarehouseOrderLineProductMatcherStreamTest extends StreamTestBase {

    /** Consumer record factories to send input data for testing */
    private ConsumerRecordFactory<byte[], byte[]> purchaseOrderLinesConsumerRecordFactory = null;
    private ConsumerRecordFactory<byte[], byte[]> productsLegacyIdCacheConsumerRecordFactory = null;

    /** Constants */
    private final String PURCHASE_ORDER_LINES_INPUT_TOPIC = "t.purchase-order-lines-input";
    private final String PRODUCT_LEGACY_IDS_INPUT_TOPIC = "t.products-legacy-id-input";
    private final String WAREHOUSE_ORDER_LINES_MATCHED_OUTPUT_TOPIC = "t.warehouse-order-lines-matched-output";
    private final String WAREHOUSE_ORDER_LINES_UNMATCHED_OUTPUT_TOPIC = "t.warehouse-order-lines-unmatched-output";

    /**
     * Sets up the environment before testing the Kafka streams process
     */
    @Before
    public void setUp() {

        // Create the schema registry mock to avoid using the real schema registry
        final SchemaRegistryClient schemaRegistryClient = new MockSchemaRegistryClient();

        // Create the topology builder to get the topology (what we are testing)
        final WarehouseOrderLineProductMatcherStream streamTopologyBuilder = new WarehouseOrderLineProductMatcherStream(
                schemaRegistryClient,
                DUMMY_SCHEMA_REGISTRY_URL,
                PURCHASE_ORDER_LINES_INPUT_TOPIC,
                PRODUCT_LEGACY_IDS_INPUT_TOPIC,
                WAREHOUSE_ORDER_LINES_MATCHED_OUTPUT_TOPIC,
                WAREHOUSE_ORDER_LINES_UNMATCHED_OUTPUT_TOPIC
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

        // Create the product legacy id consumer record factory to send records to the Kafka stream consumer
        productsLegacyIdCacheConsumerRecordFactory = new ConsumerRecordFactory<>(
                PRODUCT_LEGACY_IDS_INPUT_TOPIC,
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
     * Unit test to check the purchase order line goes to unmatched when no product ids cache is empty
     */
    @Test
    public void testPurchaseOrderLineGoestoUnmatchedWhenProductIdsCacheIsEmpty() {

        // Create a input commercial order line
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

        // Send the input commercial order line
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, inputValue);
        ConsumerRecord<byte[], byte[]> input = purchaseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Read the matched output topic
        ProducerRecord<byte[], byte[]> matchedOutput = testDriver.readOutput(
                WAREHOUSE_ORDER_LINES_MATCHED_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Read the unmatched output topic
        ProducerRecord<byte[], byte[]> unmatchedOutput = testDriver.readOutput(
                WAREHOUSE_ORDER_LINES_UNMATCHED_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is no matched output
        Assert.assertNull(matchedOutput);

        // Ensure there is an unmatched output
        Assert.assertNotNull(unmatchedOutput);

        // Validate the output
        GenericRecord unmatchedOutputValue = (GenericRecord) valueAvroDeserializer.deserialize(unmatchedOutput.topic(), unmatchedOutput.value());
        Assert.assertNotNull(unmatchedOutputValue);

        Assert.assertNotNull(unmatchedOutputValue.get("uuid"));
        Assert.assertEquals("ES", unmatchedOutputValue.get("country"));
        Assert.assertEquals(946681200015L, unmatchedOutputValue.get("date"));
        Assert.assertEquals("prod1", unmatchedOutputValue.get("productUuid"));
        Assert.assertNull(unmatchedOutputValue.get("productLegacyId"));
        Assert.assertEquals("prod1n", unmatchedOutputValue.get("productName"));
        Assert.assertEquals("prod1b", unmatchedOutputValue.get("productBarCode"));
        Assert.assertEquals(1, (int) unmatchedOutputValue.get("quantity"));
    }

    /**
     * Unit test to check the purchase order line goes to matched when product id found in cache
     */
    @Test
    public void testPurchaseOrderLineGoestoMatchedWhenProductIdFoundInCache() {

        // Create a product Id
        int productInputValue = 1234567890;
        String productInputKey = "prod1";

        // Send the input commercial order line
        byte[] productInputKeyEncoded = keyAvroSerializer.serialize(PRODUCT_LEGACY_IDS_INPUT_TOPIC, productInputKey);
        byte[] productInputValueEncoded = valueAvroSerializer.serialize(PRODUCT_LEGACY_IDS_INPUT_TOPIC, productInputValue);
        ConsumerRecord<byte[], byte[]> productInput = productsLegacyIdCacheConsumerRecordFactory.create(productInputKeyEncoded, productInputValueEncoded);
        testDriver.pipeInput(productInput);

        // Create a input commercial order line
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

        // Send the input commercial order line
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, inputValue);
        ConsumerRecord<byte[], byte[]> input = purchaseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Read the matched output topic
        ProducerRecord<byte[], byte[]> matchedOutput = testDriver.readOutput(
                WAREHOUSE_ORDER_LINES_MATCHED_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Read the unmatched output topic
        ProducerRecord<byte[], byte[]> unmatchedOutput = testDriver.readOutput(
                WAREHOUSE_ORDER_LINES_UNMATCHED_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is an matched output
        Assert.assertNotNull(matchedOutput);

        // Ensure there is no unmatched output
        Assert.assertNull(unmatchedOutput);

        // Validate the output
        GenericRecord matchedOutputValue = (GenericRecord) valueAvroDeserializer.deserialize(matchedOutput.topic(), matchedOutput.value());
        Assert.assertNotNull(matchedOutputValue);

        Assert.assertNotNull(matchedOutputValue.get("uuid"));
        Assert.assertEquals("ES", matchedOutputValue.get("country"));
        Assert.assertEquals(946681200015L, matchedOutputValue.get("date"));
        Assert.assertEquals("prod1", matchedOutputValue.get("productUuid"));
        Assert.assertEquals(1234567890, (int) matchedOutputValue.get("productLegacyId"));
        Assert.assertEquals("prod1n", matchedOutputValue.get("productName"));
        Assert.assertEquals("prod1b", matchedOutputValue.get("productBarCode"));
        Assert.assertEquals(1, (int) matchedOutputValue.get("quantity"));
    }
}
