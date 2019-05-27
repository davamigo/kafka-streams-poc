package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLine;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.apache.avro.generic.GenericRecord;
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
public class PurchaseOrderGeneratorStreamTest extends StreamTestBase {

    /** Consumer recor factories to send input data for testing */
    private ConsumerRecordFactory<byte[], byte[]> purchaseOrderLinesConsumerRecordFactory = null;

    /** Constants */
    private final String PURCHASE_ORDER_LINES_INPUT_TOPIC = "t.purchase-order-lines-input";
    private final String PURCHASE_ORDERS_OUTPUT_TOPIC = "t.purchase-orders-output";

    /**
     * Sets up the environment before testing the Kafka streams process
     */
    @Before
    public void setUp() {

        // Create the schema registry mock to avoid using the real schema registry
        final SchemaRegistryClient schemaRegistryClient = new MockSchemaRegistryClient();

        // Create the topology builder to get the topology (what we are testing)
        final PurchaseOrderGeneratorStream streamTopologyBuilder = new PurchaseOrderGeneratorStream(
                schemaRegistryClient,
                DUMMY_SCHEMA_REGISTRY_URL,
                PURCHASE_ORDER_LINES_INPUT_TOPIC,
                PURCHASE_ORDERS_OUTPUT_TOPIC
        );

        final StreamsBuilder streamsBuilder = streamTopologyBuilder.startProcessing(new StreamsBuilder());
        final Topology topology = streamsBuilder.build();

        // Parent Setup to create the TopologyTestDriver
        parentSetUp(schemaRegistryClient, topology);

        // Create the purchase line order consumer record factory to send records to the Kafka stream consumer
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
     * Unit test with one purchase order line converting in one purchase order
     */
    @Test
    public void testGenerateOnePurchaseOrderFromOnePurchaseOrderLine() {

        // Create a purchase order line
        PurchaseOrderLine purchaseOrderLine = PurchaseOrderLine
                .newBuilder()
                .setUuid("101")
                .setAggregationKey("102")
                .setCountry("103")
                .setDate(104L)
                .setProductUuid("105")
                .setProductName("106")
                .setProductType("107")
                .setProductBarCode("108")
                .setProductPrice(109f)
                .setQuantity(110)
                .build();
        String purchaseOrderLineUuid = purchaseOrderLine.getUuid();

        // Send the purchase order to the topic
        byte[] bytePurchaseOrderKey = keyAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, purchaseOrderLineUuid);
        byte[] bytePurchaseOrderValue = valueAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, purchaseOrderLine);
        testDriver.pipeInput(purchaseOrderLinesConsumerRecordFactory.create(bytePurchaseOrderKey, bytePurchaseOrderValue));

        // Read the output
        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                PURCHASE_ORDERS_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is an output
        Assert.assertNotNull(output);

        // Validate the output
        GenericRecord poResult = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
        Assert.assertNotNull(poResult);

        Assert.assertNotNull(poResult.get("uuid"));
        Assert.assertEquals("103-1970-01-01", poResult.get("aggregationKey"));
        Assert.assertEquals("103", poResult.get("country"));
        Assert.assertEquals(104L, poResult.get("date"));
        Assert.assertEquals(11990f, (float) poResult.get("totalAmount"), 0.001);
        Assert.assertEquals(110, (int) poResult.get("totalQuantity"));

        List<GenericRecord> poResultLines = (List<GenericRecord>) poResult.get("lines");
        Assert.assertEquals(1, poResultLines.size());

        GenericRecord poLine = poResultLines.get(0);
        Assert.assertNotNull(poLine.get("uuid"));
        Assert.assertEquals("102", poLine.get("aggregationKey"));
        Assert.assertEquals("105", poLine.get("productUuid"));
        Assert.assertEquals(109f, poLine.get("price"));
        Assert.assertEquals(110, poLine.get("quantity"));
    }

    /**
     * Unit test with three purchase order line converting in one purchase order with three lines
     */
    @Test
    public void testGenerateOnePurchaseOrderFromMultiplePurchaseOrderLines() {

        // Create a purchase order line #1
        PurchaseOrderLine purchaseOrderLine = PurchaseOrderLine
                .newBuilder()
                .setUuid("uuid1")
                .setAggregationKey("a-key")
                .setCountry("ES")
                .setDate(1546300800000L)
                .setProductUuid("prod1")
                .setProductName("prod1")
                .setProductType("prod1")
                .setProductBarCode("prod1")
                .setProductPrice(100f)
                .setQuantity(5)
                .build();
        String purchaseOrderLineUuid = purchaseOrderLine.getUuid();

        // Send the purchase order #1 to the topic
        byte[] bytePurchaseOrderKey = keyAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, purchaseOrderLineUuid);
        byte[] bytePurchaseOrderValue = valueAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, purchaseOrderLine);
        testDriver.pipeInput(purchaseOrderLinesConsumerRecordFactory.create(bytePurchaseOrderKey, bytePurchaseOrderValue));

        // Create a purchase order line #2
        purchaseOrderLine = PurchaseOrderLine
                .newBuilder(purchaseOrderLine)
                .setUuid("uuid2")
                .setProductUuid("prod2")
                .setProductName("prod2")
                .setProductType("prod2")
                .setProductBarCode("prod2")
                .setProductPrice(150f)
                .setQuantity(3)
                .build();
        purchaseOrderLineUuid = purchaseOrderLine.getUuid();

        // Send the purchase order #2 to the topic
        bytePurchaseOrderKey = keyAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, purchaseOrderLineUuid);
        bytePurchaseOrderValue = valueAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, purchaseOrderLine);
        testDriver.pipeInput(purchaseOrderLinesConsumerRecordFactory.create(bytePurchaseOrderKey, bytePurchaseOrderValue));

        // Create a purchase order line #3
        purchaseOrderLine = PurchaseOrderLine
                .newBuilder(purchaseOrderLine)
                .setUuid("uuid3")
                .setProductUuid("prod3")
                .setProductName("prod3")
                .setProductType("prod3")
                .setProductBarCode("prod3")
                .setProductPrice(50f)
                .setQuantity(2)
                .build();
        purchaseOrderLineUuid = purchaseOrderLine.getUuid();

        // Send the purchase order #3 to the topic
        bytePurchaseOrderKey = keyAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, purchaseOrderLineUuid);
        bytePurchaseOrderValue = valueAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, purchaseOrderLine);
        testDriver.pipeInput(purchaseOrderLinesConsumerRecordFactory.create(bytePurchaseOrderKey, bytePurchaseOrderValue));

        // Read the last message of the output
        ProducerRecord<byte[], byte[]> output = null;
        ProducerRecord<byte[], byte[]> newOutput = testDriver.readOutput(
                PURCHASE_ORDERS_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        while (null != newOutput) {
            output = newOutput;
            System.out.println((GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value()));
            newOutput = testDriver.readOutput(
                    PURCHASE_ORDERS_OUTPUT_TOPIC,
                    new ByteArrayDeserializer(),
                    new ByteArrayDeserializer()
            );
        }

        // Ensure there is an output
        Assert.assertNotNull(output);

        // Validate the output
        GenericRecord poResult = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
        Assert.assertNotNull(poResult);

        Assert.assertNotNull(poResult.get("uuid"));
        Assert.assertEquals("ES-2019-01-01", poResult.get("aggregationKey"));
        Assert.assertEquals("ES", poResult.get("country"));
        Assert.assertEquals(1546300800000L, poResult.get("date"));
        Assert.assertEquals(1050f, (float) poResult.get("totalAmount"), 0.001);
        Assert.assertEquals(10, (int) poResult.get("totalQuantity"));

        List<GenericRecord> poResultLines = (List<GenericRecord>) poResult.get("lines");
        Assert.assertEquals(3, poResultLines.size());
    }

    /**
     * Unit test last order line with the same product replaces old values
     */
    @Test
    public void testOrderLinesWithSameProductReplacesOldOrderLines() {

        // Create a purchase order line #1
        PurchaseOrderLine purchaseOrderLine = PurchaseOrderLine
                .newBuilder()
                .setUuid("uuid1")
                .setAggregationKey("a-key")
                .setCountry("ES")
                .setDate(1546300800000L)
                .setProductUuid("prod1")
                .setProductName("prod1")
                .setProductType("prod1")
                .setProductBarCode("prod1")
                .setProductPrice(100f)
                .setQuantity(5)
                .build();
        String purchaseOrderLineUuid = purchaseOrderLine.getUuid();

        // Send the purchase order #1 to the topic
        byte[] bytePurchaseOrderKey = keyAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, purchaseOrderLineUuid);
        byte[] bytePurchaseOrderValue = valueAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, purchaseOrderLine);
        testDriver.pipeInput(purchaseOrderLinesConsumerRecordFactory.create(bytePurchaseOrderKey, bytePurchaseOrderValue));

        // Create a purchase order line #2
        purchaseOrderLine = PurchaseOrderLine
                .newBuilder(purchaseOrderLine)
                .setQuantity(7)
                .build();
        purchaseOrderLineUuid = purchaseOrderLine.getUuid();

        // Send the purchase order #2 to the topic
        bytePurchaseOrderKey = keyAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, purchaseOrderLineUuid);
        bytePurchaseOrderValue = valueAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, purchaseOrderLine);
        testDriver.pipeInput(purchaseOrderLinesConsumerRecordFactory.create(bytePurchaseOrderKey, bytePurchaseOrderValue));

        // Create a purchase order line #3
        purchaseOrderLine = PurchaseOrderLine
                .newBuilder(purchaseOrderLine)
                .setQuantity(12)
                .build();
        purchaseOrderLineUuid = purchaseOrderLine.getUuid();

        // Send the purchase order #3 to the topic
        bytePurchaseOrderKey = keyAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, purchaseOrderLineUuid);
        bytePurchaseOrderValue = valueAvroSerializer.serialize(PURCHASE_ORDER_LINES_INPUT_TOPIC, purchaseOrderLine);
        testDriver.pipeInput(purchaseOrderLinesConsumerRecordFactory.create(bytePurchaseOrderKey, bytePurchaseOrderValue));

        // Read the last message of the output
        ProducerRecord<byte[], byte[]> output = null;
        ProducerRecord<byte[], byte[]> newOutput = testDriver.readOutput(
                PURCHASE_ORDERS_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        while (null != newOutput) {
            output = newOutput;
            System.out.println((GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value()));
            newOutput = testDriver.readOutput(
                    PURCHASE_ORDERS_OUTPUT_TOPIC,
                    new ByteArrayDeserializer(),
                    new ByteArrayDeserializer()
            );
        }

        // Ensure there is an output
        Assert.assertNotNull(output);

        // Validate the output
        GenericRecord poResult = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
        Assert.assertNotNull(poResult);

        Assert.assertNotNull(poResult.get("uuid"));
        Assert.assertEquals("ES-2019-01-01", poResult.get("aggregationKey"));
        Assert.assertEquals("ES", poResult.get("country"));
        Assert.assertEquals(1546300800000L, poResult.get("date"));
        Assert.assertEquals(1200f, (float) poResult.get("totalAmount"), 0.001);
        Assert.assertEquals(12, (int) poResult.get("totalQuantity"));

        List<GenericRecord> poResultLines = (List<GenericRecord>) poResult.get("lines");
        Assert.assertEquals(1, poResultLines.size());
    }
}
