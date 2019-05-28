package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.service.api.LegacyProductIdsApiInterface;
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

import java.util.Optional;

@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class WarehouseOrderLineProductRecoveryStreamTest extends StreamTestBase {

    /** Consumer record factories to send input data for testing */
    private ConsumerRecordFactory<byte[], byte[]> warehouseOrderLinesConsumerRecordFactory = null;

    /** Legacy product Api mock (used by the stream) */
    private final LegacyProductIdsApiMock legacyProductIdsApi = new LegacyProductIdsApiMock();

    /** Constants */
    private final String WAREHOUSE_ORDER_LINES_INPUT_TOPIC = "t.warehouse-order-lines-unmatched-input";
    private final String WAREHOUSE_ORDER_LINES_RECOVERED_OUTPUT_TOPIC = "t.warehouse-order-lines-recovered-output";
    private final String WAREHOUSE_ORDER_LINES_FAILED_OUTPUT_TOPIC = "t.warehouse-order-lines-failed-output";

    /**
     * Sets up the environment before testing the Kafka streams process
     */
    @Before
    public void setUp() {

        // Create the schema registry mock to avoid using the real schema registry
        final SchemaRegistryClient schemaRegistryClient = new MockSchemaRegistryClient();

        // Create the topology builder to get the topology (what we are testing)
        final WarehouseOrderLineProductRecoveryStream streamTopologyBuilder = new WarehouseOrderLineProductRecoveryStream(
                schemaRegistryClient,
                DUMMY_SCHEMA_REGISTRY_URL,
                WAREHOUSE_ORDER_LINES_INPUT_TOPIC,
                WAREHOUSE_ORDER_LINES_RECOVERED_OUTPUT_TOPIC,
                WAREHOUSE_ORDER_LINES_FAILED_OUTPUT_TOPIC,
                legacyProductIdsApi
        );

        final StreamsBuilder streamsBuilder = streamTopologyBuilder.startProcessing(new StreamsBuilder());
        final Topology topology = streamsBuilder.build();

        // Parent Setup to create the TopologyTestDriver
        parentSetUp(schemaRegistryClient, topology);

        // Create the warehouse order line consumer record factory to send records to the Kafka stream consumer
        warehouseOrderLinesConsumerRecordFactory = new ConsumerRecordFactory<>(
                WAREHOUSE_ORDER_LINES_INPUT_TOPIC,
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
     * Unit test to check the product legacy id. found
     */
    @Test
    public void testWhenProductLegacyIdFound() {

        // Set legacy value
        legacyProductIdsApi.setValue(111);

        // Create a input commercial order line
        WarehouseOrderLine inputValue = WarehouseOrderLine
                .newBuilder()
                .setUuid("wol1")
                .setCountry("ES")
                .setDate(946681200015L)
                .setProductUuid("prod1")
                .setProductName("prod1n")
                .setProductBarCode("prod1b")
                .setQuantity(1)
                .build();
        String inputKey = inputValue.getUuid();

        // Send the input commercial order line
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputValue);
        ConsumerRecord<byte[], byte[]> input = warehouseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Read the matched output topic
        ProducerRecord<byte[], byte[]> recoveredOutput = testDriver.readOutput(
                WAREHOUSE_ORDER_LINES_RECOVERED_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Read the unmatched output topic
        ProducerRecord<byte[], byte[]> failedOutput = testDriver.readOutput(
                WAREHOUSE_ORDER_LINES_FAILED_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is an unmatched output
        Assert.assertNotNull(recoveredOutput);

        // Ensure there is no matched output
        Assert.assertNull(failedOutput);

        // Validate the output
        GenericRecord recoveredOutputValue = (GenericRecord) valueAvroDeserializer.deserialize(recoveredOutput.topic(), recoveredOutput.value());
        Assert.assertNotNull(recoveredOutputValue);

        Assert.assertNotNull(recoveredOutputValue.get("uuid"));
        Assert.assertEquals("ES", recoveredOutputValue.get("country"));
        Assert.assertEquals(946681200015L, recoveredOutputValue.get("date"));
        Assert.assertEquals("prod1", recoveredOutputValue.get("productUuid"));
        Assert.assertEquals(111, (int) recoveredOutputValue.get("productLegacyId"));
        Assert.assertEquals("prod1n", recoveredOutputValue.get("productName"));
        Assert.assertEquals("prod1b", recoveredOutputValue.get("productBarCode"));
        Assert.assertEquals(1, (int) recoveredOutputValue.get("quantity"));
    }

    /**
     * Unit test to check the product legacy id. not found
     */
    @Test
    public void testWhenProductLegacyIdNotFound() {

        // Set legacy value
        legacyProductIdsApi.setValue(null);

        // Create a input commercial order line
        WarehouseOrderLine inputValue = WarehouseOrderLine
                .newBuilder()
                .setUuid("wol1")
                .setCountry("ES")
                .setDate(946681200015L)
                .setProductUuid("prod1")
                .setProductName("prod1n")
                .setProductBarCode("prod1b")
                .setQuantity(1)
                .build();
        String inputKey = inputValue.getUuid();

        // Send the input commercial order line
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputValue);
        ConsumerRecord<byte[], byte[]> input = warehouseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Read the matched output topic
        ProducerRecord<byte[], byte[]> recoveredOutput = testDriver.readOutput(
                WAREHOUSE_ORDER_LINES_RECOVERED_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Read the unmatched output topic
        ProducerRecord<byte[], byte[]> failedOutput = testDriver.readOutput(
                WAREHOUSE_ORDER_LINES_FAILED_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is no matched output
        Assert.assertNull(recoveredOutput);

        // Ensure there is an unmatched output
        Assert.assertNotNull(failedOutput);

        // Validate the output
        GenericRecord failedOutputValue = (GenericRecord) valueAvroDeserializer.deserialize(failedOutput.topic(), failedOutput.value());
        Assert.assertNotNull(failedOutputValue);

        Assert.assertNotNull(failedOutputValue.get("uuid"));
        Assert.assertEquals("ES", failedOutputValue.get("country"));
        Assert.assertEquals(946681200015L, failedOutputValue.get("date"));
        Assert.assertEquals("prod1", failedOutputValue.get("productUuid"));
        Assert.assertNull(failedOutputValue.get("productLegacyId"));
        Assert.assertEquals("prod1n", failedOutputValue.get("productName"));
        Assert.assertEquals("prod1b", failedOutputValue.get("productBarCode"));
        Assert.assertEquals(1, (int) failedOutputValue.get("quantity"));
    }

    /**
     * Inner class used as mock for LegacyProductIdsInterface
     */
    private class LegacyProductIdsApiMock implements LegacyProductIdsApiInterface {

        private Integer value = null;

        @Override
        public Optional<Integer> getLegacyId(String productUuid) {
            if (null == value) {
                return Optional.empty();
            }
            else {
                return Optional.of(value);
            }
        }

        void setValue(Integer value) {
            this.value = value;
        }
    };

}
