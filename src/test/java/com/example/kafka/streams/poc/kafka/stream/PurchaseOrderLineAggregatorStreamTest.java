package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.schemas.order.CommercialOrderLineSplit;
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
public class PurchaseOrderLineAggregatorStreamTest extends StreamTestBase {

    /** Consumer record factory to send input data for testing */
    private ConsumerRecordFactory<byte[], byte[]> commercialOrderLinesConsumerRecordFactory = null;

    /** Constants */
    private final String COMMERCIAL_ORDER_LINES_INPUT_TOPIC = "t.commercial-order-lines-input";
    private final String PURCHASE_ORDER_LINES_OUTPUT_TOPIC = "t.purchase-order-lines-input";

    /**
     * Sets up the environment before testing the Kafka streams process
     */
    @Before
    public void setUp() {

        // Create the schema registry mock to avoid using the real schema registry
        final SchemaRegistryClient schemaRegistryClient = new MockSchemaRegistryClient();

        // Create the topology builder to get the topology (what we are testing)
        final PurchaseOrderLineAggregatorStream streamTopologyBuilder = new PurchaseOrderLineAggregatorStream(
                schemaRegistryClient,
                DUMMY_SCHEMA_REGISTRY_URL,
                COMMERCIAL_ORDER_LINES_INPUT_TOPIC,
                PURCHASE_ORDER_LINES_OUTPUT_TOPIC
        );

        final StreamsBuilder streamsBuilder = streamTopologyBuilder.startProcessing(new StreamsBuilder());
        final Topology topology = streamsBuilder.build();

        // Parent Setup to create the TopologyTestDriver
        parentSetUp(schemaRegistryClient, topology);

        // Create the purchase line order consumer record factory to send records to the Kafka stream consumer
        commercialOrderLinesConsumerRecordFactory = new ConsumerRecordFactory<>(
                COMMERCIAL_ORDER_LINES_INPUT_TOPIC,
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
     * Unit test to generate one purchase order line from a single commercial order line
     */
    @Test
    public void testGenerateOnePurchaseOrderLineFromOneCommercialOrderLine() {

        // Create a input commercial order line
        CommercialOrderLineSplit inputValue = CommercialOrderLineSplit
                .newBuilder()
                .setUuid("col1")
                .setCommercialOrderUuid("com1")
                .setCommercialOrderDatetime(946681200015L)
                .setShippingCountry("ES")
                .setMemberUuid("mem1")
                .setProductUuid("prod1")
                .setProductName("prod1n")
                .setProductType("prod1t")
                .setProductBarCode("prod1b")
                .setProductPrice(100f)
                .setOrderLinePrice(120f)
                .setQuantity(1)
                .build();
        String inputKey = inputValue.getUuid();

        // Send the input commercial order line
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(COMMERCIAL_ORDER_LINES_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(COMMERCIAL_ORDER_LINES_INPUT_TOPIC, inputValue);
        ConsumerRecord<byte[], byte[]> input = commercialOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Read the output purchase order line
        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                PURCHASE_ORDER_LINES_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is an output
        Assert.assertNotNull(output);

        // Validate the output
        GenericRecord outputValue = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
        Assert.assertNotNull(outputValue);

        Assert.assertNotNull(outputValue.get("uuid"));
        Assert.assertEquals("ES-2000-01-01-prod1", outputValue.get("aggregationKey"));
        Assert.assertEquals("ES", outputValue.get("country"));
        Assert.assertEquals(946681200000L, outputValue.get("date"));
        Assert.assertEquals("prod1", outputValue.get("productUuid"));
        Assert.assertEquals("prod1n", outputValue.get("productName"));
        Assert.assertEquals("prod1t", outputValue.get("productType"));
        Assert.assertEquals("prod1b", outputValue.get("productBarCode"));
        Assert.assertEquals(100, (float) outputValue.get("productPrice"), 0.001);
        Assert.assertEquals(1, outputValue.get("quantity"));
    }

    /**
     * Unit test to generate one purchase order line from a multiple commercial order lines with the same product
     */
    @Test
    public void testGenerateOnePurchaseOrderLineFromMultipleCommercialOrderLines() {

        // Create a input commercial order line #1
        CommercialOrderLineSplit inputValue = CommercialOrderLineSplit
                .newBuilder()
                .setUuid("col1")
                .setCommercialOrderUuid("com1")
                .setCommercialOrderDatetime(946681200015L)
                .setShippingCountry("ES")
                .setMemberUuid("mem1")
                .setProductUuid("prod1")
                .setProductName("prod1n")
                .setProductType("prod1t")
                .setProductBarCode("prod1b")
                .setProductPrice(100f)
                .setOrderLinePrice(120f)
                .setQuantity(1)
                .build();
        String inputKey = inputValue.getUuid();

        // Send the input commercial order line #1
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(COMMERCIAL_ORDER_LINES_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(COMMERCIAL_ORDER_LINES_INPUT_TOPIC, inputValue);
        ConsumerRecord<byte[], byte[]> input = commercialOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Create a input commercial order line #2
        inputValue = CommercialOrderLineSplit
                .newBuilder(inputValue)
                .setUuid("col2")
                .setCommercialOrderUuid("com2")
                .setCommercialOrderDatetime(946681200100L)
                .setShippingCountry("ES")
                .setOrderLinePrice(121f)
                .setQuantity(2)
                .build();
        inputKey = inputValue.getUuid();

        // Send the input commercial order line #2
        inputKeyEncoded = keyAvroSerializer.serialize(COMMERCIAL_ORDER_LINES_INPUT_TOPIC, inputKey);
        inputValueEncoded = valueAvroSerializer.serialize(COMMERCIAL_ORDER_LINES_INPUT_TOPIC, inputValue);
        input = commercialOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Create a input commercial order line #3
        inputValue = CommercialOrderLineSplit
                .newBuilder(inputValue)
                .setUuid("col2")
                .setCommercialOrderUuid("com2")
                .setCommercialOrderDatetime(946681201000L)
                .setShippingCountry("ES")
                .setOrderLinePrice(122f)
                .setQuantity(5)
                .build();
        inputKey = inputValue.getUuid();

        // Send the input commercial order line #3
        inputKeyEncoded = keyAvroSerializer.serialize(COMMERCIAL_ORDER_LINES_INPUT_TOPIC, inputKey);
        inputValueEncoded = valueAvroSerializer.serialize(COMMERCIAL_ORDER_LINES_INPUT_TOPIC, inputValue);
        input = commercialOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Read the last message of the output purchase order line
        int outputCount = 0;
        ProducerRecord<byte[], byte[]> output = null;
        ProducerRecord<byte[], byte[]> tempOutput = testDriver.readOutput(
                PURCHASE_ORDER_LINES_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        while (null != tempOutput) {
            outputCount++;
            output = tempOutput;
            tempOutput = testDriver.readOutput(
                    PURCHASE_ORDER_LINES_OUTPUT_TOPIC,
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
        Assert.assertEquals("ES-2000-01-01-prod1", outputValue.get("aggregationKey"));
        Assert.assertEquals("ES", outputValue.get("country"));
        Assert.assertEquals(946681200000L, outputValue.get("date"));
        Assert.assertEquals("prod1", outputValue.get("productUuid"));
        Assert.assertEquals("prod1n", outputValue.get("productName"));
        Assert.assertEquals("prod1t", outputValue.get("productType"));
        Assert.assertEquals("prod1b", outputValue.get("productBarCode"));
        Assert.assertEquals(100f, (float) outputValue.get("productPrice"), 0.001);
        Assert.assertEquals(8, outputValue.get("quantity"));
    }

    /**
     * Unit test to generate two purchase order line from a two commercial order lines with the different products
     */
    @Test
    public void testGenerateTwoPurchaseOrderLineFromTwoCommercialOrderLines() {

        // Create a input commercial order line #1
        CommercialOrderLineSplit inputValue = CommercialOrderLineSplit
                .newBuilder()
                .setUuid("col1")
                .setCommercialOrderUuid("com1")
                .setCommercialOrderDatetime(946681200015L)
                .setShippingCountry("ES")
                .setMemberUuid("mem1")
                .setProductUuid("prod1")
                .setProductName("prod1n")
                .setProductType("prod1t")
                .setProductBarCode("prod1b")
                .setProductPrice(100f)
                .setOrderLinePrice(120f)
                .setQuantity(5)
                .build();
        String inputKey = inputValue.getUuid();

        // Send the input commercial order line #1
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(COMMERCIAL_ORDER_LINES_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(COMMERCIAL_ORDER_LINES_INPUT_TOPIC, inputValue);
        ConsumerRecord<byte[], byte[]> input = commercialOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Create a input commercial order line #2
        inputValue = CommercialOrderLineSplit
                .newBuilder(inputValue)
                .setUuid("col2")
                .setProductUuid("prod2")
                .setProductName("prod2n")
                .setProductType("prod2t")
                .setProductBarCode("prod2b")
                .setProductPrice(80f)
                .setOrderLinePrice(90f)
                .setQuantity(6)
                .build();
        inputKey = inputValue.getUuid();

        // Send the input commercial order line #2
        inputKeyEncoded = keyAvroSerializer.serialize(COMMERCIAL_ORDER_LINES_INPUT_TOPIC, inputKey);
        inputValueEncoded = valueAvroSerializer.serialize(COMMERCIAL_ORDER_LINES_INPUT_TOPIC, inputValue);
        input = commercialOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Read output message #1
        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                PURCHASE_ORDER_LINES_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there two output message #1
        Assert.assertNotNull(output);

        // Validate the output #1
        GenericRecord outputValue = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
        Assert.assertNotNull(outputValue);

        Assert.assertNotNull(outputValue.get("uuid"));
        Assert.assertEquals("ES-2000-01-01-prod1", outputValue.get("aggregationKey"));
        Assert.assertEquals("ES", outputValue.get("country"));
        Assert.assertEquals(946681200000L, outputValue.get("date"));
        Assert.assertEquals("prod1", outputValue.get("productUuid"));
        Assert.assertEquals("prod1n", outputValue.get("productName"));
        Assert.assertEquals("prod1t", outputValue.get("productType"));
        Assert.assertEquals("prod1b", outputValue.get("productBarCode"));
        Assert.assertEquals(100f, (float) outputValue.get("productPrice"), 0.001);
        Assert.assertEquals(5, outputValue.get("quantity"));

        // Read output message #2
        output = testDriver.readOutput(
                PURCHASE_ORDER_LINES_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there two output message #2
        Assert.assertNotNull(output);

        // Validate the output #2
        outputValue = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
        Assert.assertNotNull(outputValue);

        Assert.assertNotNull(outputValue.get("uuid"));
        Assert.assertEquals("ES-2000-01-01-prod2", outputValue.get("aggregationKey"));
        Assert.assertEquals("ES", outputValue.get("country"));
        Assert.assertEquals(946681200000L, outputValue.get("date"));
        Assert.assertEquals("prod2", outputValue.get("productUuid"));
        Assert.assertEquals("prod2n", outputValue.get("productName"));
        Assert.assertEquals("prod2t", outputValue.get("productType"));
        Assert.assertEquals("prod2b", outputValue.get("productBarCode"));
        Assert.assertEquals(80f, (float) outputValue.get("productPrice"), 0.001);
        Assert.assertEquals(6, outputValue.get("quantity"));
    }
}
