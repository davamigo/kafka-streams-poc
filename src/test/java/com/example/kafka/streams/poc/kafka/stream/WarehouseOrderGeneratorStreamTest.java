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

import java.util.List;

@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class WarehouseOrderGeneratorStreamTest extends StreamTestBase {

    /** Consumer record factory to send input data for testing */
    private ConsumerRecordFactory<byte[], byte[]> warehouseOrderLinesConsumerRecordFactory = null;

    /** Constants */
    private final String WAREHOUSE_ORDER_LINES_INPUT_TOPIC = "t.warehouse-order-lines-input";
    private final String WAREHOUSE_ORDERS_OUTPUT_TOPIC = "t.warehouse-orders-output";

    /**
     * Sets up the environment before testing the Kafka streams process
     */
    @Before
    public void setUp() {

        // Create the schema registry mock to avoid using the real schema registry
        final SchemaRegistryClient schemaRegistryClient = new MockSchemaRegistryClient();

        // Create the topology builder to get the topology (what we are testing)
        final WarehouseOrderGeneratorStream streamTopologyBuilder = new WarehouseOrderGeneratorStream(
                schemaRegistryClient,
                DUMMY_SCHEMA_REGISTRY_URL,
                WAREHOUSE_ORDER_LINES_INPUT_TOPIC,
                WAREHOUSE_ORDERS_OUTPUT_TOPIC
        );

        final StreamsBuilder streamsBuilder = streamTopologyBuilder.startProcessing(new StreamsBuilder());
        final Topology topology = streamsBuilder.build();

        // Parent Setup to create the TopologyTestDriver
        parentSetUp(schemaRegistryClient, topology);

        // Create the warehouse line order consumer record factory to send records to the Kafka stream consumer
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
     * Unit test with one warehouse order line converting in one warehouse order
     */
    @Test
    public void testGenerateOneWarehouseOrderFromOneWarehouseOrderLine() {

        // Create a warehouse order line
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

        // Send the warehouse order to the topic
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputValue);
        ConsumerRecord<byte[], byte[]> input = warehouseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Read the output
        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                WAREHOUSE_ORDERS_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is an output
        Assert.assertNotNull(output);

        // Validate the output
        GenericRecord outputValue = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
        Assert.assertNotNull(outputValue);

        Assert.assertNotNull(outputValue.get("uuid"));
        Assert.assertEquals("102-1970-01-01", outputValue.get("aggregationKey"));
        Assert.assertEquals("102", outputValue.get("country"));
        Assert.assertEquals(103L, outputValue.get("date"));

        List<GenericRecord> outputValueLines = (List<GenericRecord>) outputValue.get("lines");
        Assert.assertEquals(1, outputValueLines.size());

        GenericRecord outputValueLine0 = outputValueLines.get(0);
        Assert.assertNotNull(outputValueLine0.get("uuid"));
        Assert.assertEquals("104", outputValueLine0.get("productUuid"));
        Assert.assertEquals(105, outputValueLine0.get("productLegacyId"));
        Assert.assertEquals("106", outputValueLine0.get("productName"));
        Assert.assertEquals("107", outputValueLine0.get("productBarCode"));
        Assert.assertEquals(108, outputValueLine0.get("quantity"));
    }

    /**
     * Unit test with three warehouse order line converting in one warehouse order with three lines
     */
    @Test
    public void testGenerateOneWarehouseOrderFromMultipleWarehouseOrderLines() {

        // Create a warehouse order line #1
        WarehouseOrderLine inputValue = WarehouseOrderLine
                .newBuilder()
                .setUuid("uuid1")
                .setCountry("ES")
                .setDate(1546300800000L)
                .setProductUuid("prod1")
                .setProductLegacyId(1111)
                .setProductName("prod1-name")
                .setProductBarCode("prod1-barcode")
                .setQuantity(6)
                .build();
        String inputKey = inputValue.getUuid();

        // Send the warehouse order #1 to the topic
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputValue);
        testDriver.pipeInput(warehouseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded));

        // Create a warehouse order line #2
        inputValue = WarehouseOrderLine
                .newBuilder(inputValue)
                .setUuid("uuid2")
                .setProductUuid("prod2")
                .setProductLegacyId(2222)
                .setProductName("prod2-name")
                .setProductBarCode("prod2-barcode")
                .setQuantity(4)
                .build();
        inputKey = inputValue.getUuid();

        // Send the warehouse order #2 to the topic
        inputKeyEncoded = keyAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputKey);
        inputValueEncoded = valueAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputValue);
        testDriver.pipeInput(warehouseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded));

        // Create a warehouse order line #3
        inputValue = WarehouseOrderLine
                .newBuilder(inputValue)
                .setUuid("uuid3")
                .setProductUuid("prod3")
                .setProductLegacyId(3333)
                .setProductName("prod3-name")
                .setProductBarCode("prod3-barcode")
                .setQuantity(1)
                .build();
        inputKey = inputValue.getUuid();

        // Send the warehouse order #3 to the topic
        inputKeyEncoded = keyAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputKey);
        inputValueEncoded = valueAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputValue);
        testDriver.pipeInput(warehouseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded));

        // Read the last message of the output
        int outputCount = 0;
        ProducerRecord<byte[], byte[]> output = null;
        ProducerRecord<byte[], byte[]> tempOutput = testDriver.readOutput(
                WAREHOUSE_ORDERS_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        while (null != tempOutput) {
            outputCount++;
            output = tempOutput;
            tempOutput = testDriver.readOutput(
                    WAREHOUSE_ORDERS_OUTPUT_TOPIC,
                    new ByteArrayDeserializer(),
                    new ByteArrayDeserializer()
            );
        }

        // Ensure there is an output
        Assert.assertNotNull(output);
        Assert.assertEquals(3, outputCount);

        // Validate the output
        GenericRecord outputValue = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
        Assert.assertNotNull(outputValue);

        Assert.assertNotNull(outputValue.get("uuid"));
        Assert.assertEquals("ES-2019-01-01", outputValue.get("aggregationKey"));
        Assert.assertEquals("ES", outputValue.get("country"));
        Assert.assertEquals(1546300800000L, outputValue.get("date"));

        List<GenericRecord> outputValueLines = (List<GenericRecord>) outputValue.get("lines");
        Assert.assertEquals(3, outputValueLines.size());
    }

    /**
     * Unit test last order line with the same product replaces old values
     */
    @Test
    public void testOrderLinesWithSameProductReplacesOldOrderLines() {

        // Create a warehouse order line #1
        WarehouseOrderLine inputValue = WarehouseOrderLine
                .newBuilder()
                .setUuid("uuid1")
                .setCountry("ES")
                .setDate(1546300800000L)
                .setProductUuid("prod1")
                .setProductLegacyId(1111)
                .setProductName("prod1")
                .setProductBarCode("prod1")
                .setQuantity(5)
                .build();
        String inputKey = inputValue.getUuid();

        // Send the warehouse order #1 to the topic
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputValue);
        testDriver.pipeInput(warehouseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded));

        // Create a warehouse order line #2
        inputValue = WarehouseOrderLine
                .newBuilder(inputValue)
                .setQuantity(7)
                .build();
        inputKey = inputValue.getUuid();

        // Send the warehouse order #2 to the topic
        inputKeyEncoded = keyAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputKey);
        inputValueEncoded = valueAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputValue);
        testDriver.pipeInput(warehouseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded));

        // Create a warehouse order line #3
        inputValue = WarehouseOrderLine
                .newBuilder(inputValue)
                .setQuantity(12)
                .build();
        inputKey = inputValue.getUuid();

        // Send the warehouse order #3 to the topic
        inputKeyEncoded = keyAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputKey);
        inputValueEncoded = valueAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_INPUT_TOPIC, inputValue);
        testDriver.pipeInput(warehouseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded));

        // Read the last message of the output
        int outputCount = 0;
        ProducerRecord<byte[], byte[]> output = null;
        ProducerRecord<byte[], byte[]> newOutput = testDriver.readOutput(
                WAREHOUSE_ORDERS_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        while (null != newOutput) {
            outputCount++;
            output = newOutput;
            newOutput = testDriver.readOutput(
                    WAREHOUSE_ORDERS_OUTPUT_TOPIC,
                    new ByteArrayDeserializer(),
                    new ByteArrayDeserializer()
            );
        }

        // Ensure there is an output
        Assert.assertNotNull(output);
        Assert.assertEquals(3, outputCount);

        // Validate the output
        GenericRecord outputValue = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
        Assert.assertNotNull(outputValue);

        Assert.assertNotNull(outputValue.get("uuid"));
        Assert.assertEquals("ES-2019-01-01", outputValue.get("aggregationKey"));
        Assert.assertEquals("ES", outputValue.get("country"));
        Assert.assertEquals(1546300800000L, outputValue.get("date"));

        List<GenericRecord> outputValueLines = (List<GenericRecord>) outputValue.get("lines");
        Assert.assertEquals(1, outputValueLines.size());

        GenericRecord outputValueLine0 = outputValueLines.get(0);
        Assert.assertEquals("prod1", outputValueLine0.get("productUuid"));
        Assert.assertEquals(1111, outputValueLine0.get("productLegacyId"));
        Assert.assertEquals("prod1", outputValueLine0.get("productName"));
        Assert.assertEquals("prod1", outputValueLine0.get("productBarCode"));
        Assert.assertEquals(12, outputValueLine0.get("quantity"));
    }
}
