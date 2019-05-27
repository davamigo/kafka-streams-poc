package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.kafka.serde.GenericPrimitiveAvroSerde;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderLineSplit;
import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLine;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Kafka streams for generating the purchase order lines
 */
@Component
public class PurchaseOrderLineAggregatorStream extends BaseStream {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderLineAggregatorStream.class);

    /** The name of the split commercial order lines Kafka topic (input KStream) */
    private final String splitCommercialOrderLinesTopic;

    /** The name of the aggregated purchase order lines Kafka topic (output KStream) */
    private final String aggregatedPurchaseOrderLinesTopic;

    /** Serde for the string avro key */
    private final Serde<String> stringKeyAvroSerde;

    /** Serde for the input commercial order line avro value */
    private final Serde<CommercialOrderLineSplit> commercialOrderLineSplitValueAvroSerde;

    /** Serde for the output purchase order line avro value */
    private final Serde<PurchaseOrderLine> purchaseOrderLineValueAvroSerde;

    /**
     * Autowired constructor
     *
     * @param schemaRegistryUrl                 the URL of the schema registry
     * @param splitCommercialOrderLinesTopic    the name of the split commercial order lines Kafka topic (input KStream)
     * @param aggregatedPurchaseOrderLinesTopic the name of the aggregated purchase order lines Kafka topic (output KStream)
     */
    @Autowired
    public PurchaseOrderLineAggregatorStream(
            @Value("${spring.kafka.schema-registry-url}") String schemaRegistryUrl,
            @Value("${spring.kafka.topics.commercial-order-lines-split}") String splitCommercialOrderLinesTopic,
            @Value("${spring.kafka.topics.purchase-order-lines-aggregated}") String aggregatedPurchaseOrderLinesTopic
    ) {
        super(schemaRegistryUrl);

        this.splitCommercialOrderLinesTopic = splitCommercialOrderLinesTopic;
        this.aggregatedPurchaseOrderLinesTopic = aggregatedPurchaseOrderLinesTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>();
        this.commercialOrderLineSplitValueAvroSerde = new SpecificAvroSerde<>();
        this.purchaseOrderLineValueAvroSerde = new SpecificAvroSerde<>();

        configureSerdes();
    }

    /**
     * Test constructor
     *
     * @param schemaRegistryClient              the schema registry client (for testing)
     * @param schemaRegistryUrl                 the URL of the schema registry
     * @param splitCommercialOrderLinesTopic    the name of the split commercial order lines Kafka topic (input KStream)
     * @param aggregatedPurchaseOrderLinesTopic the name of the aggregated purchase order lines Kafka topic (output KStream)
     */
    public PurchaseOrderLineAggregatorStream(
            SchemaRegistryClient schemaRegistryClient,
            String schemaRegistryUrl,
            String splitCommercialOrderLinesTopic,
            String aggregatedPurchaseOrderLinesTopic
    ) {
        super(schemaRegistryUrl);

        this.splitCommercialOrderLinesTopic = splitCommercialOrderLinesTopic;
        this.aggregatedPurchaseOrderLinesTopic = aggregatedPurchaseOrderLinesTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>(schemaRegistryClient);
        this.commercialOrderLineSplitValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);
        this.purchaseOrderLineValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);

        configureSerdes();
    }

    /**
     * Configures all the serdes for this Kafka Streams
     */
    private void configureSerdes() {
        this.stringKeyAvroSerde.configure(serdeConfig, true);
        commercialOrderLineSplitValueAvroSerde.configure(serdeConfig, false);
        this.purchaseOrderLineValueAvroSerde.configure(serdeConfig, false);
    }

    /**
     * Builds the topology of the Kafka Streams.
     *
     * - The map operation will convert the commercial order line to a purchase order line, with a new key.
     *
     *   The new key will be used for aggregate the order purchase order lines and it will be composed by the
     *   country code, the date (one per day) and the product uuid.
     *
     *   Key format: CC-YYYY-MM-DD-PPPPPP, where:
     *   - CC - The country code.
     *   - YYYY-MM-DD - The date.
     *   - PPPPPP - The product Uuid.
     *
     * - The groupByKey operation will group all the purchase order lines with the same key.
     *
     * - The reduce operation will sum the queatities for the aggregated purchase order lines.
     *
     *   So there will be a purchase order line per country, day and product.
     *
     * @param builder the streams builder
     * @return the builder configured with the topology
     */
    @Bean("purchaseOrderLineAggregatedStreamTopology")
    public StreamsBuilder startProcessing(
            @Qualifier("purchaseOrderLineAggregatedStreamBuilderFactoryBean") StreamsBuilder builder
    ) {
        KStream<String, CommercialOrderLineSplit> commercialOrderLinesStream = builder.stream(
                splitCommercialOrderLinesTopic,
                Consumed.with(stringKeyAvroSerde, commercialOrderLineSplitValueAvroSerde)
        );

        KStream<String, PurchaseOrderLine> purchaseOrderLinesUngroupedStream = commercialOrderLinesStream
                .map(
                        (String commercialOrderLineUuid, CommercialOrderLineSplit commercialOrderLine) -> {

                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            Date datetime = new Date(commercialOrderLine.getCommercialOrderDatetime());
                            String aggregationKey = commercialOrderLine.getShippingCountry() + "-" + df.format(datetime) + "-" + commercialOrderLine.getProductUuid();

                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(commercialOrderLine.getCommercialOrderDatetime());
                            int year = cal.get(Calendar.YEAR);
                            int month = cal.get(Calendar.MONTH);
                            int day = cal.get(Calendar.DAY_OF_MONTH);

                            cal.clear();
                            cal.set(year, month, day, 0, 0, 0);
                            long purchaseOrderDate = cal.getTimeInMillis();

                            PurchaseOrderLine purchaseOrderLine = PurchaseOrderLine
                                    .newBuilder()
                                    .setUuid(UUID.randomUUID().toString())
                                    .setAggregationKey(aggregationKey)
                                    .setCountry(commercialOrderLine.getShippingCountry())
                                    .setDate(purchaseOrderDate)
                                    .setProductUuid(commercialOrderLine.getProductUuid())
                                    .setProductName(commercialOrderLine.getProductName())
                                    .setProductType(commercialOrderLine.getProductType())
                                    .setProductBarCode(commercialOrderLine.getProductBarCode())
                                    .setProductPrice(commercialOrderLine.getProductPrice())
                                    .setQuantity(commercialOrderLine.getQuantity())
                                    .build();

                            LOGGER.info(">>> Stream - Commercial order line uuid={} mapped to purchase order line aggregation-key={}...", commercialOrderLine.getUuid(), aggregationKey);

                            return KeyValue.pair(aggregationKey, purchaseOrderLine);
                        }
                );

        KStream<String, PurchaseOrderLine> purchaseOrderLinesAggregatedStream = purchaseOrderLinesUngroupedStream
                .groupByKey(
                        Serialized.with(stringKeyAvroSerde, purchaseOrderLineValueAvroSerde)
                )
                .reduce(
                        (PurchaseOrderLine aggregatedPurchaseOrderLine, PurchaseOrderLine newPurchaseOrderLine) -> {

                            int quantity = aggregatedPurchaseOrderLine.getQuantity() + newPurchaseOrderLine.getQuantity();

                            LOGGER.info(">>> Stream - Purchase order line key={} aggregated quantity={}...", aggregatedPurchaseOrderLine.getAggregationKey(), quantity);

                            return PurchaseOrderLine
                                    .newBuilder(aggregatedPurchaseOrderLine)
                                    .setQuantity(quantity)
                                    .build();
                        }
                )
                .toStream();

        purchaseOrderLinesAggregatedStream.to(
                aggregatedPurchaseOrderLinesTopic,
                Produced.with(stringKeyAvroSerde, purchaseOrderLineValueAvroSerde)
        );

        return builder;
    }
}
