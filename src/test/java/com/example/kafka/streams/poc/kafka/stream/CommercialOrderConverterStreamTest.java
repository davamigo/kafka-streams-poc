package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.schemas.member.Member;
import com.example.kafka.streams.poc.schemas.order.CommercialOrder;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderAddress;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderLine;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;

@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class CommercialOrderConverterStreamTest {

    /** Topology test driver - tool to test the topology of a Kafka stream */
    private TopologyTestDriver testDriver = null;

    /** Consumer recor factories to send input data for testing */
    private ConsumerRecordFactory<byte[], byte[]> commercialOrderConsumerRecordFactory = null;
    private ConsumerRecordFactory<byte[], byte[]> memberConsumerRecordFactory = null;

    /** Avro serializers/deserializers */
    private KafkaAvroSerializer keyAvroSerializer;
    private KafkaAvroSerializer valueAvroSerializer;
    private KafkaAvroDeserializer valueAvroDeserializer;

    /** Constants */
    private final String DUMMY_SCHEMA_REGISTRY = "dummy:1234";
    private final String DUMMY_SCHEMA_REGISTRY_URL = "http://" + DUMMY_SCHEMA_REGISTRY;
    private final String MEMBERS_TOPIC = "t.members";
    private final String COMMERCIAL_ORDERS_INPUT_TOPIC = "t.commercial-orders-input";
    private final String COMMERCIAL_ORDERS_OUTPUT_TOPIC = "t.commercial-orders-output";

    /**
     * Sets up the environment before testing the Kafka streams process
     */
    @Before
    public void setUp() {

        // Create the schema registry mock to avoid using the real schema registry
        final SchemaRegistryClient schemaRegistryClient = new MockSchemaRegistryClient();

        // Create the topology builder to get the topology (what we are testing)
        final CommercialOrderConverterStream streamTopologyBuilder = new CommercialOrderConverterStream(
                schemaRegistryClient,
                DUMMY_SCHEMA_REGISTRY_URL,
                MEMBERS_TOPIC,
                COMMERCIAL_ORDERS_INPUT_TOPIC,
                COMMERCIAL_ORDERS_OUTPUT_TOPIC
        );

        final StreamsBuilder streamsBuilder = streamTopologyBuilder.startProcessing(new StreamsBuilder());
        final Topology topology = streamsBuilder.build();

        // Configure the properties of the Kafka Streams process
        final Properties config = new Properties();
        config.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, DUMMY_SCHEMA_REGISTRY);
        config.setProperty(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
        config.setProperty(StreamsConfig.NUM_STREAM_THREADS_CONFIG, "1");
        config.setProperty(StreamsConfig.REPLICATION_FACTOR_CONFIG, "1");
        config.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, GenericAvroSerde.class.getName());
        config.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class.getName());
        config.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "commercialOrderConverterStream");
        config.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.setProperty(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, DUMMY_SCHEMA_REGISTRY_URL);

        // Create the test driver
        testDriver = new TopologyTestDriver(topology, config);

        // Create the comercial order consumer record factory to send records to the Kafka stream consumer
        commercialOrderConsumerRecordFactory = new ConsumerRecordFactory<>(
                COMMERCIAL_ORDERS_INPUT_TOPIC,
                new ByteArraySerializer(),
                new ByteArraySerializer()
        );

        // Create the member consumer record factory to send records to the Kafka stream consumer
        memberConsumerRecordFactory = new ConsumerRecordFactory<>(
                MEMBERS_TOPIC,
                new ByteArraySerializer(),
                new ByteArraySerializer()
        );

        // Create de avro serializers/deserializers
        final Map<String, String> serializerConfig = Collections
                .singletonMap(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, DUMMY_SCHEMA_REGISTRY_URL);

        keyAvroSerializer = new KafkaAvroSerializer(schemaRegistryClient);
        keyAvroSerializer.configure(serializerConfig, true);

        valueAvroSerializer = new KafkaAvroSerializer(schemaRegistryClient);
        valueAvroSerializer.configure(serializerConfig, false);

        valueAvroDeserializer = new KafkaAvroDeserializer(schemaRegistryClient);
        valueAvroDeserializer.configure(serializerConfig, false);
    }

    /**
     * Clears the environment after testing the Kafka streams process
     */
    @After
    public void tearDown() {
        if (null != testDriver) {
            testDriver.close();
        }
    }

    /**
     * Unit test to ensure a commercial order matches with a member when both exist in the input topics
     */
    @Test
    public void testValidOutputWhenMemberMatched() {

        // Create a member
        Member memberValue = createTestMember(1);
        String memberKey = memberValue.getUuid();

        // Send the member to the topic
        byte[] byteMemberKey = keyAvroSerializer.serialize(MEMBERS_TOPIC, memberKey);
        byte[] byteMemberValue = valueAvroSerializer.serialize(MEMBERS_TOPIC, memberValue);
        testDriver.pipeInput(memberConsumerRecordFactory.create(byteMemberKey, byteMemberValue));

        // Create a commercial order
        CommercialOrder commercialOrderValue = createTestcommercialOrder(1);
        String commercialOrderKey = commercialOrderValue.getUuid();

        // Send the commercial order to the topic
        byte[] byteCommercialOrderKey = keyAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderKey);
        byte[] byteCommercialOrderValue = valueAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderValue);
        testDriver.pipeInput(commercialOrderConsumerRecordFactory.create(byteCommercialOrderKey, byteCommercialOrderValue));

        // Read the output
        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                COMMERCIAL_ORDERS_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is output
        Assert.assertNotNull(output);

        // Validate the output
        GenericRecord result = (GenericRecord) valueAvroDeserializer.deserialize(output.topic(), output.value());
        Assert.assertNotNull(result);
        Assert.assertEquals("101", result.get("uuid"));
        Assert.assertEquals(102L, result.get("datetime"));
        Assert.assertEquals("103", result.get("memberUuid"));
        Assert.assertEquals("131", result.get("memberFirstName"));
        Assert.assertEquals("132", result.get("memberLastName"));
        Assert.assertEquals("111", result.get("shippingCountry"));
        Assert.assertEquals("112", result.get("shippingCity"));
        Assert.assertEquals("113", result.get("shippingZipCode"));
        Assert.assertEquals(104, result.get("totalQuantity"));
        Assert.assertEquals((float)105 * 104, (float) result.get("totalAmount"), 0.001);
    }

    /**
     * Unit test to ensure a commercial order doesn't match when the member is different
     */
    @Test
    public void testNoOutputWhenMemberIsDifferent() {

        // Create a member
        Member memberValue = createTestMember(2);
        String memberKey = memberValue.getUuid();

        // Send the member to the topic
        byte[] byteMemberKey = keyAvroSerializer.serialize(MEMBERS_TOPIC, memberKey);
        byte[] byteMemberValue = valueAvroSerializer.serialize(MEMBERS_TOPIC, memberValue);
        testDriver.pipeInput(memberConsumerRecordFactory.create(byteMemberKey, byteMemberValue));

        // Create a commercial order
        CommercialOrder commercialOrderValue = createTestcommercialOrder(1);
        String commercialOrderKey = commercialOrderValue.getUuid();

        // Send the commercial order to the topic
        byte[] byteCommercialOrderKey = keyAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderKey);
        byte[] byteCommercialOrderValue = valueAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderValue);
        testDriver.pipeInput(commercialOrderConsumerRecordFactory.create(byteCommercialOrderKey, byteCommercialOrderValue));

        // Read the output record
        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                COMMERCIAL_ORDERS_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure no output
        Assert.assertNull(output);
    }

    /**
     * Unit test to ensure a commercial order doesn't match when the member topic is empty
     */
    @Test
    public void testNoOutputWhenNoMembers() {

        // Create a commercial order
        CommercialOrder commercialOrderValue = createTestcommercialOrder(3);
        String commercialOrderKey = commercialOrderValue.getUuid();

        // Send the commercial order to the topic
        byte[] byteCommercialOrderKey = keyAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderKey);
        byte[] byteCommercialOrderValue = valueAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderValue);
        testDriver.pipeInput(commercialOrderConsumerRecordFactory.create(byteCommercialOrderKey, byteCommercialOrderValue));

        // Read the output record
        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                COMMERCIAL_ORDERS_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure no output
        Assert.assertNull(output);
    }

    /**
     * Unit test to ensure a commercial order doesn't match when the member comes after the order
     */
    @Test
    public void testNoOutputWhenMemberComesAfterTheOrder() {

        // Create a commercial order
        CommercialOrder commercialOrderValue = createTestcommercialOrder(4);
        String commercialOrderKey = commercialOrderValue.getUuid();

        // Send the commercial order to the topic
        byte[] byteCommercialOrderKey = keyAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderKey);
        byte[] byteCommercialOrderValue = valueAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderValue);
        testDriver.pipeInput(commercialOrderConsumerRecordFactory.create(byteCommercialOrderKey, byteCommercialOrderValue));

        // Create a member
        Member memberValue = createTestMember(4);
        String memberKey = memberValue.getUuid();

        // Send the member to the topic
        byte[] byteMemberKey = keyAvroSerializer.serialize(MEMBERS_TOPIC, memberKey);
        byte[] byteMemberValue = valueAvroSerializer.serialize(MEMBERS_TOPIC, memberValue);
        testDriver.pipeInput(memberConsumerRecordFactory.create(byteMemberKey, byteMemberValue));

        // Read the output record
        ProducerRecord<byte[], byte[]> output = testDriver.readOutput(
                COMMERCIAL_ORDERS_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure no output
        Assert.assertNull(output);
    }

    /**
     * Unit test to ensure a commercial order can match with a single member
     */
    @Test
    public void testTwoCommercialOrdersMatchTheSameMember() {

        // Create a member
        Member memberValue = createTestMember(5);
        String memberKey = memberValue.getUuid();

        // Send the member to the topic
        byte[] byteMemberKey = keyAvroSerializer.serialize(MEMBERS_TOPIC, memberKey);
        byte[] byteMemberValue = valueAvroSerializer.serialize(MEMBERS_TOPIC, memberValue);
        testDriver.pipeInput(memberConsumerRecordFactory.create(byteMemberKey, byteMemberValue));

        // Create a commercial order #1
        CommercialOrder commercialOrderValue1 = createTestcommercialOrder(5);
        String commercialOrderKey1 = commercialOrderValue1.getUuid();

        // Send the commercial order to the topic #1
        byte[] byteCommercialOrderKey1 = keyAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderKey1);
        byte[] byteCommercialOrderValue1 = valueAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderValue1);
        testDriver.pipeInput(commercialOrderConsumerRecordFactory.create(byteCommercialOrderKey1, byteCommercialOrderValue1));

        // Create a commercial order #2
        CommercialOrder commercialOrderValue2 = createTestcommercialOrder(6);
        commercialOrderValue2.setMemberUuid("503");
        String commercialOrderKey2 = commercialOrderValue2.getUuid();

        // Send the commercial order to the topic #2
        byte[] byteCommercialOrderKey2 = keyAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderKey2);
        byte[] byteCommercialOrderValue2 = valueAvroSerializer.serialize(COMMERCIAL_ORDERS_INPUT_TOPIC, commercialOrderValue2);
        testDriver.pipeInput(commercialOrderConsumerRecordFactory.create(byteCommercialOrderKey2, byteCommercialOrderValue2));

        // Read the output #1
        ProducerRecord<byte[], byte[]> output1 = testDriver.readOutput(
                COMMERCIAL_ORDERS_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is output #1
        Assert.assertNotNull(output1);

        // Read the output #2
        ProducerRecord<byte[], byte[]> output2 = testDriver.readOutput(
                COMMERCIAL_ORDERS_OUTPUT_TOPIC,
                new ByteArrayDeserializer(),
                new ByteArrayDeserializer()
        );

        // Ensure there is output #2
        Assert.assertNotNull(output2);
    }

    /**
     * Creates a commercial order for testing purposes
     *
     * @param prefix the prefix of the fields of the commercial order
     * @return the commercial order created
     */
    private CommercialOrder createTestcommercialOrder(int prefix) {

        Assert.assertTrue(prefix > 0);
        Assert.assertTrue(prefix < 10);

        CommercialOrder.Builder builder = CommercialOrder
                .newBuilder()
                .setUuid(prefix + "01")
                .setDatetime((new Date(100 * prefix + 2)).getTime())
                .setMemberUuid(prefix + "03")
                .setShippingAddress(
                        CommercialOrderAddress
                                .newBuilder()
                                .setCountry(prefix + "11")
                                .setCity(prefix + "12")
                                .setZipCode(prefix + "13")
                                .build()
                );

        List<CommercialOrderLine> lines = new ArrayList<>();
        lines.add(CommercialOrderLine
                .newBuilder()
                .setUuid(prefix + "21")
                .setCommercialOrderUuid(prefix + "22")
                .setProductUuid(prefix + "23")
                .setQuantity(100 * prefix + 4)
                .setPrice((float)100 * prefix + 5)
                .build());
        builder.setLines(lines);

        return builder.build();
    }

    /**
     * Creates a member for testing purposes
     *
     * @param prefix the prefix of the fields of the member
     * @return the member created
     */
    private Member createTestMember(int prefix) {

        Assert.assertTrue(prefix > 0);
        Assert.assertTrue(prefix < 10);

        Member.Builder builder = Member
                .newBuilder()
                .setUuid(prefix + "03")
                .setFirstName(prefix + "31")
                .setLastName(prefix + "32")
                .setAddresses(new ArrayList<>());

        return builder.build();
    }
}
