package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.kafka.serde.GenericPrimitiveAvroSerde;
import com.example.kafka.streams.poc.schemas.order.CommercialOrder;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderLine;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderLineSplit;
import com.example.kafka.streams.poc.schemas.product.Product;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Kafka streams for splitting the commercial order lines
 */
@Component
public class CommercialOrderLineSplitterStream extends BaseStream {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommercialOrderLineSplitterStream.class);

    /** The name of the new products Kafka topic (input KTable) */
    private final String newProductsTopic;

    /** The name of the new commercial orders Kafka topic (input KStream) */
    private final String newCommercialOrdersTopic;

    /** The name of the split commercial order lines Kafka topic (output KStream) */
    private final String splitCommercialOrderLinesTopic;

    /** Serde for the string avro key */
    private final Serde<String> stringKeyAvroSerde;

    /** Serde for the product avro value */
    private final Serde<Product> productValueAvroSerde;

    /** Serde for the input commercial order avro value */
    private final Serde<CommercialOrder> commercialOrderValueAvroSerde;

    /** Serde for the output commercial order lines avro value */
    private final Serde<CommercialOrderLineSplit> commercialOrderLineValueAvroSerde;

    /**
     * Autowired constructor
     *
     * @param schemaRegistryUrl              the URL of the schema registry
     * @param newProductsTopic               the name of the new products Kafka topic (input KTable)
     * @param newCommercialOrdersTopic       the name of the new commercial orders Kafka topic (input KStream)
     * @param splitCommercialOrderLinesTopic the name of the split commercial order lines Kafka topic (output KStream)
     */
    @Autowired
    public CommercialOrderLineSplitterStream(
            @Value("${spring.kafka.schema-registry-url}") String schemaRegistryUrl,
            @Value("${spring.kafka.topics.products-new}") String newProductsTopic,
            @Value("${spring.kafka.topics.commercial-orders-new}") String newCommercialOrdersTopic,
            @Value("${spring.kafka.topics.commercial-order-lines-split}") String splitCommercialOrderLinesTopic
    ) {
        super(schemaRegistryUrl);

        this.newProductsTopic = newProductsTopic;
        this.newCommercialOrdersTopic = newCommercialOrdersTopic;
        this.splitCommercialOrderLinesTopic = splitCommercialOrderLinesTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>();
        this.productValueAvroSerde = new SpecificAvroSerde<>();
        this.commercialOrderValueAvroSerde = new SpecificAvroSerde<>();
        this.commercialOrderLineValueAvroSerde = new SpecificAvroSerde<>();

        configureSerdes();
    }

    /**
     * Test constructor
     *
     * @param schemaRegistryClient           the schema registry client (for testing)
     * @param schemaRegistryUrl              the URL of the schema registry
     * @param newProductsTopic               the name of the new products Kafka topic (input KTable)
     * @param newCommercialOrdersTopic       the name of the new commercial orders Kafka topic (input KStream)
     * @param splitCommercialOrderLinesTopic the name of the split commercial order lines Kafka topic (output KStream)
     */
    public CommercialOrderLineSplitterStream(
            SchemaRegistryClient schemaRegistryClient,
            String schemaRegistryUrl,
            String newProductsTopic,
            String newCommercialOrdersTopic,
            String splitCommercialOrderLinesTopic
    ) {
        super(schemaRegistryUrl);

        this.newProductsTopic = newProductsTopic;
        this.newCommercialOrdersTopic = newCommercialOrdersTopic;
        this.splitCommercialOrderLinesTopic = splitCommercialOrderLinesTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>(schemaRegistryClient);
        this.productValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);
        this.commercialOrderValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);
        this.commercialOrderLineValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);

        configureSerdes();
    }

    /**
     * Configures all the serdes for this Kafka Streams
     */
    private void configureSerdes() {
        this.stringKeyAvroSerde.configure(serdeConfig, true);
        this.productValueAvroSerde.configure(serdeConfig, false);
        this.commercialOrderValueAvroSerde.configure(serdeConfig, false);
        this.commercialOrderLineValueAvroSerde.configure(serdeConfig, false);
    }

    /**
     * Builds the topology of the Kafka Streams
     *
     * @param builder the streams builder
     * @return the builder configured with the topology
     */
    @Bean("commercialOrderLineSplitStreamTopology")
    public StreamsBuilder startProcessing(
            @Qualifier("commercialOrderLineSplitStreamBuilderFactoryBean") StreamsBuilder builder
    ) {
        GlobalKTable<String, Product> productsGlobalTable = builder.globalTable(
                newProductsTopic,
                Consumed.with(stringKeyAvroSerde, productValueAvroSerde)
        );

        KStream<String, CommercialOrder> commercialOrdersStream = builder.stream(
                newCommercialOrdersTopic,
                Consumed.with(stringKeyAvroSerde, commercialOrderValueAvroSerde)
        );

        KStream<String, CommercialOrderLineSplit> commercialOrderLinesStream = commercialOrdersStream
                .flatMap(
                        (String uuid, CommercialOrder commercialOrder) -> {
                            LOGGER.info(">>> Stream - Commercial order uuid={} - Splitting commercial order lines...", commercialOrder.getUuid());

                            List<KeyValue<String, CommercialOrderLineSplit>> result = new LinkedList<>();
                            for (CommercialOrderLine line : commercialOrder.getLines()) {
                                LOGGER.info(">>> Stream - Commercial order uuid={} - Splitting commercial order line uuid={}...", commercialOrder.getUuid(), line.getUuid());

                                CommercialOrderLineSplit commercialOrderLineSplit = CommercialOrderLineSplit
                                        .newBuilder()
                                        .setUuid(line.getUuid())
                                        .setCommercialOrderUuid(commercialOrder.getUuid())
                                        .setCommercialOrderDatetime(commercialOrder.getDatetime())
                                        .setShippingCountry(commercialOrder.getShippingAddress().getCountry())
                                        .setMemberUuid(commercialOrder.getMemberUuid())
                                        .setProductUuid(line.getProductUuid())
                                        .setProductName("")
                                        .setProductType("")
                                        .setProductBarCode("")
                                        .setProductPrice(0)
                                        .setOrderLinePrice(line.getPrice())
                                        .setQuantity(line.getQuantity())
                                        .build();

                                result.add(KeyValue.pair(uuid, commercialOrderLineSplit));
                            }
                            return result;
                        }
                );

        KStream<String, CommercialOrderLineSplit> joinedCommercialOrderLinesStream = commercialOrderLinesStream
                .join(
                        productsGlobalTable,
                        (String uuid, CommercialOrderLineSplit line) -> line.getProductUuid(),
                        (CommercialOrderLineSplit line, Product product) -> {
                            LOGGER.info(">>> Stream - Commercial order line uuid={} joined with product uuid={}.", line.getUuid(), line.getProductUuid());
                            return CommercialOrderLineSplit
                                    .newBuilder(line)
                                    .setProductName(product.getName())
                                    .setProductType(product.getType())
                                    .setProductBarCode(product.getBarCode())
                                    .setProductPrice(product.getPrice())
                                    .build();
                        }
                );

        joinedCommercialOrderLinesStream.to(
                splitCommercialOrderLinesTopic,
                Produced.with(stringKeyAvroSerde, commercialOrderLineValueAvroSerde)
        );

        return builder;
    }
}
