package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine;
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

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class WarehouseOrderLineMergerStreamTest extends StreamTestBase {

    /** Consumer record factories to send input data for testing */
    private ConsumerRecordFactory<byte[], byte[]> matchedWarehouseOrderLinesConsumerRecordFactory = null;
    private ConsumerRecordFactory<byte[], byte[]> recoveredWarehouseOrderLinesConsumerRecordFactory = null;

    /** Constants */
    private final String WAREHOUSE_ORDER_LINES_MATCHED_INPUT_TOPIC = "t.warehouse-order-lines-matched-input";
    private final String WAREHOUSE_ORDER_LINES_RECOVERED_INPUT_TOPIC = "t.warehouse-order-lines-recovered-input";
    private final String WAREHOUSE_ORDER_LINES_NEW_OUTPUT_TOPIC = "t.warehouse-order-lines-new-output";

    /**
     * Sets up the environment before testing the Kafka streams process
     */
    @Before
    public void setUp() {

        // Create the schema registry mock to avoid using the real schema registry
        final SchemaRegistryClient schemaRegistryClient = new MockSchemaRegistryClient();

        // Create the topology builder to get the topology (what we are testing)
        final WarehouseOrderLineMergerStream streamTopologyBuilder = new WarehouseOrderLineMergerStream(
                schemaRegistryClient,
                DUMMY_SCHEMA_REGISTRY_URL,
                WAREHOUSE_ORDER_LINES_MATCHED_INPUT_TOPIC,
                WAREHOUSE_ORDER_LINES_RECOVERED_INPUT_TOPIC,
                WAREHOUSE_ORDER_LINES_NEW_OUTPUT_TOPIC
        );

        final StreamsBuilder streamsBuilder = streamTopologyBuilder.startProcessing(new StreamsBuilder());
        final Topology topology = streamsBuilder.build();

        // Parent Setup to create the TopologyTestDriver
        parentSetUp(schemaRegistryClient, topology);

        // Create the warehouse order line consumer record factory to send records to the Kafka stream consumer
        matchedWarehouseOrderLinesConsumerRecordFactory = new ConsumerRecordFactory<>(
                WAREHOUSE_ORDER_LINES_MATCHED_INPUT_TOPIC,
                new ByteArraySerializer(),
                new ByteArraySerializer()
        );

        recoveredWarehouseOrderLinesConsumerRecordFactory = new ConsumerRecordFactory<>(
                WAREHOUSE_ORDER_LINES_MATCHED_INPUT_TOPIC,
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
     * Unit test to check merge works when only matched warehouse order lines
     */
    @Test
    public void testMergeWorksWithMatched() {

        // Create a input commercial order line
        WarehouseOrderLine inputValue = WarehouseOrderLine
                .newBuilder()
                .setUuid("101")
                .setCountry("102")
                .setDate(103L)
                .setProductUuid("104")
                .setProductLegacyId(105)
                .setProductName("106")
                .setProductBarCode("107")
                .setQuantity(108)
                .build();
        String inputKey = inputValue.getUuid();

        // Send the input commercial order line to matched topic
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_MATCHED_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_MATCHED_INPUT_TOPIC, inputValue);
        ConsumerRecord<byte[], byte[]> input = matchedWarehouseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Read the output topic
        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                WAREHOUSE_ORDER_LINES_NEW_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is an matched output
        Assert.assertNotNull(output);

        // Validate the output
        GenericRecord outputValue = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
        Assert.assertNotNull(outputValue);

        Assert.assertEquals("101", outputValue.get("uuid"));
        Assert.assertEquals("102", outputValue.get("country"));
        Assert.assertEquals(103L, outputValue.get("date"));
        Assert.assertEquals("104", outputValue.get("productUuid"));
        Assert.assertEquals(105, (int) outputValue.get("productLegacyId"));
        Assert.assertEquals("106", outputValue.get("productName"));
        Assert.assertEquals("107", outputValue.get("productBarCode"));
        Assert.assertEquals(108, (int) outputValue.get("quantity"));
    }

    /**
     * Unit test to check merge works when only recovered warehouse order lines
     */
    @Test
    public void testMergeWorksWithRecovered() {

        // Create a input commercial order line
        WarehouseOrderLine inputValue = WarehouseOrderLine
                .newBuilder()
                .setUuid("201")
                .setCountry("202")
                .setDate(203L)
                .setProductUuid("204")
                .setProductLegacyId(205)
                .setProductName("206")
                .setProductBarCode("207")
                .setQuantity(208)
                .build();
        String inputKey = inputValue.getUuid();

        // Send the input commercial order line to recovered topic
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_RECOVERED_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_RECOVERED_INPUT_TOPIC, inputValue);
        ConsumerRecord<byte[], byte[]> input = recoveredWarehouseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Read the output topic
        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                WAREHOUSE_ORDER_LINES_NEW_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is an recovered output
        Assert.assertNotNull(output);

        // Validate the output
        GenericRecord outputValue = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
        Assert.assertNotNull(outputValue);

        Assert.assertEquals("201", outputValue.get("uuid"));
        Assert.assertEquals("202", outputValue.get("country"));
        Assert.assertEquals(203L, outputValue.get("date"));
        Assert.assertEquals("204", outputValue.get("productUuid"));
        Assert.assertEquals(205, (int) outputValue.get("productLegacyId"));
        Assert.assertEquals("206", outputValue.get("productName"));
        Assert.assertEquals("207", outputValue.get("productBarCode"));
        Assert.assertEquals(208, (int) outputValue.get("quantity"));
    }

    /**
     * Unit test to check merge works when there are warehouse order lines in both streams
     */
    @Test
    public void testMergeWorksWithBothStreams() {

        // Create a input commercial order line for matched topic
        WarehouseOrderLine inputValue = WarehouseOrderLine
                .newBuilder()
                .setUuid("301")
                .setCountry("302")
                .setDate(303L)
                .setProductUuid("304")
                .setProductLegacyId(305)
                .setProductName("306")
                .setProductBarCode("307")
                .setQuantity(308)
                .build();
        String inputKey = inputValue.getUuid();

        // Send the input commercial order line to matched topic
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_MATCHED_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_MATCHED_INPUT_TOPIC, inputValue);
        ConsumerRecord<byte[], byte[]> input = matchedWarehouseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Create a input commercial order line for recovered topic
        inputValue = WarehouseOrderLine
                .newBuilder()
                .setUuid("401")
                .setCountry("402")
                .setDate(403L)
                .setProductUuid("404")
                .setProductLegacyId(405)
                .setProductName("406")
                .setProductBarCode("407")
                .setQuantity(408)
                .build();
        inputKey = inputValue.getUuid();

        // Send the input commercial order line to recovered topic
        inputKeyEncoded = keyAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_RECOVERED_INPUT_TOPIC, inputKey);
        inputValueEncoded = valueAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_RECOVERED_INPUT_TOPIC, inputValue);
        input = recoveredWarehouseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Read and validate the output topic
        ProducerRecord<byte[], byte[]> output;
        int outputCount = 0;
        do {
            output = testDriver.readOutput(
                    WAREHOUSE_ORDER_LINES_NEW_OUTPUT_TOPIC,
                    new ByteArrayDeserializer(),
                    new ByteArrayDeserializer()
            );
            if (output != null) {
                ++outputCount;

                GenericRecord outputValue = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
                Assert.assertNotNull(outputValue);

                String outputUuid = (String) outputValue.get("uuid");
                Assert.assertTrue(outputUuid.equals("301") || outputUuid.equals("401"));
            }
        }
        while (output != null);

        // Ensure there are two outputs
        Assert.assertEquals(2, outputCount);
    }
}
