package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
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
public class ProductLegacyIdFeederStreamTest extends StreamTestBase {

    /** Consumer record factories to send input data for testing */
    private ConsumerRecordFactory<byte[], byte[]> recoveredWarehouseOrderLinesConsumerRecordFactory = null;

    /** Constants */
    private final String WAREHOUSE_ORDER_LINES_RECOVERED_INPUT_TOPIC = "t.warehouse-order-lines-recovered-input";
    private final String PRODUCT_LEGACY_IDS_OUTPUT_TOPIC = "t.product-legacy-ids-input";

    /**
     * Sets up the environment before testing the Kafka streams process
     */
    @Before
    public void setUp() {

        // Create the schema registry mock to avoid using the real schema registry
        final SchemaRegistryClient schemaRegistryClient = new MockSchemaRegistryClient();

        // Create the topology builder to get the topology (what we are testing)
        final ProductLegacyIdFeederStream streamTopologyBuilder = new ProductLegacyIdFeederStream(
                schemaRegistryClient,
                DUMMY_SCHEMA_REGISTRY_URL,
                WAREHOUSE_ORDER_LINES_RECOVERED_INPUT_TOPIC,
                PRODUCT_LEGACY_IDS_OUTPUT_TOPIC
        );

        final StreamsBuilder streamsBuilder = streamTopologyBuilder.startProcessing(new StreamsBuilder());
        final Topology topology = streamsBuilder.build();

        // Parent Setup to create the TopologyTestDriver
        parentSetUp(schemaRegistryClient, topology);

        // Create the warehouse order line consumer record factory to send records to the Kafka stream consumer
        recoveredWarehouseOrderLinesConsumerRecordFactory = new ConsumerRecordFactory<>(
                WAREHOUSE_ORDER_LINES_RECOVERED_INPUT_TOPIC,
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
     * Unit test to check merge works when only recovered warehouse order lines
     */
    @Test
    public void testMergeWorksWithRecovered() {

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

        // Send the input commercial order line to recovered topic
        byte[] inputKeyEncoded = keyAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_RECOVERED_INPUT_TOPIC, inputKey);
        byte[] inputValueEncoded = valueAvroSerializer.serialize(WAREHOUSE_ORDER_LINES_RECOVERED_INPUT_TOPIC, inputValue);
        ConsumerRecord<byte[], byte[]> input = recoveredWarehouseOrderLinesConsumerRecordFactory.create(inputKeyEncoded, inputValueEncoded);
        testDriver.pipeInput(input);

        // Read the output topic
        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                PRODUCT_LEGACY_IDS_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is an recovered output
        Assert.assertNotNull(output);

        // Validate the output key and value
        String outputKey = (String) keyAvroDeserializer.deserialize(output.topic(), output.key());
        Integer outputValue = (Integer) valueAvroDeserializer.deserialize(output.topic(), output.value());

        Assert.assertNotNull(outputKey);
        Assert.assertNotNull(outputValue);

        Assert.assertEquals("104", outputKey);
        Assert.assertEquals(105, (int) outputValue);
    }
}
