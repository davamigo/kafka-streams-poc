package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.schemas.order.CommercialOrder;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderAddress;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderLine;
import com.example.kafka.streams.poc.schemas.product.Product;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class CommercialOrderLineSplitterStreamTest extends StreamTestBase {

    /** Consumer recor factories to send input data for testing */
    private ConsumerRecordFactory<byte[], byte[]> commercialOrderConsumerRecordFactory = null;
    private ConsumerRecordFactory<byte[], byte[]> productConsumerRecordFactory = null;

    /** Constants */
    private final String PRODUCTS_TOPIC = "t.products";
    private final String COMMERCIAL_ORDERS_INPUT_TOPIC = "t.commercial-orders-input";
    private final String COMMERCIAL_ORDERS_LINES_OUTPUT_TOPIC = "t.commercial-order-lines-output";

    /**
     * Sets up the environment before testing the Kafka streams process
     */
    @Before
    public void setUp() {

        // Create the schema registry mock to avoid using the real schema registry
        final SchemaRegistryClient schemaRegistryClient = new MockSchemaRegistryClient();

        // Create the topology builder to get the topology (what we are testing)
        final CommercialOrderLineSplitterStream streamTopologyBuilder = new CommercialOrderLineSplitterStream(
                schemaRegistryClient,
                DUMMY_SCHEMA_REGISTRY_URL,
                PRODUCTS_TOPIC,
                COMMERCIAL_ORDERS_INPUT_TOPIC,
                COMMERCIAL_ORDERS_LINES_OUTPUT_TOPIC
        );

        final StreamsBuilder streamsBuilder = streamTopologyBuilder.startProcessing(new StreamsBuilder());
        final Topology topology = streamsBuilder.build();

        // Parent Setup to create the TopologyTestDriver
        parentSetUp(schemaRegistryClient, topology);

        // Create the comercial order consumer record factory to send records to the Kafka stream consumer
        commercialOrderConsumerRecordFactory = new ConsumerRecordFactory<>(
                COMMERCIAL_ORDERS_INPUT_TOPIC,
                new ByteArraySerializer(),
                new ByteArraySerializer()
        );

        // Create the product consumer record factory to send records to the Kafka stream consumer
        productConsumerRecordFactory = new ConsumerRecordFactory<>(
                PRODUCTS_TOPIC,
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
     * Unit test to ensure a commercial order lines matches with a product when both exist in the input topics
     */
    @Test
    public void testValidOutputWhenProductMatched() {

        // Create a product
        Product productValue = new Product("aaa", "bbb", "ccc", "ddd", 100f);
        String productKey = productValue.getUuid();

        // Send the product to the topic
        byte[] byteProductKey = keyAvroSerializer.serialize(PRODUCTS_TOPIC, productKey);
        byte[] byteProductValue = valueAvroSerializer.serialize(PRODUCTS_TOPIC, productValue);
        testDriver.pipeInput(productConsumerRecordFactory.create(byteProductKey, byteProductValue));

        // Create a commercial order
        CommercialOrder commercialOrderValue = CommercialOrder
                .newBuilder()
                .setUuid("101")
                .setDatetime(102L)
                .setMemberUuid("103")
                .setShippingAddress(CommercialOrderAddress
                        .newBuilder()
                        .setCountry("111")
                        .setCity("112")
                        .setZipCode("113")
                        .build())
                .setLines(Collections.singletonList(CommercialOrderLine
                        .newBuilder()
                        .setUuid("131")
                        .setCommercialOrderUuid("101")
                        .setProductUuid("aaa")
                        .setPrice(133f)
                        .setQuantity(134)
                        .build()))
                .build();
        String commercialOrderKey = commercialOrderValue.getUuid();

        // Send the commercial order to the topic
        byte[] byteCommercialOrderKey = keyAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderKey);
        byte[] byteCommercialOrderValue = valueAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderValue);
        testDriver.pipeInput(commercialOrderConsumerRecordFactory.create(byteCommercialOrderKey, byteCommercialOrderValue));

        // Read the output
        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                COMMERCIAL_ORDERS_LINES_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is output
        Assert.assertNotNull(output);

        // Validate the output
        GenericRecord result = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
        Assert.assertNotNull(result);

        Assert.assertEquals("131", result.get("uuid"));
        Assert.assertEquals("101", result.get("commercialOrderUuid"));
        Assert.assertEquals(102L, result.get("commercialOrderDatetime"));
        Assert.assertEquals("111", result.get("shippingCountry"));
        Assert.assertEquals("103", result.get("memberUuid"));
        Assert.assertEquals("aaa", result.get("productUuid"));
        Assert.assertEquals("bbb", result.get("productName"));
        Assert.assertEquals("ccc", result.get("productType"));
        Assert.assertEquals("ddd", result.get("productBarCode"));
        Assert.assertEquals(100f, (float) result.get("productPrice"), 0.001);
        Assert.assertEquals(133f, (float) result.get("orderLinePrice"), 0.001);
        Assert.assertEquals(134, result.get("quantity"));
    }

    /**
     * Unit test to ensure no commercial order lines generated when no products in the topic
     */
    @Test
    public void testNoOutputWhenNoProducts() {

        // Create a commercial order
        CommercialOrder commercialOrderValue = CommercialOrder
                .newBuilder()
                .setUuid("201")
                .setDatetime(202L)
                .setMemberUuid("203")
                .setShippingAddress(CommercialOrderAddress
                        .newBuilder()
                        .setCountry("211")
                        .setCity("212")
                        .setZipCode("213")
                        .build())
                .setLines(Collections.singletonList(CommercialOrderLine
                        .newBuilder()
                        .setUuid("231")
                        .setCommercialOrderUuid("201")
                        .setProductUuid("232")
                        .setPrice(233f)
                        .setQuantity(234)
                        .build()))
                .build();
        String commercialOrderKey = commercialOrderValue.getUuid();

        // Send the commercial order to the topic
        byte[] byteCommercialOrderKey = keyAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderKey);
        byte[] byteCommercialOrderValue = valueAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderValue);
        testDriver.pipeInput(commercialOrderConsumerRecordFactory.create(byteCommercialOrderKey, byteCommercialOrderValue));

        // Read the output
        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                COMMERCIAL_ORDERS_LINES_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is no output
        Assert.assertNull(output);
    }

    /**
     * Unit test to ensure a commercial order lines matches with a product when both exist in the input topics
     */
    @Test
    public void testNoOutputWhenProductNotMatched() {

        // Create a product
        Product productValue = new Product("aaa", "bbb", "ccc", "ddd", 100f);
        String productKey = productValue.getUuid();

        // Send the product to the topic
        byte[] byteProductKey = keyAvroSerializer.serialize(PRODUCTS_TOPIC, productKey);
        byte[] byteProductValue = valueAvroSerializer.serialize(PRODUCTS_TOPIC, productValue);
        testDriver.pipeInput(productConsumerRecordFactory.create(byteProductKey, byteProductValue));

        // Create a commercial order
        CommercialOrder commercialOrderValue = CommercialOrder
                .newBuilder()
                .setUuid("301")
                .setDatetime(302L)
                .setMemberUuid("303")
                .setShippingAddress(CommercialOrderAddress
                        .newBuilder()
                        .setCountry("311")
                        .setCity("312")
                        .setZipCode("313")
                        .build())
                .setLines(Collections.singletonList(CommercialOrderLine
                        .newBuilder()
                        .setUuid("331")
                        .setCommercialOrderUuid("301")
                        .setProductUuid("__other__")
                        .setPrice(323f)
                        .setQuantity(324)
                        .build()))
                .build();
        String commercialOrderKey = commercialOrderValue.getUuid();

        // Send the commercial order to the topic
        byte[] byteCommercialOrderKey = keyAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderKey);
        byte[] byteCommercialOrderValue = valueAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderValue);
        testDriver.pipeInput(commercialOrderConsumerRecordFactory.create(byteCommercialOrderKey, byteCommercialOrderValue));

        // Read the output
        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                COMMERCIAL_ORDERS_LINES_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is no output
        Assert.assertNull(output);
    }

    /**
     * Unit test validate one commercial order with more than one order line generate multiple outputs
     */
    @Test
    public void testSingleOrderWithManyLinesGenerateMultipleOutputs() {

        // Create a product
        Product productValue = new Product("aaa", "bbb", "ccc", "ddd", 100f);
        String productKey = productValue.getUuid();

        // Send the product to the topic
        byte[] byteProductKey = keyAvroSerializer.serialize(PRODUCTS_TOPIC, productKey);
        byte[] byteProductValue = valueAvroSerializer.serialize(PRODUCTS_TOPIC, productValue);
        testDriver.pipeInput(productConsumerRecordFactory.create(byteProductKey, byteProductValue));

        // Create a commercial order
        CommercialOrder commercialOrderValue = CommercialOrder
                .newBuilder()
                .setUuid("401")
                .setDatetime(402L)
                .setMemberUuid("403")
                .setShippingAddress(CommercialOrderAddress
                        .newBuilder()
                        .setCountry("411")
                        .setCity("412")
                        .setZipCode("413")
                        .build())
                .setLines(Arrays.asList(
                        CommercialOrderLine
                                .newBuilder()
                                .setUuid("431")
                                .setCommercialOrderUuid("401")
                                .setProductUuid("aaa")
                                .setPrice(433f)
                                .setQuantity(434)
                                .build(),
                        CommercialOrderLine
                                .newBuilder()
                                .setUuid("441")
                                .setCommercialOrderUuid("401")
                                .setProductUuid("aaa")
                                .setPrice(443f)
                                .setQuantity(444)
                                .build(),
                        CommercialOrderLine
                                .newBuilder()
                                .setUuid("451")
                                .setCommercialOrderUuid("401")
                                .setProductUuid("aaa")
                                .setPrice(453f)
                                .setQuantity(454)
                                .build()))
                .build();
        String commercialOrderKey = commercialOrderValue.getUuid();

        // Send the commercial order to the topic
        byte[] byteCommercialOrderKey = keyAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderKey);
        byte[] byteCommercialOrderValue = valueAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderValue);
        testDriver.pipeInput(commercialOrderConsumerRecordFactory.create(byteCommercialOrderKey, byteCommercialOrderValue));

        // Read the output
        List<String> ids = new ArrayList<>();


        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                COMMERCIAL_ORDERS_LINES_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        while (null != output) {
            GenericRecord result = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
            Assert.assertNotNull(result);
            ids.add((String) result.get("uuid"));
            output = testDriver.readOutput(
                    COMMERCIAL_ORDERS_LINES_OUTPUT_TOPIC,
                    new ByteArrayDeserializer(),
                    new ByteArrayDeserializer()
            );
        }

        // Ensure there is output
        Assert.assertEquals(3, ids.size());
        Assert.assertTrue(ids.contains("431"));
        Assert.assertTrue(ids.contains("441"));
        Assert.assertTrue(ids.contains("451"));
    }
}
